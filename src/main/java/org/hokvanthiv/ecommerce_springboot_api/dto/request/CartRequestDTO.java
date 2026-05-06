package org.hokvanthiv.ecommerce_springboot_api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;
}