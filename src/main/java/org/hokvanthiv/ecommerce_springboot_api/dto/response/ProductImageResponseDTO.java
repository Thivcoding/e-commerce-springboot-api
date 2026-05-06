package org.hokvanthiv.ecommerce_springboot_api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductImageResponseDTO {

    private Long id;
    private String imageUrl;
    private String publicId;
    private Long productId;
    private LocalDateTime createdAt;
}
