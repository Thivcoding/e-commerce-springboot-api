package org.hokvanthiv.ecommerce_springboot_api.service;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.PaymentRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.PaymentResponseDTO;

import java.util.List;

public interface PaymentService {

    PaymentResponseDTO createPayment(
            String email,
            PaymentRequestDTO request
    );

    PaymentResponseDTO getPaymentById(
            Long id,
            String email
    );

    List<PaymentResponseDTO> getMyPayments(
            String email
    );

    List<PaymentResponseDTO> getAllPayments();

    PaymentResponseDTO updatePaymentStatus(
            Long id,
            String status
    );
}