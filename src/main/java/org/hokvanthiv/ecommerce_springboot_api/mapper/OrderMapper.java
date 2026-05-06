package org.hokvanthiv.ecommerce_springboot_api.mapper;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.OrderRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.OrderItemResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.OrderResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    // =========================
    // REQUEST -> ENTITY
    // =========================
    public static Order toEntity(OrderRequestDTO dto, User user, Cart cart) {
        if (dto == null) return null;

        Order order = new Order();

        order.setUser(user);
        order.setCart(cart);

        order.setFullName(dto.getFullName());
        order.setPhone(dto.getPhone());
        order.setAddressLine(dto.getAddressLine());
        order.setCity(dto.getCity());
        order.setCountry(dto.getCountry());

        return order;
    }

    // =========================
    // ENTITY -> RESPONSE
    // =========================
    public static OrderResponseDTO toDTO(Order order) {
        if (order == null) return null;

        OrderResponseDTO dto = new OrderResponseDTO();

        dto.setId(order.getId());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus());

        // user
        if (order.getUser() != null) {
            dto.setUserId(order.getUser().getId());
            dto.setUserName(order.getUser().getName());
        }

        // shipping
        dto.setFullName(order.getFullName());
        dto.setPhone(order.getPhone());
        dto.setAddressLine(order.getAddressLine());
        dto.setCity(order.getCity());
        dto.setCountry(order.getCountry());

        // items (never null)
        List<OrderItemResponseDTO> items = order.getItems() == null
                ? List.of()
                : order.getItems()
                .stream()
                .map(OrderMapper::mapItem)
                .collect(Collectors.toList());

        dto.setItems(items);

        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        return dto;
    }

    // =========================
    // ITEM MAPPING
    // =========================
    private static OrderItemResponseDTO mapItem(OrderItem item) {
        if (item == null) return null;

        OrderItemResponseDTO dto = new OrderItemResponseDTO();

        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());

        if (item.getProduct() != null) {
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
        }

        // total per item
        double price = item.getPrice() != null ? item.getPrice() : 0.0;
        int qty = item.getQuantity() != null ? item.getQuantity() : 0;

        dto.setTotalPrice(price * qty);

        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }

    // =========================
    // CALCULATE TOTAL (OPTIONAL)
    // =========================
    public static Double calculateTotal(List<OrderItem> items) {
        if (items == null) return 0.0;

        return items.stream()
                .mapToDouble(item -> {
                    double price = item.getPrice() != null ? item.getPrice() : 0.0;
                    int qty = item.getQuantity() != null ? item.getQuantity() : 0;
                    return price * qty;
                })
                .sum();
    }
}