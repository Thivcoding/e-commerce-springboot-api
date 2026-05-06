package org.hokvanthiv.ecommerce_springboot_api.repository;

import org.hokvanthiv.ecommerce_springboot_api.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    List<CartItem> findByCartId(Long cartId);

    List<CartItem> findByProductId(Long productId);

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

}
