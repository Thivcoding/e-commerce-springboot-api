package org.hokvanthiv.ecommerce_springboot_api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderItemResponseDTO {

    private Long id;

    private Long productId;
    private String productName;

    private Integer quantity;
    private Double price;

    private Double totalPrice;

    private LocalDateTime createdAt;
}