package com.mankind.mankindmatrixuserservice.controller;

import com.mankind.mankindmatrixuserservice.dto.UpdateUserDTO;
import com.mankind.mankindmatrixuserservice.dto.UserDTO;
import com.mankind.mankindmatrixuserservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserDTO) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserDTO));
    }
}
