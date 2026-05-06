package org.hokvanthiv.ecommerce_springboot_api.service;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.CategoryRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO createCategory(CategoryRequestDTO request);

    List<CategoryResponseDTO> getAllCategory();

    CategoryResponseDTO getCategoryById(Long id);

    CategoryResponseDTO updateCategory(CategoryRequestDTO request,Long id);

    void deleteCategory(Long id);
}
