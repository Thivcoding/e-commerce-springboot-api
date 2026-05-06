package org.hokvanthiv.ecommerce_springboot_api.dto.response;


public class LoginResponseDTO {
    private String token;
    private UserResponseDTO user;

    public LoginResponseDTO(String token, UserResponseDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() { return token; }
    public UserResponseDTO getUser() { return user; }
}
