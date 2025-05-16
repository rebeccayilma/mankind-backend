package com.mankind.mankindmatrixuserservice.dto;

import com.mankind.mankindmatrixuserservice.model.Address;
import lombok.Data;

@Data
public class UpdateAddressDTO {
    private Address.AddressType addressType;
    private Boolean isDefault;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}