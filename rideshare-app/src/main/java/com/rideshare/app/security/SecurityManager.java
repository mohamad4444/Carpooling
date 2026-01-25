package com.rideshare.app.security;

import com.rideshare.app.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SecurityManager
{
    private final Map<String, User> accessList = new ConcurrentHashMap<>();

    public String createUserToken(User user)
    {
        String accessToken = UUID.randomUUID().toString();
        accessList.put(accessToken, user);
        return accessToken;
    }

    public boolean removeToken(String accessToken)
    {
        return accessList.remove(accessToken) != null;
    }

    public boolean isValid(String accessToken)
    {
        return accessList.containsKey(accessToken);
    }

    public User getUser(String accessToken)
    {
        return accessList.get(accessToken);
    }

    public void checkIfTokenIsAccepted(String token)
    {
        if( !this.isValid(token) )
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    public void checkIfTokenIsFromUser(String token, int userId)
    {
        if( userId != this.getUser(token).getUserId() )
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
