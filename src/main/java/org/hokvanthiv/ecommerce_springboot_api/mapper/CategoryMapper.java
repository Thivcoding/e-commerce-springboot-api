package org.hokvanthiv.ecommerce_springboot_api.mapper;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.CategoryRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.CategoryResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.Category;

public class CategoryMapper {

    // Convert RequestDTO -> Entity (for create/update)
    public static Category toEntity(CategoryRequestDTO dto) {
        if (dto == null) return null;

        Category category = new Category();
        category.setName(dto.getName());
        category.setSlug(dto.getSlug());
        category.setDescription(dto.getDescription());

        return category;
    }

    // Convert Entity -> ResponseDTO
    public static CategoryResponseDTO toDTO(Category category) {
        if (category == null) return null;

        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setDescription(category.getDescription());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());

        return dto;
    }

    // Optional: update existing entity (for PUT/PATCH)
    public static void updateEntity(Category category, CategoryRequestDTO dto) {
        if (dto == null || category == null) return;

        category.setName(dto.getName());
        category.setSlug(dto.getSlug());
        category.setDescription(dto.getDescription());
    }
}