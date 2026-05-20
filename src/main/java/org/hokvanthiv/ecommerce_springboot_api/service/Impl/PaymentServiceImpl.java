package org.hokvanthiv.ecommerce_springboot_api.service.Impl;

import lombok.RequiredArgsConstructor;
import org.hokvanthiv.ecommerce_springboot_api.Enum.OrderStatus;
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
    public PaymentResponseDTO createPayment(
            String email,
            PaymentRequestDTO request
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Order order = orderRepository
                .findById(request.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"
                        ));

        // owner check
        if (!order.getUser().getId()
                .equals(user.getId())) {

            throw new AccessDeniedException(
                    "You cannot pay another user's order"
            );
        }

        // already paid check
        paymentRepository.findByOrderId(order.getId())
                .ifPresent(payment -> {
                    throw new DuplicateResourceException(
                            "Payment already exists for this order"
                    );
                });

        Payment payment = PaymentMapper
                .toEntity(request, order);

        // generate invoice
        payment.setInvoiceNo(
                "INV-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase()
        );

        payment.setPaymentStatus(
                PaymentStatus.PENDING
        );

        // fake KHQR string
        payment.setQrString(
                "000201010211KHQR123456789"
        );

        Payment savedPayment =
                paymentRepository.save(payment);

        return PaymentMapper.toDTO(savedPayment);
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
    @Override
    public PaymentResponseDTO updatePaymentStatus(
            Long id,
            String status
    ) {

        Payment payment = paymentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found"
                        ));

        PaymentStatus paymentStatus =
                PaymentStatus.valueOf(
                        status.toUpperCase()
                );

        payment.setPaymentStatus(paymentStatus);

        // if paid
        if (paymentStatus == PaymentStatus.PAID) {

            payment.setPaidAt(
                    LocalDateTime.now()
            );

            payment.setBakongTxnId(
                    UUID.randomUUID().toString()
            );

            // update order status
            Order order = payment.getOrder();

            order.setStatus(OrderStatus.PAID);
        }

        Payment updatedPayment =
                paymentRepository.save(payment);

        return PaymentMapper.toDTO(updatedPayment);
    }
}