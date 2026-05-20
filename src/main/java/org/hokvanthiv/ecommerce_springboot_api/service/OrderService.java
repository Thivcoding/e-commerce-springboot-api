package org.hokvanthiv.ecommerce_springboot_api.service;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.OrderRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(String email, OrderRequestDTO request);

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO getOrderById(Long id, String email);

    List<OrderResponseDTO> getMyOrders(String email);

    void deleteOrder(Long id, String email);
}