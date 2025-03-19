package com.vasyerp.discount.service.impl;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vasyerp.discount.dto.DiscountRequest;
import com.vasyerp.discount.dto.ProductResponse;
import com.vasyerp.discount.exception.DiscountTypeNotFoundException;
import com.vasyerp.discount.exception.ProductNotFoundExcepton;
import com.vasyerp.discount.exception.ProductOutOfStockException;
import com.vasyerp.discount.model.DiscountType;
import com.vasyerp.discount.model.Product;
import com.vasyerp.discount.model.Season;
import com.vasyerp.discount.repository.IProductRepository;
import com.vasyerp.discount.repository.ISeasonRepository;
import com.vasyerp.discount.service.IProductDiscountService;

@Service
public class ProductDiscountServiceImpl implements IProductDiscountService {

	@Autowired
	private IProductRepository productRepository;

	@Autowired
	private ISeasonRepository seasonRepository;

	private static final Logger logger = LoggerFactory.getLogger(ProductDiscountServiceImpl.class);

	@Override
	@Transactional
	public ProductResponse applyDiscount(DiscountRequest request) {
		logger.info("Processing discount for product ID: {}", request.getProductId());

		Optional<Product> productOptional = productRepository.findById(request.getProductId());

		if (productOptional.isEmpty()) {
			logger.error("Product not found with ID: {}", request.getProductId());
			throw new ProductNotFoundExcepton("Product not found with ID: " + request.getProductId());
		}

		Product product = productOptional.get();
		logger.info("Product found: {} | Price: {} | Quantity: {}", product.getName(), product.getPrice(),
				product.getQuantity());

		if (product.getQuantity() <= 0) {
			logger.warn("Product {} is out of stock.", product.getName());
			throw new ProductOutOfStockException("Product is out of stock.");
		}

		Double finalPrice = calculateDiscountedPrice(product, request);

		logger.info("Final discounted price for product {}: {}", product.getName(), finalPrice);

		logger.info("Applying discount for product: {} with ID: {}", product.getName(), product.getId());

		product.setPrice(finalPrice);
		logger.debug("Updated product price after discount: ${}", finalPrice);

		product.setSeasonalDiscountActive(request.getSeasonalDiscountActive());
		logger.debug("Updated seasonal discount status: {}", request.getSeasonalDiscountActive());

		if(product.getSeason().getId() == null) {
			Random random = new Random();
			long seasonId = random.nextInt(3) + 1L;

			logger.info("Generated random seasonId: {}", seasonId);

			Optional<Season> optionalSeason = seasonRepository.findById(seasonId);

			if (optionalSeason.isPresent()) {
				Season season = optionalSeason.get();
				product.setSeason(season);
				logger.info("Assigned Season (ID: {}, Name: {}) to Product: {}", season.getId(), season.getName(),
						product.getName());
			} else {
				logger.warn("No season found with ID: {}", seasonId);
			}
		}

		Product savedProduct = productRepository.save(product);
		logger.info("Product details updated and saved successfully for ID: {}", savedProduct.getId());

		ProductResponse productResponse = new ProductResponse();
		productResponse.setProductId(savedProduct.getId());
		productResponse.setProductName(savedProduct.getName());
		productResponse.setFinalPrice(savedProduct.getPrice());

		logger.info("Returning ProductResponse: Product ID: {}, Name: {}, Final Price: ${}", savedProduct.getId(),
				savedProduct.getName(), savedProduct.getPrice());

		return productResponse;
	}

	private Double calculateDiscountedPrice(Product product, DiscountRequest request) {
		logger.info("Calculating discounted price for product: {} with discount type: {}", product.getName(),
				request.getDiscountType());

		Double finalPrice = product.getPrice();

		if (DiscountType.PERCENTAGE.name().equalsIgnoreCase(request.getDiscountType().name())) {
			finalPrice -= (product.getPrice() * (request.getDiscountValue() / 100.0));
			logger.debug("Applied percentage discount: {}%. New price: {}", request.getDiscountValue(), finalPrice);
		} else if (DiscountType.FLAT.name().equalsIgnoreCase(request.getDiscountType().name())) {
			finalPrice -= request.getDiscountValue();
			logger.debug("Applied flat discount: {}. New price: {}", request.getDiscountValue(), finalPrice);
		} else {
			logger.error("Invalid discount type provided: {}", request.getDiscountType());
			throw new DiscountTypeNotFoundException("Invalid discount type: " + request.getDiscountType());
		}

		if (isProductEligibleForSeasonalDiscount(product, request.getSeasonalDiscountActive())) {
			double seasonalDiscount = finalPrice * (product.getSeason().getDiscountRate() / 100);
			finalPrice -= seasonalDiscount;
			logger.info("Seasonal discount applied: {}%. New price: {}", product.getSeason().getDiscountRate(),
					finalPrice);
		} else {
			logger.info("No seasonal discount applied for product: {}", product.getName());
		}

		if (finalPrice < 0) {
			logger.warn("Final price after discount calculation is negative. Setting to 0.");
			finalPrice = 0.0;
		}

		DecimalFormat df = new DecimalFormat("#.##");
		finalPrice = Double.parseDouble(df.format(finalPrice));
		return finalPrice;
	}

	private boolean isProductEligibleForSeasonalDiscount(Product product, Boolean seasonalDiscountActive) {
		logger.info("Checking seasonal discount eligibility for product: {}", product.getName());

		LocalDate today = LocalDate.now();

		boolean isEligible = seasonalDiscountActive != null && seasonalDiscountActive
				&& product.getSeasonalDiscountActive() && product.getSeason() != null
				&& product.getSeason().getStartDate() != null && product.getSeason().getEndDate() != null
				&& !today.isBefore(product.getSeason().getStartDate())
				&& !today.isAfter(product.getSeason().getEndDate());

		if (isEligible) {
			logger.info("Product {} is eligible for seasonal discount.", product.getName());
		} else {
			logger.info("Product {} is NOT eligible for seasonal discount.", product.getName());
		}

		return isEligible;
	}

	@Override
	public ProductResponse getProduct(Long id) {
		logger.info("Fetching product details for ID: {}", id);
		Optional<Product> productOptional = productRepository.findById(id);

		if (productOptional.isEmpty()) {
			logger.error("Product not found with ID: {}", id);
			throw new ProductNotFoundExcepton("Product not found with ID: " + id);
		}

		Product product = productOptional.get();
		logger.info("Product details retrieved: {}", product.getName());

		ProductResponse productResponse = new ProductResponse();
		productResponse.setProductId(product.getId());
		productResponse.setProductName(product.getName());
		productResponse.setFinalPrice(product.getPrice());

		logger.info("Returning ProductResponse: Product ID: {}, Name: {}, Final Price: ${}", product.getId(),
				product.getName(), product.getPrice());

		return productResponse;
	}
}
