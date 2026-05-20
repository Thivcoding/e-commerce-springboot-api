package org.hokvanthiv.ecommerce_springboot_api.controller;

import jakarta.validation.Valid;
import org.hokvanthiv.ecommerce_springboot_api.dto.common.ApiResponse;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.OrderRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.OrderResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // =========================
    // CREATE ORDER
    // =========================
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(
            Authentication authentication,
            @Valid @RequestBody OrderRequestDTO request
    ) {

        String email = authentication.getName();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                orderService.createOrder(email, request),
                                "Order created successfully"
                        )
                );
    }

    // =========================
    // GET ALL ORDERS
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getAllOrders() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        orderService.getAllOrders(),
                        "Get all orders successfully"
                )
        );
    }

    // =========================
    // GET ORDER BY ID
    // =========================
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(
            Authentication authentication,
            @PathVariable Long id
    ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.success(
                        orderService.getOrderById(id, email),
                        "Get order successfully"
                )
        );
    }

    // =========================
    // GET MY ORDERS
    // =========================
        @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
        @GetMapping("/my-orders")
        public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getMyOrders(
                Authentication authentication
        ) {

            String email = authentication.getName();

            return ResponseEntity.ok(
                    ApiResponse.success(
                            orderService.getMyOrders(email),
                            "Get my orders successfully"
                    )
            );
        }

    // =========================
    // DELETE ORDER
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(
            Authentication authentication,
            @PathVariable Long id
    ) {

        String email = authentication.getName();

        orderService.deleteOrder(id, email);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order deleted successfully"
                )
        );
    }
}