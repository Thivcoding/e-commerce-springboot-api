package org.hokvanthiv.ecommerce_springboot_api.mapper;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.PaymentRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.PaymentResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.Order;
import org.hokvanthiv.ecommerce_springboot_api.entity.Payment;

public class PaymentMapper {

    // =========================
    // REQUEST -> ENTITY
    // =========================
    public static Payment toEntity(
            PaymentRequestDTO dto,
            Order order
    ) {

        if (dto == null) return null;

        Payment payment = new Payment();

        payment.setOrder(order);

        payment.setPaymentMethod(dto.getPaymentMethod());

        payment.setAmount(dto.getAmount());

        payment.setCurrency(dto.getCurrency());

        return payment;
    }

    // =========================
    // ENTITY -> RESPONSE
    // =========================
    public static PaymentResponseDTO toDTO(
            Payment payment
    ) {

        if (payment == null) return null;

        PaymentResponseDTO dto =
                new PaymentResponseDTO();

        dto.setId(payment.getId());

        dto.setInvoiceNo(payment.getInvoiceNo());

        dto.setPaymentMethod(
                payment.getPaymentMethod()
        );

        dto.setAmount(payment.getAmount());

        dto.setCurrency(payment.getCurrency());

        dto.setPaymentStatus(
                payment.getPaymentStatus()
        );

        dto.setQrString(payment.getQrString());

        dto.setBakongTxnId(
                payment.getBakongTxnId()
        );

        dto.setPaidAt(payment.getPaidAt());

        if (payment.getOrder() != null) {
            dto.setOrderId(
                    payment.getOrder().getId()
            );
        }

        dto.setCreatedAt(payment.getCreatedAt());

        return dto;
    }
}