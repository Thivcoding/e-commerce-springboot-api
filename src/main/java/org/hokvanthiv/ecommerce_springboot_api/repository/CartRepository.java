package org.hokvanthiv.ecommerce_springboot_api.repository;

import org.hokvanthiv.ecommerce_springboot_api.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Long> {

    List<Cart> findByUserId(Long userId);

}
