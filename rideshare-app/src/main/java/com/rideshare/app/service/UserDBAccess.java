package com.rideshare.app.service;

import com.rideshare.app.dto.UserRegisterDTOIn;
import com.rideshare.app.dto.UserUpdateDTOIn;
import com.rideshare.app.model.Position;
import com.rideshare.app.model.User;
import com.rideshare.app.util.AddressService;
import com.rideshare.app.util.NominatimQuery;
import com.rideshare.app.util.NominatimRESTService;
import com.rideshare.app.util.PasswordTools;
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
    private final AddressService addressService;

    public UserDBAccess(EntityManager entityManager, NominatimRESTService nominatimRESTService, AddressService addressService) {
        this.entityManager = entityManager;
        this.addressService = addressService;
    }

    /*---------------   User-related     ------------*/

    //insert user
    //Profnc
    public User createUser(UserRegisterDTOIn userDto) {
        User user = User.from(userDto);
        // Get coordinates
        NominatimQuery query = NominatimQuery.Builder.create()
                .street(userDto.address().getStreet())
                .streetNr(userDto.address().getStreetNumber())
                .city(userDto.address().getCity())
                .postalcode(userDto.address().getZip())
                .build();
        Position position = addressService.getGeoData(query);
        user.setPosition(position);

        byte[] salt = PasswordTools.generateSalt();
        byte[] hash = PasswordTools.generatePasswordHash(userDto.password(), salt);
        user.setPasswordSalt(salt);
        user.setPasswordHash(hash);
        entityManager.persist(user);
        return user;
    }

    //Find user
    public User loginUser(String username, String rawPassword) {
        // Step 1: Find user by username
        User user = findUserByUsername(username);
        if (user == null) {
            return null; // user not found
        }

        // Step 2: Get stored salt and hash
        byte[] storedSalt = user.getPasswordSalt();
        byte[] storedHash = user.getPasswordHash();

        // Step 3: Hash input password using stored salt
        byte[] calculatedHash = PasswordTools.generatePasswordHash(rawPassword, storedSalt);

        // Step 4: Compare hashes securely
        if (isEqual(storedHash, calculatedHash)) {
            return user; // Authentication successful
        } else {
            return null; // Authentication failed
        }
    }

    private boolean isEqual(byte[] a, byte[] b) {
        if (a == null || b == null || a.length != b.length) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i]; // XOR and OR to prevent early exit
        }
        return result == 0;
    }

    public User findUserByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class
        );
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public List<User> findUsersNearby(Position position, double maxDistanceInKm) {
        String sql = "SELECT * FROM user u " +
                "WHERE (ST_Distance_Sphere(u.position, point(?, ?)) / 1000) <= ?";

        Query nativeQuery = entityManager.createNativeQuery(sql, User.class);
        nativeQuery.setParameter(1, position.getLongitude());
        nativeQuery.setParameter(2, position.getLatitude());
        nativeQuery.setParameter(3, maxDistanceInKm);

        @SuppressWarnings("unchecked")
        List<User> users = nativeQuery.getResultList();
        return users;
    }

    public Collection<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    //Profnp
    public boolean deleteUser(int id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
            return true;
        }
        return false;
    }


}
