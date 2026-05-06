package org.hokvanthiv.ecommerce_springboot_api.mapper;

import org.hokvanthiv.ecommerce_springboot_api.dto.response.PaymentResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.Order;
import org.hokvanthiv.ecommerce_springboot_api.entity.Payment;

public class PaymentMapper {

    public static Payment toEntity(Order order, Double amount, String currency) {
        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setAmount(amount);
        payment.setCurrency(currency);

        return payment;
    }

    // =========================
    // ENTITY -> RESPONSE DTO
    // =========================
    public static PaymentResponseDTO toDTO(Payment payment) {
        if (payment == null) return null;

        PaymentResponseDTO dto = new PaymentResponseDTO();

        dto.setId(payment.getId());
        dto.setInvoiceNo(payment.getInvoiceNo());
        dto.setPaymentMethod(payment.getPaymentMethod());

        dto.setAmount(payment.getAmount());
        dto.setCurrency(payment.getCurrency());

        dto.setPaymentStatus(payment.getPaymentStatus());

        dto.setQrString(payment.getQrString());
        dto.setBakongTxnId(payment.getBakongTxnId());

        dto.setPaidAt(payment.getPaidAt());
        dto.setCreatedAt(payment.getCreatedAt());

        // safe order mapping
        if (payment.getOrder() != null) {
            dto.setOrderId(payment.getOrder().getId());
        }

        return dto;
    }
}