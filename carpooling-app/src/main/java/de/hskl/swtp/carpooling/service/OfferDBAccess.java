package de.hskl.swtp.carpooling.service;

import de.hskl.swtp.carpooling.dto.OfferDtoIn;
import de.hskl.swtp.carpooling.model.Offer;
import de.hskl.swtp.carpooling.model.Position;
import de.hskl.swtp.carpooling.model.Request;
import de.hskl.swtp.carpooling.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class OfferDBAccess {
    private final EntityManager entityManager;
    public OfferDBAccess(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /*---------------   Offer-related     ------------*/
    public Offer createOffer(User user, OfferDtoIn offerDtoIn) {
        System.out.println("Creating offer...");
        System.out.println(offerDtoIn.toString());
        if (user != null) {
            Offer offer=Offer.from(offerDtoIn);
            offer.setUser(user);
            entityManager.persist(offer);
            return offer;
        }
        throw new IllegalArgumentException("User not found with ID: " + user.getUserId());
    }




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

    public List<Offer> getOffersByUserId(int userId) {
        TypedQuery<Offer> query = entityManager.createQuery(
                "SELECT o FROM Offer o WHERE o.user.userId = :userId", Offer.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<Offer> getActiveOffersByUserId(int userId) {
        TypedQuery<Offer> query = entityManager.createQuery(
                "SELECT o FROM Offer o WHERE o.user.userId = :userId AND o.startTime > CURRENT_TIMESTAMP",
                Offer.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public Optional<Offer> getOfferById(int offerId) {
        return Optional.ofNullable(entityManager.find(Offer.class, offerId));
    }

    public Collection<Offer> getPossibleOffersForUser(int userId) {
        return null;
    }
    public int deleteExpiredOffers() {
        String sql = "DELETE FROM offer WHERE start_time < ?";
        int deletedCount = entityManager.createNativeQuery(sql)
                .setParameter(1, java.sql.Timestamp.from(Instant.now()))
                .executeUpdate();
        return deletedCount;
    }
}
