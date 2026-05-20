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

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    // CREATE USER
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(
            @Valid @ModelAttribute UserRequestDTO request){

        return ResponseEntity.status(HttpStatus.CREATED).
                body(
                        ApiResponse.success(userService.createUser(request),
                                "User create successfully"
                        ));
    }

    // GET USER ALL
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUser(){

        return ResponseEntity.ok(
                ApiResponse.success(userService.getUserAll()
                , "Get Users Successfully")
        );
    }

    // GET USER ALL
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(
            @PathVariable Long id
    ){

        return ResponseEntity.ok(
                ApiResponse.success(userService.getUserById(id)
                        , "Get Users by id Successfully")
        );
    }

}
