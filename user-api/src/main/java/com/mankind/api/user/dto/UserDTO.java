package com.mankind.api.user.dto;


import com.mankind.api.user.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private Role role;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean active;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}