package org.hokvanthiv.ecommerce_springboot_api.service;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.UserRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO dto);

    List<UserResponseDTO> getUserAll();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO updateUser(Long id, UserRequestDTO dto);

    void deleteUser(Long id);
}
