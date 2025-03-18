package com.vasyerp.discount.service;

import com.vasyerp.discount.dto.DiscountRequest;
import com.vasyerp.discount.dto.ProductResponse;

public interface IProductDiscountService {
	
	 public ProductResponse applyDiscount(DiscountRequest request);
	 
	 public ProductResponse getProduct(Long id);

}
