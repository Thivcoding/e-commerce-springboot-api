package org.hokvanthiv.ecommerce_springboot_api.repository;

import org.hokvanthiv.ecommerce_springboot_api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);

}
