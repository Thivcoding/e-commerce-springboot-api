package org.hokvanthiv.ecommerce_springboot_api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private Double price;
    private Integer stock;

    private Long categoryId;
    private String categoryName;

    private List<ProductImageResponseDTO> images;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
