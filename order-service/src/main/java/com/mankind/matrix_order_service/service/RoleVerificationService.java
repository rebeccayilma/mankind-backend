package com.mankind.matrix_order_service.service;

import com.mankind.api.user.dto.UserDTO;
import com.mankind.api.user.enums.Role;
import com.mankind.matrix_order_service.client.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import feign.FeignException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleVerificationService {
    private final UserClient userClient;
    
    public void verifyAdminOrSuperAdminRole() {
        try {
            UserDTO currentUser = userClient.getCurrentUser();
            if (currentUser == null) {
                log.error("Current user is null - admin verification failed");
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required - user not found");
            }
            if (currentUser.getRole() == null) {
                log.error("User {} has null role - admin verification failed", currentUser.getUsername());
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required - user role is null");
            }
            boolean hasAdminRole = (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.SUPER_ADMIN);
            if (!hasAdminRole) {
                log.warn("User {} does not have ADMIN or SUPER_ADMIN role. Current role: {}", 
                        currentUser.getUsername(), currentUser.getRole());
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, 
                    String.format("Admin access required. Current role: %s", currentUser.getRole()));
            }
            log.debug("User {} has admin role: {}", currentUser.getUsername(), currentUser.getRole());
        } catch (FeignException e) {
            log.error("Feign communication error during admin role verification: {} - Status: {}", e.getMessage(), e.status());
            
            if (e.status() == 401) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed - please provide a valid JWT token");
            } else if (e.status() == 403) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied - insufficient permissions");
            } else if (e.status() == 404) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User service endpoint not found");
            } else if (e.status() >= 500) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, 
                    "User service is currently unavailable. Please try again later.");
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Communication error with user service: " + e.getMessage());
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during admin role verification: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Unexpected error during role verification: " + e.getMessage());
        }
    }
    
    public void verifyAuthenticatedUser() {
        try {
            UserDTO currentUser = userClient.getCurrentUser();
            if (currentUser == null) {
                log.error("Current user is null - authentication verification failed");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
            }
            log.debug("User {} is authenticated", currentUser.getUsername());
        } catch (FeignException e) {
            log.error("Feign communication error during user authentication: {} - Status: {}", e.getMessage(), e.status());
            
            if (e.status() == 401) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed - please provide a valid JWT token");
            } else if (e.status() >= 500) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, 
                    "User service is currently unavailable. Please try again later.");
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Communication error with user service: " + e.getMessage());
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during user authentication: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Unexpected error during authentication: " + e.getMessage());
        }
    }
}
