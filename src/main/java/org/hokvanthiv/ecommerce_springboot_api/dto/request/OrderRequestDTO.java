package org.hokvanthiv.ecommerce_springboot_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderRequestDTO {

    @NotNull(message = "Cart ID is required")
    private Long cartId;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Phone is required")
    @Size(min = 6, max = 20, message = "Phone must be valid")
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address is too long")
    private String addressLine;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;
}