package org.hokvanthiv.ecommerce_springboot_api.dto.response;

import lombok.Data;
import org.hokvanthiv.ecommerce_springboot_api.Enum.PaymentMethod;
import org.hokvanthiv.ecommerce_springboot_api.Enum.PaymentStatus;

import java.time.LocalDateTime;

@Data
public class PaymentResponseDTO {

    private Long id;

    private String invoiceNo;

    private PaymentMethod paymentMethod;

    private Double amount;
    private String currency;

    private PaymentStatus paymentStatus;

    private String qrString;
    private String bakongTxnId;

    private LocalDateTime paidAt;

    // Order info (avoid full object to prevent recursion)
    private Long orderId;

    private LocalDateTime createdAt;
}