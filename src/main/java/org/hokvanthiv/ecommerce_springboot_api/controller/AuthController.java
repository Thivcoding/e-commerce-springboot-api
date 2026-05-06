package org.hokvanthiv.ecommerce_springboot_api.controller;

import jakarta.validation.Valid;
import org.hokvanthiv.ecommerce_springboot_api.dto.common.ApiResponse;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.LoginRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.UserRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.LoginResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.UserResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(
            @Valid @ModelAttribute UserRequestDTO request){

        return ResponseEntity.status(HttpStatus.CREATED).
                body(
                    ApiResponse.success(authService.register(request),
                    "User register successfully"
                ));
    }
    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request){

        return ResponseEntity.status(HttpStatus.CREATED).
                body(
                    ApiResponse.success(authService.login(request),
                    "User login successfully"
                ));
    }
}
