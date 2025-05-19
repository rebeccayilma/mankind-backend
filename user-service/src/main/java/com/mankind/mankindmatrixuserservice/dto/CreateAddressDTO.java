package com.mankind.mankindmatrixuserservice.dto;

import com.mankind.mankindmatrixuserservice.model.Address;
import lombok.Data;

@Data
public class CreateAddressDTO {
    private Address.AddressType addressType;
    private Boolean isDefault = false;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}