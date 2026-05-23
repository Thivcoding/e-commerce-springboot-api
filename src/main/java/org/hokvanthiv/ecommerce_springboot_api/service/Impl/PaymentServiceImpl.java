package org.hokvanthiv.ecommerce_springboot_api.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hokvanthiv.ecommerce_springboot_api.Enum.OrderStatus;
import org.hokvanthiv.ecommerce_springboot_api.Enum.PaymentMethod;
import org.hokvanthiv.ecommerce_springboot_api.Enum.PaymentStatus;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.PaymentRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.PaymentResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.Order;
import org.hokvanthiv.ecommerce_springboot_api.entity.Payment;
import org.hokvanthiv.ecommerce_springboot_api.entity.User;
import org.hokvanthiv.ecommerce_springboot_api.exception.DuplicateResourceException;
import org.hokvanthiv.ecommerce_springboot_api.exception.ResourceNotFoundException;
import org.hokvanthiv.ecommerce_springboot_api.mapper.PaymentMapper;
import org.hokvanthiv.ecommerce_springboot_api.repository.OrderRepository;
import org.hokvanthiv.ecommerce_springboot_api.repository.PaymentRepository;
import org.hokvanthiv.ecommerce_springboot_api.repository.UserRepository;
import org.hokvanthiv.ecommerce_springboot_api.service.PaymentService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // =========================
    // CREATE PAYMENT
    // =========================
    @Override
    public PaymentResponseDTO createPayment(String email, PaymentRequestDTO request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot pay another user's order");
        }

        paymentRepository.findByOrderId(order.getId())
                .ifPresent(p -> {
                    throw new DuplicateResourceException("Payment already exists");
                });

        Payment payment = PaymentMapper.toEntity(request, order);

        payment.setInvoiceNo("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // =========================
        // PAYMENT LOGIC FIXED
        // =========================
        if (request.getPaymentMethod() == PaymentMethod.CASH) {

            payment.setPaymentMethod(PaymentMethod.CASH);
            payment.setPaymentStatus(PaymentStatus.PAID); // ✅ AUTO PAID

            payment.setPaidAt(LocalDateTime.now());

            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);

        } else if (request.getPaymentMethod() == PaymentMethod.BAKONG) {

            payment.setPaymentMethod(PaymentMethod.BAKONG);
            payment.setPaymentStatus(PaymentStatus.PENDING); // ⏳ WAIT PAYMENT

            payment.setQrString("000201010211KHQR123456789");

            order.setStatus(OrderStatus.PENDING); // optional
            orderRepository.save(order);
        }

        Payment saved = paymentRepository.save(payment);

        return PaymentMapper.toDTO(saved);
    }

    // =========================
    // GET PAYMENT BY ID
    // =========================
    @Override
    public PaymentResponseDTO getPaymentById(
            Long id,
            String email
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Payment payment = paymentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found"
                        ));

        boolean isOwner =
                payment.getOrder()
                        .getUser()
                        .getId()
                        .equals(user.getId());

        boolean isAdmin =
                user.getRole().name()
                        .equals("ADMIN");

        if (!isOwner && !isAdmin) {

            throw new AccessDeniedException(
                    "You cannot access this payment"
            );
        }

        return PaymentMapper.toDTO(payment);
    }

    // =========================
    // GET MY PAYMENTS
    // =========================
    @Override
    public List<PaymentResponseDTO> getMyPayments(
            String email
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        return paymentRepository.findAll()
                .stream()
                .filter(payment ->
                        payment.getOrder()
                                .getUser()
                                .getId()
                                .equals(user.getId())
                )
                .map(PaymentMapper::toDTO)
                .toList();
    }

    // =========================
    // GET ALL PAYMENTS
    // =========================
    @Override
    public List<PaymentResponseDTO> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(PaymentMapper::toDTO)
                .toList();
    }

    // =========================
    // UPDATE PAYMENT STATUS
    // =========================
    @Transactional
    @Override
    public PaymentResponseDTO updatePaymentStatus(Long id, PaymentStatus status) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException("Payment already completed");
        }

        payment.setPaymentStatus(status);

        Order order = payment.getOrder();

        if (status == PaymentStatus.PAID) {

            payment.setPaidAt(LocalDateTime.now());
            payment.setBakongTxnId(UUID.randomUUID().toString());

            if (order != null) {
                order.setStatus(OrderStatus.PAID);
                orderRepository.save(order);
            }
        }

        Payment updated = paymentRepository.save(payment);

        return PaymentMapper.toDTO(updated);
    }
}