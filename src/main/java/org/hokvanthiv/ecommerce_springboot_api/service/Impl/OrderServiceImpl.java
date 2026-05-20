package org.hokvanthiv.ecommerce_springboot_api.service.Impl;

import lombok.RequiredArgsConstructor;
import org.hokvanthiv.ecommerce_springboot_api.Enum.OrderStatus;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.OrderRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.OrderResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.*;
import org.hokvanthiv.ecommerce_springboot_api.exception.AccessDeniedException;
import org.hokvanthiv.ecommerce_springboot_api.exception.ResourceNotFoundException;
import org.hokvanthiv.ecommerce_springboot_api.mapper.OrderMapper;
import org.hokvanthiv.ecommerce_springboot_api.repository.*;
import org.hokvanthiv.ecommerce_springboot_api.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    // =========================
    // CREATE ORDER
    // =========================
    @Override
    public OrderResponseDTO createOrder(
            String email,
            OrderRequestDTO dto
    ) {

        // current user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // cart
        Cart cart = cartRepository.findById(dto.getCartId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));

        // security check
        if (!cart.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException(
                    "You cannot order another user's cart"
            );
        }

        // create order
        Order order = OrderMapper.toEntity(dto, user, cart);

        order.setStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        // create order items
        List<OrderItem> orderItems = new ArrayList<>();

        double totalPrice = 0;

        for (CartItem cartItem : cart.getItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(savedOrder);

            orderItem.setProduct(cartItem.getProduct());

            orderItem.setQuantity(cartItem.getQuantity());

            orderItem.setPrice(
                    cartItem.getProduct().getPrice()
            );

            totalPrice +=
                    cartItem.getQuantity()
                            * cartItem.getProduct().getPrice();

            orderItems.add(orderItem);
        }

        orderItemRepository.saveAll(orderItems);

        // update order
        savedOrder.setItems(orderItems);

        savedOrder.setTotalPrice(totalPrice);

        orderRepository.save(savedOrder);

        return OrderMapper.toDTO(savedOrder);
    }

    // =========================
    // GET ALL ORDERS
    // =========================
    @Override
    public List<OrderResponseDTO> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toDTO)
                .toList();
    }

    // =========================
    // GET ORDER BY ID
    // =========================
    @Override
    public OrderResponseDTO getOrderById(
            Long id,
            String email
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        // USER can only access own order
        boolean isOwner =
                order.getUser().getId().equals(user.getId());

        boolean isAdmin =
                user.getRole().name().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException(
                    "You cannot access this order"
            );
        }

        return OrderMapper.toDTO(order);
    }

    // =========================
    // GET MY ORDERS
    // =========================
        @Override
        public List<OrderResponseDTO> getMyOrders(String email) {

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("User not found"));

            List<Order> orders =
                    orderRepository.findByUserId(user.getId());

            return orders.stream()
                    .map(OrderMapper::toDTO)
                    .toList();
        }

    // =========================
    // DELETE ORDER
    // =========================
    @Override
    public void deleteOrder(
            Long id,
            String email
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        boolean isAdmin =
                user.getRole().name().equals("ADMIN");

        if (!isAdmin) {
            throw new AccessDeniedException(
                    "Only admin can delete orders"
            );
        }

        orderRepository.delete(order);
    }
}