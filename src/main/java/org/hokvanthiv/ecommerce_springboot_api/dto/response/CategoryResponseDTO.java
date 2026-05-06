package org.hokvanthiv.ecommerce_springboot_api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String slug;
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
