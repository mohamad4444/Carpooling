package com.rideshare.app.dto;

import com.rideshare.app.model.Position;

public record UserUpdateDTOIn(
    int userId,
    String username,
    String firstname,
    String lastname,
    Position position,
    String streetNumber,
    String street,
    String zip,
    String city,
    String email
) {}
