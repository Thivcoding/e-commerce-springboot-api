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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;

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

        if (userRepository.existsByEmail(dto.getEmail().toLowerCase().trim())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User entity = UserMapper.toEntity(dto, passwordEncoder);

        // set role
        if (dto.getRole() != null) {
            entity.setRole(dto.getRole());
        }

        // upload image
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {

            CloudinaryResponse upload =
                    cloudinaryService.uploadFile(dto.getImage());

            entity.setImageUrl(upload.getUrl());
            entity.setPublicId(upload.getPublicId());
        }

        User user = userRepository.save(entity);

        return UserMapper.toDTO(user);
    }

    // GET ALL USERS
    @Override
    public List<UserResponseDTO> getUserAll() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    // GET USER BY ID
    @Override
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return UserMapper.toDTO(user);
    }

    // UPDATE USER
    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // check duplicate email
        if (!user.getEmail().equalsIgnoreCase(dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {

            throw new DuplicateResourceException("Email already exists");
        }

        // update fields
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        // UPDATE ROLE
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }

        // update password
        if (dto.getPassword() != null
                && !dto.getPassword().trim().isEmpty()) {

            user.setPassword(
                    passwordEncoder.encode(dto.getPassword())
            );
        }

        // update image
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {

            // delete old image
            if (user.getPublicId() != null) {
                cloudinaryService.deleteFile(user.getPublicId());
            }

            // upload new image
            CloudinaryResponse upload =
                    cloudinaryService.uploadFile(dto.getImage());

            user.setImageUrl(upload.getUrl());
            user.setPublicId(upload.getPublicId());
        }

        User updatedUser = userRepository.save(user);

        return UserMapper.toDTO(updatedUser);
    }

    // DELETE USER
    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // delete image from cloudinary
        if (user.getPublicId() != null) {
            cloudinaryService.deleteFile(user.getPublicId());
        }

        userRepository.delete(user);
    }
}