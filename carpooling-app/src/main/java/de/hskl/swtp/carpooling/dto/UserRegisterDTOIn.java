package de.hskl.swtp.carpooling.dto;

import de.hskl.swtp.carpooling.model.Address;

public record UserRegisterDTOIn(
    String username,
    String password,
    String firstname,
    String lastname,
    Address address,
    String email
) {}
