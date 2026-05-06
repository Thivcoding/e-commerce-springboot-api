package org.hokvanthiv.ecommerce_springboot_api.dto.response;

import lombok.Data;
import org.hokvanthiv.ecommerce_springboot_api.Enum.UserRole;

import java.time.LocalDateTime;

@Data
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private UserRole role;

    private String imageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}