package org.hokvanthiv.ecommerce_springboot_api.controller;

import jakarta.validation.Valid;
import org.hokvanthiv.ecommerce_springboot_api.dto.common.ApiResponse;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.ProductRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.ProductResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    // CREATE PRODUCT + UPLOAD IMAGE IN CLOUDINARY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(
            @Valid @ModelAttribute ProductRequestDTO request){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        productService.createProduct(request),
                        "Product create successfully"
                ));
    }

    // GET ALL PRODUCT + IMAGE
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getAllProduct(){

        return ResponseEntity.ok(ApiResponse.success(
                productService.getAllProduct(),
                "Get All Product successfully"
        ));
    }

    // GET PRODUCT BY ID
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProductById(@PathVariable Long id){

        return ResponseEntity.ok(ApiResponse.success(
                productService.getProductById(id),
                "Get Product successfully"
        ));
    }

    // UPDATE PRODUCT + UPLOAD IMAGE IN CLOUDINARY
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> updateProduct(
            @Valid @ModelAttribute ProductRequestDTO request,
            @PathVariable Long id){

        return ResponseEntity.ok(
                ApiResponse.success(
                        productService.updateProduct(id, request),
                        "Product updated successfully"
                )
        );
    }

    // DELETE PRODUCT
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id){

        productService.deleteProduct(id);

        return ResponseEntity.ok(
                ApiResponse.success("Product deleted successfully")
        );
    }

}
