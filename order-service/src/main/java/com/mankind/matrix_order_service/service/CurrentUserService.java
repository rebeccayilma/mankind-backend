package com.mankind.matrix_order_service.service;

import com.mankind.api.user.dto.UserDTO;
import com.mankind.matrix_order_service.client.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrentUserService {
    private final UserClient userClient;

    public UserDTO getCurrentUser() {
        try {
            return userClient.getCurrentUser();
        } catch (Exception e) {
            log.error("Failed to get current user: {}", e.getMessage());
            throw new RuntimeException("Failed to get current user", e);
        }
    }

    public Long getCurrentUserId() {
        UserDTO user = getCurrentUser();
        return user.getId();
    }
}
