package org.hokvanthiv.ecommerce_springboot_api.service.Impl;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.CartRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.CartResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.Cart;
import org.hokvanthiv.ecommerce_springboot_api.entity.User;
import org.hokvanthiv.ecommerce_springboot_api.exception.AccessDeniedException;
import org.hokvanthiv.ecommerce_springboot_api.exception.ResourceNotFoundException;
import org.hokvanthiv.ecommerce_springboot_api.mapper.CartMapper;
import org.hokvanthiv.ecommerce_springboot_api.repository.CartRepository;
import org.hokvanthiv.ecommerce_springboot_api.repository.UserRepository;
import org.hokvanthiv.ecommerce_springboot_api.service.CartService;
//import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // CREATE CART (JWT SAFE)
    // =========================
    @Override
    public CartResponseDTO createCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        return CartMapper.toDTO(cart);
    }

    // =========================
    // GET CART BY ID (OWNERSHIP CHECK)
    // =========================
    @Override
    public CartResponseDTO getCartById(Long id, String email) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));

        if (!cart.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("You are not allowed to access this cart");
        }

        return CartMapper.toDTO(cart);
    }

    // =========================
    // GET ALL CARTS (ADMIN ONLY CONTROLLER)
    // =========================
    @Override
    public List<CartResponseDTO> getAllCarts() {

        return cartRepository.findAll()
                .stream()
                .map(CartMapper::toDTO)
                .collect(Collectors.toList());
    }

    // =========================
    // GET MY CART
    // =========================
        @Override
        public CartResponseDTO getMyCart(String email) {

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "User not found"
                            ));

            Cart cart = cartRepository.findByUserId(user.getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Cart not found"
                            ));

            return CartMapper.toDTO(cart);
        }

    // =========================
    // UPDATE CART (FIXED)
    // =========================
    @Override
    public CartResponseDTO updateCart(Long id, String email, CartRequestDTO requestDTO) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));

        // ownership check
        if (!cart.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Access denied");
        }

        // example update logic (you can extend later)
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        cart.setUser(user);

        Cart updatedCart = cartRepository.save(cart);

        return CartMapper.toDTO(updatedCart);
    }

    // =========================
    // DELETE CART (SECURE VERSION)
    // =========================
    @Override
    public void deleteCart(Long id, String email) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));

        // ownership check
        if (!cart.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Access denied");
        }

        cartRepository.delete(cart);
    }
}