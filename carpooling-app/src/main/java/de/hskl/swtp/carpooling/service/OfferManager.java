package de.hskl.swtp.carpooling.service;

import de.hskl.swtp.carpooling.model.Offer;
import de.hskl.swtp.carpooling.model.Request;
import de.hskl.swtp.carpooling.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class OfferManager {
    EntityManager entityManager;
    public OfferManager(EntityManager entityManager) {
        this.entityManager = entityManager;
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

    public Offer createOffer(int userId, Offer offer) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            offer.setUser(user);
            entityManager.persist(offer);
            return offer;
        }
        throw new IllegalArgumentException("User not found with ID: " + userId);
    }
}
