package org.hokvanthiv.ecommerce_springboot_api.repository;

import org.hokvanthiv.ecommerce_springboot_api.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {

    void deleteByProductId(Long productId);

    Optional<ProductImage> findByPublicId(String publicId);

    boolean existsByPublicId(String publicId);

    List<ProductImage> findByProductId(Long productId);

}
