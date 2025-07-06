package com.mankind.mankindmatrixuserservice.service;

import com.mankind.api.user.dto.AuthRequest;
import com.mankind.api.user.dto.AuthResponse;
import com.mankind.api.user.dto.UpdateUserDTO;
import com.mankind.api.user.dto.UserDTO;
import com.mankind.api.user.dto.UserRegistrationDTO;
import com.mankind.mankindmatrixuserservice.exception.UserNotFoundException;
import com.mankind.mankindmatrixuserservice.mapper.UserMapper;
import com.mankind.mankindmatrixuserservice.mapper.UserRegistrationMapper;
import com.mankind.mankindmatrixuserservice.mapper.UserUpdateMapper;
import com.mankind.api.user.enums.Role;
import com.mankind.mankindmatrixuserservice.model.User;
import com.mankind.mankindmatrixuserservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRegistrationMapper userRegistrationMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final KeycloakAdminClientService kcAdmin;
    private final KeycloakTokenService tokenService;
    private final PasswordValidationService passwordValidationService;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, UserRegistrationMapper userRegistrationMapper, UserUpdateMapper userUpdateMapper, KeycloakAdminClientService kcAdmin, KeycloakTokenService tokenService, PasswordValidationService passwordValidationService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userRegistrationMapper = userRegistrationMapper;
        this.userUpdateMapper = userUpdateMapper;
        this.kcAdmin = kcAdmin;
        this.tokenService = tokenService;
        this.passwordValidationService = passwordValidationService;
    }

    @Transactional
    public UserDTO register(UserRegistrationDTO registrationDTO) {
        // Validate password strength
        passwordValidationService.validatePassword(registrationDTO.getPassword());

        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new DataIntegrityViolationException("Username already in use");
        }
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new DataIntegrityViolationException("Email already in use");
        }

        // Create user in Keycloak first
        String keycloakId = kcAdmin.createUser(
                registrationDTO.getUsername(),
                registrationDTO.getEmail(),
                registrationDTO.getPassword(),
                registrationDTO.getFirstName(),
                registrationDTO.getLastName(),
                registrationDTO.getCustomAttributes()
        );

        // Create user in local database (without password)
        User user = userRegistrationMapper.toEntity(registrationDTO);
        user.setKeycloakId(keycloakId);
        user.setRole(Role.USER); // default role
        user.setActive(true);
        
        return userMapper.toDto(userRepository.save(user));
    }

    public AuthResponse authenticate(AuthRequest creds) {
        var tokenResponse = tokenService.getToken(creds.getUsername(), creds.getPassword());
        if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return new AuthResponse(
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken(),
                tokenResponse.getExpiresIn()
        );
    }

    /**
     * Revoke the refresh token at Keycloak (logout)
     */
    public void logout(String refreshToken) {
        tokenService.revokeRefreshToken(refreshToken);
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Update user with UpdateUserDTO (used by API)
     */
    public UpdateUserDTO updateUser(Long id, UpdateUserDTO updateUserDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        // Update fields that are allowed to be updated via API
        if (updateUserDTO.getFirstName() != null) {
            existingUser.setFirstName(updateUserDTO.getFirstName());
        }
        if (updateUserDTO.getLastName() != null) {
            existingUser.setLastName(updateUserDTO.getLastName());
        }
        if (updateUserDTO.getEmail() != null && !updateUserDTO.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(updateUserDTO.getEmail())) {
                throw new DataIntegrityViolationException("Email already in use");
            }
            existingUser.setEmail(updateUserDTO.getEmail());
        }

        existingUser.setProfilePictureUrl(updateUserDTO.getProfilePictureUrl());

        // Save and return updated user
        return userUpdateMapper.toDto(userRepository.save(existingUser));
    }
}
