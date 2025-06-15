package de.hskl.swtp.carpooling.service;

import de.hskl.swtp.carpooling.dto.UserRegisterDTOIn;
import de.hskl.swtp.carpooling.model.Offer;
import de.hskl.swtp.carpooling.model.Position;
import de.hskl.swtp.carpooling.model.Request;
import de.hskl.swtp.carpooling.model.User;
import de.hskl.swtp.carpooling.util.PasswordTools;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserManager {

    private final EntityManager entityManager;

    public UserManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User createUser(UserRegisterDTOIn userDto) {
        byte[] salt = PasswordTools.generateSalt();
        byte[] hash = PasswordTools.generatePasswordHash(userDto.password(), salt);
        User user = User.from(userDto);
        user.setPasswordSalt(salt);
        user.setPasswordHash(hash);
        entityManager.persist(user);
        return user;
    }
//    public void deleteUser(int userId) {
//        User user = entityManager.find(User.class,userId);
//        entityManager.getTransaction().begin();
//        entityManager.remove(user);
//        entityManager.flush();
//        entityManager.getTransaction().commit();
//    }
    public User findUserById(int id) {
        return entityManager.find(User.class, id);
    }
    public Collection<User> findAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    public User loginUser(String username, String rawPassword) {
        byte[] salt = PasswordTools.generateSalt();
        byte[] hash = PasswordTools.generatePasswordHash(rawPassword, salt);

//        User user = new User(username, hash, salt);
//        entityManager.persist(user);
        return null;
    }


    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    public Optional<User> getUserByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class
        );
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }

    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }

    public boolean updateUser(int id, User updatedData) {
        User existingUser = entityManager.find(User.class, id);
        if (existingUser == null) {
            return false;
        }

        existingUser.setFirstname(updatedData.getFirstname());
        existingUser.setLastname(updatedData.getLastname());
        existingUser.setPosition(updatedData.getPosition());
        existingUser.setStreet(updatedData.getStreet());
        existingUser.setStreetNumber(updatedData.getStreetNumber());
        existingUser.setZip(updatedData.getZip());
        existingUser.setCity(updatedData.getCity());

        // No need to call persist or merge: the entity is already managed
        return true;
    }

    public boolean deleteUser(int id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
            return true;
        }
        return false;
    }

    public boolean existsByUsername(String username) {
        return getUserByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class
        );
        query.setParameter("email", email);
        return !query.getResultList().isEmpty();
    }

    public List<Integer> findOfferIdsNearby(Position position, Instant startTime) {
        Instant beginTimeInterval = startTime.minus(1, ChronoUnit.HOURS);
        Instant endTimeInterval = startTime.plus(1, ChronoUnit.HOURS);

        String sqlQueryString = "SELECT o.offer_id " +
                "FROM user u JOIN offer o ON u.user_id = o.user_id " +
                "WHERE (ST_Distance_Sphere(u.position, point(?, ?))/1000) <= o.distance " +
                "AND o.start_time BETWEEN ? AND ? ";

        Query sqlQuery = entityManager.createNativeQuery(sqlQueryString);
        sqlQuery.setParameter(1, position.getLongitude());
        sqlQuery.setParameter(2, position.getLatitude());
        sqlQuery.setParameter(3, beginTimeInterval);
        sqlQuery.setParameter(4, endTimeInterval);

        @SuppressWarnings("unchecked")
        List<Integer> results = sqlQuery.getResultList();

        return results;
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

    // ========== COMBINED USER DATA METHODS ==========

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

    // ========== MATCHING METHODS ==========

    public List<Offer> findMatchingOffersForRequest(int requestId) {
        Request request = entityManager.find(Request.class, requestId);
        if (request == null) return List.of();

        Position userPosition = request.getUser().getPosition();
        Instant startTime = request.getStartTime();
        Instant beginTimeInterval = startTime.minus(1, ChronoUnit.HOURS);
        Instant endTimeInterval = startTime.plus(1, ChronoUnit.HOURS);

        String jpql = "SELECT o FROM Offer o JOIN o.user u " +
                "WHERE (ST_Distance_Sphere(u.position, :position)/1000) <= o.distance " +
                "AND o.startTime BETWEEN :start AND :end";

        TypedQuery<Offer> query = entityManager.createQuery(jpql, Offer.class);
        query.setParameter("position", userPosition);
        query.setParameter("start", beginTimeInterval);
        query.setParameter("end", endTimeInterval);

        return query.getResultList();
    }


}
