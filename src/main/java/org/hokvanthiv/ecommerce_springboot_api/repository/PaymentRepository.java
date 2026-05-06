package org.hokvanthiv.ecommerce_springboot_api.repository;

import org.hokvanthiv.ecommerce_springboot_api.Enum.PaymentStatus;
import org.hokvanthiv.ecommerce_springboot_api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Optional<Payment> findByOrderId(Long orderId);

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    Optional<Payment> findByBakongTxnId(String bakongTxnId);

    List<Payment> findTop10ByOrderByCreatedAtDesc();
}