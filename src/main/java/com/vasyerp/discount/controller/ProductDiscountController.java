package com.vasyerp.discount.controller;

import com.vasyerp.discount.dto.DiscountRequest;
import com.vasyerp.discount.dto.ProductResponse;
import com.vasyerp.discount.service.IProductDiscountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductDiscountController {

    private static final Logger logger = LoggerFactory.getLogger(ProductDiscountController.class);
    
    private final IProductDiscountService productDiscountService;

    public ProductDiscountController(IProductDiscountService productDiscountService) {
        this.productDiscountService = productDiscountService;
    }

   
    @PostMapping("/discount")
    public ResponseEntity<ProductResponse> applyDiscount(@Valid @RequestBody DiscountRequest request) {
        logger.info("Received request to apply discount: {}", request);
        ProductResponse response = productDiscountService.applyDiscount(request);
        return ResponseEntity.ok(response);
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        logger.info("Fetching product details for ID: {}", id);
        ProductResponse response = productDiscountService.getProduct(id);
        return ResponseEntity.ok(response);
    }
}
