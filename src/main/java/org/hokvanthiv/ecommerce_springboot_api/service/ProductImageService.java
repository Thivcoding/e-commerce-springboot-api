package org.hokvanthiv.ecommerce_springboot_api.service;

import org.hokvanthiv.ecommerce_springboot_api.dto.response.ProductImageResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {
    ProductImageResponseDTO addImage(Long productId, MultipartFile file);

    List<ProductImageResponseDTO> getImagesByProduct(Long productId);

    ProductImageResponseDTO replaceImage(Long imageId, MultipartFile file);

    void deleteImage(Long imageId);
}
