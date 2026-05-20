package org.hokvanthiv.ecommerce_springboot_api.controller;

import jakarta.validation.Valid;
import org.hokvanthiv.ecommerce_springboot_api.dto.common.ApiResponse;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.CartRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.CartResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // =========================
    // CREATE CART
    // =========================
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<CartResponseDTO>> createCart(
            Authentication authentication
    ) {

        String email = authentication.getName();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        cartService.createCart(email),
                        "Cart created successfully"
                ));
    }

    // =========================
    // GET ALL CARTS
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CartResponseDTO>>> getAllCarts() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        cartService.getAllCarts(),
                        "Get all carts successfully"
                )
        );
    }

    // =========================
    // GET CART BY ID
    // =========================
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> getCartById(
            Authentication authentication,
            @PathVariable Long id
    ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.success(
                        cartService.getCartById(id, email),
                        "Get cart successfully"
                )
        );
    }

    // =========================
// GET MY CART
// =========================
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/my-cart")
    public ResponseEntity<ApiResponse<CartResponseDTO>> getMyCart(
            Authentication authentication
    ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.success(
                        cartService.getMyCart(email),
                        "Get my cart successfully"
                )
        );
    }

    // =========================
    // UPDATE CART
    // =========================
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> updateCart(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody CartRequestDTO request
    ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.success(
                        cartService.updateCart(id, email, request),
                        "Cart updated successfully"
                )
        );
    }

    // =========================
    // DELETE CART
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCart(
            @PathVariable Long id ,
            Authentication authentication
    ) {

        String email = authentication.getName();

        cartService.deleteCart(id,email);

        return ResponseEntity.ok(
                ApiResponse.success("Cart deleted successfully")
        );
    }
}