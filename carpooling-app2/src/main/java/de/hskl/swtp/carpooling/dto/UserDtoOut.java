package de.hskl.swtp.carpooling.dto;

import de.hskl.swtp.carpooling.model.User;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public record UserDtoOut(int userId, String username)
{
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.GERMAN)
            .withZone(ZoneId.systemDefault());
    public UserDtoOut(User user)
    {
        this( user.getUserId(), user.getUsername() );
    }
}
