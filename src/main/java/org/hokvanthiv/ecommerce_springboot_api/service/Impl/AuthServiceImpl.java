package org.hokvanthiv.ecommerce_springboot_api.service.Impl;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.LoginRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.UserRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.CloudinaryResponse;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.LoginResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.UserResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.User;
import org.hokvanthiv.ecommerce_springboot_api.exception.BadRequestException;
import org.hokvanthiv.ecommerce_springboot_api.exception.DuplicateResourceException;
import org.hokvanthiv.ecommerce_springboot_api.exception.ResourceNotFoundException;
import org.hokvanthiv.ecommerce_springboot_api.mapper.UserMapper;
import org.hokvanthiv.ecommerce_springboot_api.repository.UserRepository;
import org.hokvanthiv.ecommerce_springboot_api.security.JwtUtil;
import org.hokvanthiv.ecommerce_springboot_api.service.AuthService;
import org.hokvanthiv.ecommerce_springboot_api.service.CloudinaryService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private CloudinaryService cloudinaryService;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           CloudinaryService cloudinaryService,
                           JwtUtil jwtUtil
                           ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
        this.jwtUtil = jwtUtil;

    }

    @Override
    public UserResponseDTO register(UserRequestDTO dto){

        // check duplicate email
        if (userRepository.existsByEmail(dto.getEmail().toLowerCase().trim())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User entity =  UserMapper.toEntity(dto,passwordEncoder);

        if (dto.getImage() != null && !dto.getImage().isEmpty()){

            CloudinaryResponse upload = cloudinaryService.uploadFile(dto.getImage());

            entity.setImageUrl(upload.getUrl());
            entity.setPublicId(upload.getPublicId());

        }

        User user = userRepository.save(entity);

        return UserMapper.toDTO(user);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request){

        // normalize email
        String email = request.getEmail().toLowerCase().trim();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        // check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }

        // generate token
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        // return token + user
        return new LoginResponseDTO(token, UserMapper.toDTO(user));
    }
}
