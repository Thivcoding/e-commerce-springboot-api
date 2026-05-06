package org.hokvanthiv.ecommerce_springboot_api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartResponseDTO {

    private Long id;

    // user info (avoid exposing full User entity)
    private Long userId;
    private String userName;

    // cart items
    private List<CartItemResponseDTO> items;

    // total price of cart
    private Double totalAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}