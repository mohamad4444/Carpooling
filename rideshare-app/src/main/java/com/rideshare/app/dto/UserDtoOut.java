package com.rideshare.app.dto;

import com.rideshare.app.model.User;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public record UserDtoOut(int userId, String username,String fullName)
{

    public UserDtoOut(User user)
    {
        this( user.getUserId(), user.getUsername(),user.getFirstname()+" "+user.getLastname() );
    }
}
