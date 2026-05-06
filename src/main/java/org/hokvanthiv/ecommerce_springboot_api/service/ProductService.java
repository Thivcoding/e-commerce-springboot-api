package org.hokvanthiv.ecommerce_springboot_api.service;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.ProductRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.ProductResponseDTO;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public interface ProductService {

    ProductResponseDTO createProduct(ProductRequestDTO request);

    List<ProductResponseDTO> getAllProduct();

    ProductResponseDTO getProductById(Long id);

    ProductResponseDTO updateProduct(Long id,ProductRequestDTO request);

    void deleteProduct(Long id);

}
