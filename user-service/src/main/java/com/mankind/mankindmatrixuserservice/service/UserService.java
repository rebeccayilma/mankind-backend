package com.mankind.mankindmatrixuserservice.service;

import com.mankind.mankindmatrixuserservice.dto.AuthRequest;
import com.mankind.mankindmatrixuserservice.dto.AuthResponse;
import com.mankind.mankindmatrixuserservice.dto.UpdateUserDTO;
import com.mankind.mankindmatrixuserservice.dto.UserDTO;
import com.mankind.mankindmatrixuserservice.exception.UserNotFoundException;
import com.mankind.mankindmatrixuserservice.mapper.UserMapper;
import com.mankind.mankindmatrixuserservice.mapper.UserUpdateMapper;
import com.mankind.mankindmatrixuserservice.model.Role;
import com.mankind.mankindmatrixuserservice.model.User;
import com.mankind.mankindmatrixuserservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserUpdateMapper userUpdateMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder, JwtService jwtService, UserUpdateMapper userUpdateMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userUpdateMapper = userUpdateMapper;
    }

    public UserDTO register(UserDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername()) ) {
            throw new DataIntegrityViolationException("Username already in use");
        }
        if (userRepository.existsByEmail(dto.getEmail()) ) {
            throw new DataIntegrityViolationException("Email already in use");
        }
        User user =  userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER); // default role
        user.setActive(true);
        return userMapper.toDto(userRepository.save(user));
    }

    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }

    /**
     * Logs out a user by revoking their token
     * @param token The JWT token to invalidate
     * @return true if the token was successfully revoked, false otherwise
     */
    public boolean logout(String token) {
        try {
            jwtService.revokeToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
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

    public boolean isValidPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    /**
     * Update user with full UserDTO (used internally)
     */
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        // Update fields that are allowed to be updated
        if (userDTO.getFirstName() != null) {
            existingUser.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            existingUser.setLastName(userDTO.getLastName());
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new DataIntegrityViolationException("Email already in use");
            }
            existingUser.setEmail(userDTO.getEmail());
        }
        // Only update password and role if they are explicitly included in the request
        // and not null. For the PUT API endpoint, these fields should not be updated.

        // Save and return updated user
        return userMapper.toDto(userRepository.save(existingUser));
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
