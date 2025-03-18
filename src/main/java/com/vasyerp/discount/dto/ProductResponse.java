package com.vasyerp.discount.dto;

import lombok.Data;

@Data
public class ProductResponse {

	private Long productId;
    private String productName;
    private Double finalPrice;
}
