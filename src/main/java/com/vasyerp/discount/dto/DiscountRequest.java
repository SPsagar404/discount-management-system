package com.vasyerp.discount.dto;

import com.vasyerp.discount.model.DiscountType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class DiscountRequest {
	
	  @NotNull(message = "Product ID is required")
	  private Long productId;

	  @NotNull(message = "Discount type is required")
	  private DiscountType discountType;

	  @Min(value = 0, message = "Discount value must be non-negative")
	  private Double discountValue;

	  private Boolean seasonalDiscountActive;

	
}
