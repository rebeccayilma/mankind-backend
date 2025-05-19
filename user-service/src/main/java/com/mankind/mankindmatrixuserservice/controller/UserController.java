package com.mankind.mankindmatrixuserservice.controller;

import com.mankind.mankindmatrixuserservice.dto.AddressDTO;
import com.mankind.mankindmatrixuserservice.dto.CreateAddressDTO;
import com.mankind.mankindmatrixuserservice.dto.UpdateAddressDTO;
import com.mankind.mankindmatrixuserservice.dto.UpdateUserDTO;
import com.mankind.mankindmatrixuserservice.dto.UserDTO;
import com.mankind.mankindmatrixuserservice.exception.AddressNotFoundException;
import com.mankind.mankindmatrixuserservice.service.AddressService;
import com.mankind.mankindmatrixuserservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "API endpoints for managing users and their addresses")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;

    @Autowired
    public UserController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @Operation(summary = "Get a user by ID", description = "Returns a user based on the ID provided")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(
            @Parameter(description = "ID of the user to retrieve") @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Update a user", description = "Updates a user's information based on the ID provided")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UpdateUserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "409", description = "Conflict - Email already in use")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserDTO> updateUser(
            @Parameter(description = "ID of the user to update") @PathVariable Long id,
            @Parameter(description = "Updated user information") @RequestBody UpdateUserDTO updateUserDTO) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserDTO));
    }

    // Address endpoints
    @Operation(summary = "Get all addresses for a user", description = "Returns a list of all addresses associated with the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses(
            @Parameter(description = "ID of the user whose addresses to retrieve") @PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getAddressesByUserId(userId));
    }

    @Operation(summary = "Get a specific address", description = "Returns a specific address for the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address found",
                    content = @Content(schema = @Schema(implementation = AddressDTO.class))),
            @ApiResponse(responseCode = "404", description = "Address not found or doesn't belong to the user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getUserAddress(
            @Parameter(description = "ID of the user") @PathVariable Long userId,
            @Parameter(description = "ID of the address to retrieve") @PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        // Verify that the address belongs to the specified user
        if (!addressDTO.getUserId().equals(userId)) {
            throw new AddressNotFoundException("Address with ID " + addressId + " not found for user with ID " + userId);
        }
        return ResponseEntity.ok(addressDTO);
    }

    @Operation(summary = "Create a new address", description = "Creates a new address for the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address created successfully",
                    content = @Content(schema = @Schema(implementation = AddressDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/{userId}/addresses")
    public ResponseEntity<AddressDTO> createAddress(
            @Parameter(description = "ID of the user") @PathVariable Long userId,
            @Parameter(description = "Address details") @RequestBody CreateAddressDTO createAddressDTO) {
        AddressDTO createdAddress = addressService.createAddress(userId, createAddressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }

    @Operation(summary = "Update an address", description = "Updates an existing address for the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address updated successfully",
                    content = @Content(schema = @Schema(implementation = AddressDTO.class))),
            @ApiResponse(responseCode = "404", description = "Address not found or doesn't belong to the user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(
            @Parameter(description = "ID of the user") @PathVariable Long userId,
            @Parameter(description = "ID of the address to update") @PathVariable Long addressId,
            @Parameter(description = "Updated address details") @RequestBody UpdateAddressDTO updateAddressDTO) {
        // First check if the address exists and belongs to the user
        AddressDTO existingAddress = addressService.getAddressById(addressId);
        if (!existingAddress.getUserId().equals(userId)) {
            throw new AddressNotFoundException("Address with ID " + addressId + " not found for user with ID " + userId);
        }

        AddressDTO updatedAddress = addressService.updateAddress(addressId, updateAddressDTO);
        return ResponseEntity.ok(updatedAddress);
    }

    @Operation(summary = "Delete an address", description = "Deletes an address for the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found or doesn't belong to the user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @Parameter(description = "ID of the user") @PathVariable Long userId,
            @Parameter(description = "ID of the address to delete") @PathVariable Long addressId) {
        // First check if the address exists and belongs to the user
        AddressDTO existingAddress = addressService.getAddressById(addressId);
        if (!existingAddress.getUserId().equals(userId)) {
            throw new AddressNotFoundException("Address with ID " + addressId + " not found for user with ID " + userId);
        }

        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}
