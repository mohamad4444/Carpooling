package de.hskl.swtp.carpooling.dto;

import de.hskl.swtp.carpooling.model.Position;

public record UserRegisterDTOIn(
    String username,
    String password,
    String passwordConfirm,
    String firstname,
    String lastname,
    Position position,
    String streetNumber,
    String street,
    String zip,
    String city,
    String email
) {}
