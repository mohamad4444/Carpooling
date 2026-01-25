package com.rideshare.app.dto;

import com.rideshare.app.model.Address;

public record UserRegisterDTOIn(
    String username,
    String password,
    String firstname,
    String lastname,
    Address address,
    String email
) {}
