package de.hskl.swtp.carpooling.service;

import de.hskl.swtp.carpooling.dto.RequestDTOIn;
import de.hskl.swtp.carpooling.model.Offer;
import de.hskl.swtp.carpooling.model.Position;
import de.hskl.swtp.carpooling.model.Request;
import de.hskl.swtp.carpooling.model.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RequestDBAccess {
    private static final Logger logger = LoggerFactory.getLogger(RequestDBAccess.class);

    private final EntityManager entityManager;

    public RequestDBAccess(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Request createRequest(User user, RequestDTOIn requestDTOIn) {
        Request request = Request.from(requestDTOIn);
        request.setUser(user);
        entityManager.persist(request);
        return request;
    }

    public List<Offer> findOffersWithUsersNearby(Position position, Instant startTime) {
        Instant beginTimeInterval = startTime.minus(1, ChronoUnit.HOURS);
        Instant endTimeInterval = startTime.plus(1, ChronoUnit.HOURS);

        logger.info("Searching offers nearby position (lat: {}, lon: {}) between {} and {}",
                position.getLatitude(), position.getLongitude(), beginTimeInterval, endTimeInterval);

        String sql = "SELECT " +
                "o.offer_id, " +
                "o.start_time, " +
                "o.distance, " +
                "u.user_id AS u_id, " +
                "u.username, " +
                "u.firstname, " +
                "u.lastname, " +
                "u.email " +
                "FROM offer o " +
                "JOIN user u ON o.user_id = u.user_id " +
                "WHERE (ST_Distance_Sphere(u.position, POINT(?, ?)) / 1000) <= o.distance " +
                "AND o.start_time BETWEEN ? AND ?";

        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery(sql)
                .setParameter(1, position.getLongitude())
                .setParameter(2, position.getLatitude())
                .setParameter(3, beginTimeInterval)
                .setParameter(4, endTimeInterval)
                .getResultList();

        logger.debug("Raw result rows count: {}", rows.size());

        List<Offer> offers = new ArrayList<>();
        for (Object[] row : rows) {
            Offer offer = new Offer();
            offer.setOfferId(((Number) row[0]).intValue());

            // Convert SQL Timestamp to Instant
            java.sql.Timestamp ts = (java.sql.Timestamp) row[1];
            Instant instant = ts.toLocalDateTime()
                    .atZone(ZoneId.of("UTC"))
                    .toInstant();
            offer.setStartTime(instant);

            offer.setDistance(((Number) row[2]).intValue());

            User user = new User();
            user.setUserId(((Number) row[3]).intValue());
            user.setUsername((String) row[4]);
            user.setFirstname((String) row[5]);
            user.setLastname((String) row[6]);
            user.setEmail((String) row[7]);
            offer.setUser(user);
            offers.add(offer);

            logger.trace("Parsed offer ID {} with user ID {}", offer.getOfferId(), user.getUserId());
        }

        logger.info("Returning {} offers", offers.size());
        return offers;
    }


    public List<Request> findRequestsByUserId(int userId) {
        return entityManager.createQuery("""
                            SELECT r
                            FROM Request r
                            WHERE r.user.userId = :userId
                            ORDER BY r.startTime
                        """, Request.class)
                .setParameter("userId", userId)
                .getResultList();
    }
    public int deleteExpiredRequests() {
        String sql = "DELETE FROM request WHERE start_time < ?";
        int deletedCount = entityManager.createNativeQuery(sql)
                .setParameter(1, java.sql.Timestamp.from(Instant.now()))
                .executeUpdate();
        logger.info("Deleted {} expired requests", deletedCount);
        return deletedCount;
    }

    public Request getRequestById(int requestId) {
        return entityManager.find(Request.class, requestId);
    }

    public int deleteRequest(int requestId) {
        String sql = "DELETE FROM request WHERE request_id = :requestId";
        return entityManager.createNativeQuery(sql)
                .setParameter("requestId", requestId)
                .executeUpdate();
    }
}
