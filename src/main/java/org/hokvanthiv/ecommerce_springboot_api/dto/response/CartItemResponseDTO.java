package org.hokvanthiv.ecommerce_springboot_api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartItemResponseDTO {

    private Long id;

    private Long productId;
    private String productName;

    private Double price;
    private Integer quantity;

    private Double totalPrice;

    private LocalDateTime createdAt;
}