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
import org.hokvanthiv.ecommerce_springboot_api.service.BakongClientService;
import org.hokvanthiv.ecommerce_springboot_api.service.PaymentService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BakongClientService bakongClientService;

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
        // CASH PAYMENT
        // =========================
        if (request.getPaymentMethod() == PaymentMethod.CASH) {

            payment.setPaymentMethod(PaymentMethod.CASH);
            payment.setPaymentStatus(PaymentStatus.PAID);
            payment.setPaidAt(LocalDateTime.now());

            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
        }

        // =========================
        // BAKONG PAYMENT
        // =========================
        else if (request.getPaymentMethod() == PaymentMethod.BAKONG) {

            payment.setPaymentMethod(PaymentMethod.BAKONG);
            payment.setPaymentStatus(PaymentStatus.PENDING);

            Map<String, Object> response =
                    bakongClientService.generateQR(
                            order.getId(),
                            request.getAmount()
                    );

            payment.setQrString((String) response.get("qr"));

            // FIXED: store md5 correctly
            payment.setBakongTxnId((String) response.get("md5"));

            payment.setCurrency(request.getCurrency());

            order.setStatus(OrderStatus.PENDING);
            orderRepository.save(order);
        }

        Payment saved = paymentRepository.save(payment);

        return PaymentMapper.toDTO(saved);
    }

    // =========================
    // PAYMENT CHECK BY MD5
    // =========================

    @Override
    @Transactional
    public PaymentResponseDTO checkBakongPayment(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        // 1. already paid → return immediately
        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            return PaymentMapper.toDTO(payment);
        }

        // 2. must have md5 (Bakong reference)
        if (payment.getBakongTxnId() == null) {
            throw new IllegalStateException("Missing Bakong MD5 / Txn ID");
        }

        // 3. call Laravel (Bakong service)
        Map<String, Object> result =
                bakongClientService.checkPayment(payment.getBakongTxnId());

        if (result == null) {
            throw new RuntimeException("Bakong service no response");
        }

        // 4. extract response code (safe parsing)
        Object codeObj = result.get("responseCode");
        int responseCode = codeObj != null ? Integer.parseInt(codeObj.toString()) : -1;

        boolean isPaid = (responseCode == 0);

        // 5. update DB if PAID
        if (isPaid) {

            payment.setPaymentStatus(PaymentStatus.PAID);
            payment.setPaidAt(LocalDateTime.now());

            // optional transaction id from Bakong
            if (result.get("data") instanceof Map<?, ?> data) {
                Object hash = data.get("hash");
                if (hash != null) {
                    payment.setBakongTxnId(hash.toString());
                }
            }

            // update order
            Order order = payment.getOrder();
            if (order != null) {
                order.setStatus(OrderStatus.PAID);
                orderRepository.save(order);
            }

            paymentRepository.save(payment);
        }

        // 6. return updated DTO
        PaymentResponseDTO dto = PaymentMapper.toDTO(payment);
        return dto;
    }

    // =========================
    // GET PAYMENT BY ID
    // =========================
    @Override
    public PaymentResponseDTO getPaymentById(Long id, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        boolean isOwner = payment.getOrder().getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You cannot access this payment");
        }

        return PaymentMapper.toDTO(payment);
    }

    // =========================
    // GET MY PAYMENTS
    // =========================
    @Override
    public List<PaymentResponseDTO> getMyPayments(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return paymentRepository.findAll()
                .stream()
                .filter(p -> p.getOrder().getUser().getId().equals(user.getId()))
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
    @Transactional
    public PaymentResponseDTO updatePaymentStatus(Long id, PaymentStatus status) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException("Payment already completed");
        }

        payment.setPaymentStatus(status);

        if (status == PaymentStatus.PAID) {

            payment.setPaidAt(LocalDateTime.now());

            // FIXED: real txn id from Bakong later webhook
            payment.setBakongTxnId(payment.getBakongTxnId() != null
                    ? payment.getBakongTxnId()
                    : UUID.randomUUID().toString());

            Order order = payment.getOrder();

            if (order != null) {
                order.setStatus(OrderStatus.PAID);
                orderRepository.save(order);
            }
        }

        Payment updated = paymentRepository.save(payment);

        return PaymentMapper.toDTO(updated);
    }
}