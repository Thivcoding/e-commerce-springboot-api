package org.hokvanthiv.ecommerce_springboot_api.controller;

import jakarta.validation.Valid;
import org.hokvanthiv.ecommerce_springboot_api.dto.common.ApiResponse;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.CartItemRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.CartResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.service.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    // =========================
    // ADD ITEM TO CART
    // =========================
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<CartResponseDTO>> addItemToCart(

            Authentication authentication,

            @Valid @RequestBody CartItemRequestDTO request
    ) {

        String email = authentication.getName();

        CartResponseDTO response =
                cartItemService.addItemToCart(email, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                response,
                                "Item added to cart successfully"
                        )
                );
    }

    // =========================
    // UPDATE QUANTITY
    // =========================
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{itemId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> updateCartItem(

            Authentication authentication,

            @PathVariable Long itemId,

            @RequestParam Integer quantity
    ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.success(
                        cartItemService.updateCartItem(
                                email,
                                itemId,
                                quantity
                        ),
                        "Cart item updated successfully"
                )
        );
    }

    // =========================
    // REMOVE ITEM
    // =========================
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> removeItemFromCart(

            Authentication authentication,

            @PathVariable Long itemId
    ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.success(
                        cartItemService.removeItemFromCart(
                                email,
                                itemId
                        ),
                        "Item removed from cart successfully"
                )
        );
    }
}