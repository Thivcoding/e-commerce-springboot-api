package org.hokvanthiv.ecommerce_springboot_api.service;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.CartItemRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.CartResponseDTO;

public interface CartItemService {

    CartResponseDTO addItemToCart(String email, CartItemRequestDTO request);

    CartResponseDTO updateCartItem(
            String email,
            Long itemId,
            Integer quantity
    );

    CartResponseDTO removeItemFromCart(
            String email,
            Long itemId
    );

}