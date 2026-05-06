package org.hokvanthiv.ecommerce_springboot_api.dto.response;

import lombok.Data;
import org.hokvanthiv.ecommerce_springboot_api.Enum.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {

    private Long id;

    private Double totalPrice;
    private OrderStatus status;

    // user info (avoid full User entity)
    private Long userId;
    private String userName;

    // shipping info
    private String fullName;
    private String phone;
    private String addressLine;
    private String city;
    private String country;

    // items
    private List<OrderItemResponseDTO> items;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}