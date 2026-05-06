package org.hokvanthiv.ecommerce_springboot_api.service.Impl;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.CategoryRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.CategoryResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.Category;
import org.hokvanthiv.ecommerce_springboot_api.exception.ResourceNotFoundException;
import org.hokvanthiv.ecommerce_springboot_api.mapper.CategoryMapper;
import org.hokvanthiv.ecommerce_springboot_api.repository.CategoryRepository;
import org.hokvanthiv.ecommerce_springboot_api.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRespository;

    public CategoryServiceImpl (CategoryRepository categoryRespository){
        this.categoryRespository = categoryRespository;
    }
    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {

        Category model = CategoryMapper.toEntity(request);

        Category category = categoryRespository.save(model);

        return CategoryMapper.toDTO(category);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategory(){
        return categoryRespository.findAll().stream()
//                .map(category -> CategoryMapper.toDTO(category)).toList();
                .map(CategoryMapper::toDTO).toList();
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id){

        Category category = categoryRespository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Category not found with id: "+id));

        return CategoryMapper.toDTO(category);
    }

    @Override
    public CategoryResponseDTO updateCategory(CategoryRequestDTO request, Long id){

        Category category = categoryRespository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Category not found with id: "+id));

        CategoryMapper.updateEntity(category,request);

        Category updated = categoryRespository.save(category);

        return CategoryMapper.toDTO(updated);
    }

    @Override
    public void deleteCategory(Long id){

        Category category = categoryRespository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Category not found with id: "+id));

        categoryRespository.delete(category);
    }
}
