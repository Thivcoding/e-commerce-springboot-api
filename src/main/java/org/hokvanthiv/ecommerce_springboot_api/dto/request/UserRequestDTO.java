package org.hokvanthiv.ecommerce_springboot_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hokvanthiv.ecommerce_springboot_api.Enum.UserRole;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    // ADD ROLE
    private UserRole role;

    private MultipartFile image;
}