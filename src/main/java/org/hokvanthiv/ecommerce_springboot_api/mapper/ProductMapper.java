package org.hokvanthiv.ecommerce_springboot_api.mapper;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.ProductRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.ProductImageResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.ProductResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.Category;
import org.hokvanthiv.ecommerce_springboot_api.entity.Product;
import org.hokvanthiv.ecommerce_springboot_api.entity.ProductImage;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    // =========================
    // REQUEST DTO -> ENTITY
    // =========================
    public static Product toEntity(ProductRequestDTO dto, Category category) {
        if (dto == null) return null;

        Product product = new Product();
        product.setName(dto.getName());
        product.setSlug(dto.getSlug());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(category);

        return product;
    }

    // =========================
    // ENTITY -> RESPONSE DTO
    // =========================
    public static ProductResponseDTO toDTO(Product product) {
        if (product == null) return null;

        ProductResponseDTO dto = new ProductResponseDTO();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSlug(product.getSlug());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());

        // category
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }

        // images (never null)
        List<ProductImageResponseDTO> images = product.getImages() == null
                ? List.of()
                : product.getImages()
                .stream()
                .map(ProductMapper::mapImageToDTO)
                .collect(Collectors.toList());

        dto.setImages(images);

        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        return dto;
    }

    // =========================
    // UPDATE ENTITY
    // =========================
    public static void updateEntity(Product product, ProductRequestDTO dto, Category category) {
        if (dto == null || product == null) return;

        product.setName(dto.getName());
        product.setSlug(dto.getSlug());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        if (category != null) {
            product.setCategory(category);
        }
    }

    public static ProductImageResponseDTO mapImageToDTO(ProductImage img) {
        ProductImageResponseDTO dto = new ProductImageResponseDTO();
        dto.setId(img.getId());
        dto.setImageUrl(img.getImageUrl());
        dto.setPublicId(img.getPublicId());
        dto.setProductId(img.getProduct().getId());
        dto.setCreatedAt(img.getCreatedAt());
        return dto;
    }

    // LIST ENTITY → LIST DTO
    public static List<ProductImageResponseDTO> imageToDTOList(List<ProductImage> images) {

        if (images == null || images.isEmpty()) {
            return List.of();
        }

        return images.stream()
                .map(ProductMapper::mapImageToDTO)
                .toList();
    }
}