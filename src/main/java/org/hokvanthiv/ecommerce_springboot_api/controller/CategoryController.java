package org.hokvanthiv.ecommerce_springboot_api.controller;

import jakarta.validation.Valid;
import org.hokvanthiv.ecommerce_springboot_api.dto.common.ApiResponse;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.CategoryRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.CategoryResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    // CREATE CATEGORY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> createCategory(
           @Valid @RequestBody CategoryRequestDTO request){

        CategoryResponseDTO response = categoryService.createCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED).
                body(ApiResponse.success(response,"Category create successfully"));
    }

    // GET ALL CATEGORY
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getAllCategory(){

        return ResponseEntity.ok(
                ApiResponse.success(
                        categoryService.getAllCategory(),
                        "Get Category successfully"
                )
        );
    }

    // GET BY ID CATEGORY
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> getCategoryById(@PathVariable Long id){

        return ResponseEntity.ok(
                ApiResponse.success(
                        categoryService.getCategoryById(id),
                        "Get Category successfully"
                )
        );
    }

    // UPDATE CATEGORY
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequestDTO request
    ){

        return  ResponseEntity.ok(
                ApiResponse.success(
                        categoryService.updateCategory(request, id),
                        "Category update successfully"
                )
        );
    }

    // DELETE CATEGORY
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id){

        categoryService.deleteCategory(id);

        return ResponseEntity.ok(
                ApiResponse.success("Category deleted successfully")
        );
    }
}
