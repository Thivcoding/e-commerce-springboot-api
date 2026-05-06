package org.hokvanthiv.ecommerce_springboot_api.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 150, message = "Name must be between 2 and 150 characters")
    private String name;

    @NotBlank(message = "Slug is required")
    @Pattern(
            regexp = "^[a-z0-9-]+$",
            message = "Slug can only contain lowercase letters, numbers, and hyphens"
    )
    private String slug;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @NotNull(message = "Category is required")
    private Long categoryId;


    // optional (for create/update)
    private List<MultipartFile> images;
}