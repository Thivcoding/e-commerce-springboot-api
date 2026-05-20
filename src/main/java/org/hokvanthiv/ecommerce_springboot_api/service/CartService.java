package org.hokvanthiv.ecommerce_springboot_api.service;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.CartRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.CartResponseDTO;

import java.util.List;

public interface CartService {

    CartResponseDTO createCart(String email);

    CartResponseDTO getMyCart(String email);

    CartResponseDTO getCartById(Long id, String email);

    List<CartResponseDTO> getAllCarts();

    CartResponseDTO updateCart(Long id, String email, CartRequestDTO requestDTO);

    void deleteCart(Long id, String email);
}
