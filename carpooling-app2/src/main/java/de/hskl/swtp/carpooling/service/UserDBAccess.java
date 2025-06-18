package de.hskl.swtp.carpooling.service;

import de.hskl.swtp.carpooling.dto.UserRegisterDTOIn;
import de.hskl.swtp.carpooling.dto.UserUpdateDTOIn;
import de.hskl.swtp.carpooling.model.Position;
import de.hskl.swtp.carpooling.model.User;
import de.hskl.swtp.carpooling.util.PasswordTools;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class UserDBAccess {

    private final EntityManager entityManager;

    public UserDBAccess(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /*---------------   User-related     ------------*/

    //insert user
    //Profnc
    public User createUser(UserRegisterDTOIn userDto) {
        User user = User.from(userDto);

        byte[] salt = PasswordTools.generateSalt();
        byte[] hash = PasswordTools.generatePasswordHash(userDto.password(), salt);
        user.setPasswordSalt(salt);
        user.setPasswordHash(hash);
        entityManager.persist(user);
        return user;
    }

}
