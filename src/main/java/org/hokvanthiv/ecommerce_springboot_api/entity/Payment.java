package org.hokvanthiv.ecommerce_springboot_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hokvanthiv.ecommerce_springboot_api.Enum.PaymentMethod;
import org.hokvanthiv.ecommerce_springboot_api.Enum.PaymentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNo;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Double amount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String qrString;
    private String bakongTxnId;

    @CreationTimestamp
    private LocalDateTime paidAt;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
