package org.hokvanthiv.ecommerce_springboot_api.mapper;

import org.hokvanthiv.ecommerce_springboot_api.Enum.UserRole;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.UserRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.UserResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    // =========================
    // REQUEST → ENTITY (Create)
    // =========================
    public static User toEntity(UserRequestDTO dto, PasswordEncoder passwordEncoder) {
        if (dto == null) return null;

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        // encode password
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // default role if null
        user.setRole(UserRole.USER);

        return user;
    }

    // =========================
    // ENTITY → RESPONSE
    // =========================
    public static UserResponseDTO toDTO(User user) {
        if (user == null) return null;

        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        dto.setImageUrl(user.getImageUrl());

        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }

    // =========================
    // UPDATE ENTITY
    // =========================
    public static void updateEntity(User user, UserRequestDTO dto, PasswordEncoder passwordEncoder) {
        if (dto == null || user == null) return;

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        // update password only if provided
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

    }
}