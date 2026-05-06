package org.hokvanthiv.ecommerce_springboot_api.repository;

import org.hokvanthiv.ecommerce_springboot_api.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    List<OrderItem> findByOrderId(Long orderId);

    List<OrderItem> findByProductId(Long productId);

    Optional<OrderItem> findByOrderIdAndProductId(Long orderId, Long productId);

}
