package com.mankind.mankindmatrixuserservice.dto;

import com.mankind.mankindmatrixuserservice.model.Address;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddressDTO {
    private Long id;
    private Long userId;
    private Address.AddressType addressType;
    private Boolean isDefault;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}