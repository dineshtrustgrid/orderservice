package com.example.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
	
	
	private Long orderId;
	private Long productId;
	private int quantity;
	private double totalprice;
	
	// product details
	
	private String productName;
	private double productPrice;
	
	

}
