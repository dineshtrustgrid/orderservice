package com.example.order_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.order_service.dto.OrderResponseDTO;
import com.example.order_service.dto.ProductDTO;
import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRespository;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderRespository orderRespository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	
	// create a order 
	 
	@PostMapping("/placeOrder")
	public Mono<ResponseEntity<OrderResponseDTO>> placeOrder(@RequestBody Order order)
	{
		// fetch product 
		
		if (order == null || order.getProductId() == null || order.getQuantity() == null) {
	        return Mono.just(ResponseEntity.badRequest().build());
	    }
		
		return webClientBuilder.build().get().uri("http://localhost:8081/products/"+order.getProductId()).retrieve()
				.bodyToMono(ProductDTO.class).map(productDTO ->{
					OrderResponseDTO  responseDTO = new OrderResponseDTO();
					
					responseDTO.setProductId(order.getProductId());
					responseDTO.setQuantity(order.getQuantity());
					
					// set product details
					responseDTO.setProductName(productDTO.getName());
					responseDTO.setProductPrice(productDTO.getPrice());
					responseDTO.setTotalprice(order.getQuantity()*productDTO.getPrice());
					
					//save repository
					
					orderRespository.save(order);
					responseDTO.setOrderId(order.getId());
					return ResponseEntity.ok(responseDTO);
					
				});
	}
	
	
//	@PostMapping("/placeOrder")
//	public Mono<ResponseEntity<OrderResponseDTO>> placeOrder(@RequestBody Order order) {
//
//	    if (order.getProductId() == null) {
//	        return Mono.error(new IllegalArgumentException("Product ID cannot be null"));
//	    }
//
//	    return webClientBuilder.build()
//	            .get()
//	            .uri("http://localhost:8081/products/{productId}", order.getProductId())
//	            .retrieve()
//	            .bodyToMono(ProductDTO.class)
//	            .map(productDTO -> {
//
//	                OrderResponseDTO responseDTO = new OrderResponseDTO();
//	                responseDTO.setProductId(order.getProductId());
//	                responseDTO.setQuantity(order.getQuantity());
//	                responseDTO.setProductName(productDTO.getName());
//	                responseDTO.setProductPrice(productDTO.getPrice());
//	                responseDTO.setTotalprice(order.getQuantity() * productDTO.getPrice());
//
//	                orderRespository.save(order);
//	                responseDTO.setOrderId(order.getId());
//
//	                return ResponseEntity.ok(responseDTO);
//	            });
//	}
//
	
	// get All order
	
	@GetMapping
	public List<Order> getAllOrders(){
		
		return orderRespository.findAll();
		
	}
	
	
	

	
	

}
