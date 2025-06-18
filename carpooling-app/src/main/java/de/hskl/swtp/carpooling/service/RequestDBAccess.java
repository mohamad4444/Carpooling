package de.hskl.swtp.carpooling.service;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class RequestDBAccess {
    private final EntityManager entityManager;

    public RequestDBAccess(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
