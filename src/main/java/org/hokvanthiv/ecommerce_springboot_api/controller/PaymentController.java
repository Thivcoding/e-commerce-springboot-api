package org.hokvanthiv.ecommerce_springboot_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hokvanthiv.ecommerce_springboot_api.dto.common.ApiResponse;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.PaymentRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.PaymentStatusUpdateRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.PaymentResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // =========================
    // CREATE PAYMENT
    // =========================
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponseDTO>>
    createPayment(
            Authentication authentication,
            @Valid @RequestBody PaymentRequestDTO request
    ) {

        String email = authentication.getName();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                paymentService.createPayment(
                                        email,
                                        request
                                ),
                                "Payment created successfully"
                        )
                );
    }

    // =========================
    // GET PAYMENT BY ID
    // =========================
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>>
    getPaymentById(
            Authentication authentication,
            @PathVariable Long id
    ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.success(
                        paymentService.getPaymentById(
                                id,
                                email
                        ),
                        "Get payment successfully"
                )
        );
    }

    // =========================
    // GET MY PAYMENTS
    // =========================
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/my-payments")
    public ResponseEntity<ApiResponse<List<PaymentResponseDTO>>>
    getMyPayments(
            Authentication authentication
    ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                ApiResponse.success(
                        paymentService.getMyPayments(email),
                        "Get my payments successfully"
                )
        );
    }

    // =========================
    // GET ALL PAYMENTS
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponseDTO>>>
    getAllPayments() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        paymentService.getAllPayments(),
                        "Get all payments successfully"
                )
        );
    }

    // =========================
    // UPDATE PAYMENT STATUS
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> updatePaymentStatus(
            @PathVariable Long id,
            @Valid @RequestBody PaymentStatusUpdateRequestDTO request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        paymentService.updatePaymentStatus(
                                id,
                                request.getStatus()
                        ),
                        "Payment status updated successfully"
                )
        );
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}/check-bakong")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> checkBakong(@PathVariable Long id) {

        PaymentResponseDTO response = paymentService.checkBakongPayment(id);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Check bakong success")
        );
    }
}