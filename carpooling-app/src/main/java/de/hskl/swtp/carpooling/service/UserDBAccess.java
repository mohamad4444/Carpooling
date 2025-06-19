package de.hskl.swtp.carpooling.service;

import de.hskl.swtp.carpooling.dto.UserRegisterDTOIn;
import de.hskl.swtp.carpooling.dto.UserUpdateDTOIn;
import de.hskl.swtp.carpooling.model.Position;
import de.hskl.swtp.carpooling.model.User;
import de.hskl.swtp.carpooling.util.AddressService;
import de.hskl.swtp.carpooling.util.NominatimQuery;
import de.hskl.swtp.carpooling.util.NominatimRESTService;
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

    //Profnp
    public User findUserByNameAndPassword(String username, String password) {
        List<User> users =
                entityManager.createNamedQuery("findUserByName", User.class)
                        .setParameter("username", username)
                        .getResultList();
// Weitere Code ...
        return null;
    }

    //Profnp
    public User findUserById(int id) {
        return entityManager.find(User.class, id);
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
    public User getUserWithOffers(int userId) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u LEFT JOIN FETCH u.offers WHERE u.userId = :userId", User.class);
        query.setParameter("userId", userId);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public User getUserWithRequests(int userId) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u LEFT JOIN FETCH u.requests WHERE u.userId = :userId", User.class);
        query.setParameter("userId", userId);
        return query.getResultList().stream().findFirst().orElse(null);
    }
    //Profnp
    public Collection<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    //TODO maybe not needed
    public User updateUser(UserUpdateDTOIn userDto) {
        User user=User.from(userDto);
        return entityManager.merge(user);
    }
    public boolean updateUser(int id, User updatedData) {
        User existingUser = entityManager.find(User.class, id);
        if (existingUser == null) {
            return false;
        }

        existingUser.setFirstname(updatedData.getFirstname());
        existingUser.setLastname(updatedData.getLastname());
        existingUser.setPosition(updatedData.getPosition());
        existingUser.setAddress(updatedData.getAddress());

        // No need to call persist or merge: the entity is already managed
        return true;
    }
    //TODO maybe not needed
    public User deleteUser(User user) {
        return entityManager.merge(user);
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
