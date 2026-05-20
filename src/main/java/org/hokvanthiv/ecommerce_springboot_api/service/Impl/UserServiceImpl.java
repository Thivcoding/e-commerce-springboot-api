package org.hokvanthiv.ecommerce_springboot_api.service.Impl;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.UserRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.CloudinaryResponse;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.UserResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.User;
import org.hokvanthiv.ecommerce_springboot_api.exception.DuplicateResourceException;
import org.hokvanthiv.ecommerce_springboot_api.exception.ResourceNotFoundException;
import org.hokvanthiv.ecommerce_springboot_api.mapper.UserMapper;
import org.hokvanthiv.ecommerce_springboot_api.repository.UserRepository;
import org.hokvanthiv.ecommerce_springboot_api.service.CloudinaryService;
import org.hokvanthiv.ecommerce_springboot_api.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private CloudinaryService cloudinaryService;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           CloudinaryService cloudinaryService){

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;

    }

    // CREATE USER
    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {

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

    // GET ALL
    @Override
    public List<UserResponseDTO> getUserAll() {

        List<User> user = userRepository.findAll();

        return user.stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    // GET BY ID
    @Override
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("User not found"));

        return UserMapper.toDTO(user);
    }

    // UPDATE
    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        return null;
    }

    // DELETE

    @Override
    public void deleteUser() {

    }
}
