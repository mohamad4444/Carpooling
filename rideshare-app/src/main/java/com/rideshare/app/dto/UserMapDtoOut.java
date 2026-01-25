package com.rideshare.app.dto;

import com.rideshare.app.model.User;

public record UserMapDtoOut(int userId, double longitude, double latitude)
{

    public UserMapDtoOut(User user)
    {
        this( user.getUserId(),user.getPosition().getLongitude(),user.getPosition().getLatitude());
    }
}
