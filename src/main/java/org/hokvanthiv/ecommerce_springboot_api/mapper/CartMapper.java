package org.hokvanthiv.ecommerce_springboot_api.mapper;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.CartRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.CartItemResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.CartResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.Cart;
import org.hokvanthiv.ecommerce_springboot_api.entity.CartItem;
import org.hokvanthiv.ecommerce_springboot_api.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    // =========================
    // REQUEST DTO -> ENTITY
    // =========================
    public static Cart toEntity(CartRequestDTO dto, User user) {
        if (dto == null) return null;

        Cart cart = new Cart();
        cart.setUser(user);

        return cart;
    }

    // =========================
    // ENTITY -> RESPONSE DTO
    // =========================
    public static CartResponseDTO toDTO(Cart cart) {
        if (cart == null) return null;

        CartResponseDTO dto = new CartResponseDTO();

        dto.setId(cart.getId());

        // user
        if (cart.getUser() != null) {
            dto.setUserId(cart.getUser().getId());
            dto.setUserName(cart.getUser().getName());
        }

        // items (never null)
        List<CartItemResponseDTO> items = cart.getItems() == null
                ? List.of()
                : cart.getItems()
                .stream()
                .map(CartMapper::mapCartItem)
                .collect(Collectors.toList());

        dto.setItems(items);

        // total
        dto.setTotalAmount(calculateTotal(cart));

        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());

        return dto;
    }

    // =========================
    // CART ITEM
    // =========================
    private static CartItemResponseDTO mapCartItem(CartItem item) {
        if (item == null) return null;

        CartItemResponseDTO dto = new CartItemResponseDTO();

        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());

        if (item.getProduct() != null) {
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());

            // important: use current product price OR stored price
            dto.setPrice(item.getProduct().getPrice());
        }

        // total price per item
        double price = dto.getPrice() != null ? dto.getPrice() : 0.0;
        int qty = dto.getQuantity() != null ? dto.getQuantity() : 0;

        dto.setTotalPrice(price * qty);

        return dto;
    }

    // =========================
    // TOTAL
    // =========================
    private static Double calculateTotal(Cart cart) {
        if (cart.getItems() == null) return 0.0;

        return cart.getItems()
                .stream()
                .mapToDouble(item -> {
                    double price = item.getProduct() != null
                            ? item.getProduct().getPrice()
                            : 0.0;

                    int qty = item.getQuantity() != null
                            ? item.getQuantity()
                            : 0;

                    return price * qty;
                })
                .sum();
    }
}