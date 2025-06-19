package de.hskl.swtp.carpooling.dto;

import de.hskl.swtp.carpooling.model.User;

public record UserMapDtoOut(int userId, double longitude, double latitude)
{

    public UserMapDtoOut(User user)
    {
        this( user.getUserId(),user.getPosition().getLongitude(),user.getPosition().getLatitude());
    }
}
