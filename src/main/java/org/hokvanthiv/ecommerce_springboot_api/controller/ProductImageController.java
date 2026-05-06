package org.hokvanthiv.ecommerce_springboot_api.controller;

import org.hokvanthiv.ecommerce_springboot_api.dto.common.ApiResponse;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.ProductImageResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.service.ProductImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {

    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    // =========================
    // UPLOAD IMAGE
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductImageResponseDTO>> uploadImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        productImageService.addImage(productId, file),
                        "Image uploaded successfully"
                )
        );
    }

    // =========================
    // GET IMAGES BY PRODUCT
    // =========================
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductImageResponseDTO>>> getImages(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        productImageService.getImagesByProduct(productId),
                        "Get images successfully"
                )
        );
    }

    // =========================
    // REPLACE IMAGE (UPDATE)
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductImageResponseDTO>> updateImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        productImageService.replaceImage(id, file),
                        "Image updated successfully"
                )
        );
    }

    // =========================
    // DELETE IMAGE
    // =========================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{imageId}")
    public ResponseEntity<ApiResponse<String>> deleteImage(@PathVariable Long imageId) {

        productImageService.deleteImage(imageId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Image deleted successfully"
                )
        );
    }
}