package de.hskl.swtp.carpooling.service;

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
public class RequestManager {
    EntityManager entityManager;
    public RequestManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    // ========== REQUEST-RELATED METHODS ==========

    public List<Request> getRequestsByUserId(int userId) {
        TypedQuery<Request> query = entityManager.createQuery(
                "SELECT r FROM Request r WHERE r.user.userId = :userId", Request.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<Request> getActiveRequestsByUserId(int userId) {
        TypedQuery<Request> query = entityManager.createQuery(
                "SELECT r FROM Request r WHERE r.user.userId = :userId AND r.startTime > CURRENT_TIMESTAMP",
                Request.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public Optional<Request> getRequestById(int requestId) {
        return Optional.ofNullable(entityManager.find(Request.class, requestId));
    }

    public Request createRequest(int userId, Request request) {
        User user = entityManager.find(User.class, userId);
        if (user != null) {
            request.setUser(user);
            entityManager.persist(request);
            return request;
        }
        throw new IllegalArgumentException("User not found with ID: " + userId);
    }
}
