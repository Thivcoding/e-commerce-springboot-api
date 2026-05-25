package org.hokvanthiv.ecommerce_springboot_api.controller;

import jakarta.validation.Valid;
import org.hokvanthiv.ecommerce_springboot_api.dto.common.ApiResponse;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.UserRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.UserResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    // CREATE USER
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(
            @Valid @ModelAttribute UserRequestDTO request){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                userService.createUser(request),
                                "User created successfully"
                        )
                );
    }

    // GET ALL USERS
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUser(){

        return ResponseEntity.ok(
                ApiResponse.success(
                        userService.getUserAll(),
                        "Get users successfully"
                )
        );
    }

    // GET USER BY ID
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(
            @PathVariable Long id){

        return ResponseEntity.ok(
                ApiResponse.success(
                        userService.getUserById(id),
                        "Get user by id successfully"
                )
        );
    }

    // UPDATE USER
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable Long id,
            @Valid @ModelAttribute UserRequestDTO request){

        return ResponseEntity.ok(
                ApiResponse.success(
                        userService.updateUser(id, request),
                        "User updated successfully"
                )
        );
    }

    // DELETE USER
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(
            @PathVariable Long id){

        userService.deleteUser(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "User deleted successfully"
                )
        );
    }
}