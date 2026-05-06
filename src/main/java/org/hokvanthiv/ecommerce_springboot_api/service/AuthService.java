package org.hokvanthiv.ecommerce_springboot_api.service;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.LoginRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.UserRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.LoginResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.UserResponseDTO;

public interface AuthService {

    UserResponseDTO register(UserRequestDTO request);

    LoginResponseDTO login (LoginRequestDTO request);

}
