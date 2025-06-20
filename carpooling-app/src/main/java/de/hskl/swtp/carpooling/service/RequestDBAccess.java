package de.hskl.swtp.carpooling.service;

import de.hskl.swtp.carpooling.dto.RequestDTOIn;
import de.hskl.swtp.carpooling.model.Offer;
import de.hskl.swtp.carpooling.model.Request;
import de.hskl.swtp.carpooling.model.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RequestDBAccess {
    private final EntityManager entityManager;

    public RequestDBAccess(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Request createRequest(User user, RequestDTOIn requestDTOIn) {
        Request request=Request.from(requestDTOIn);
        request.setUser(user);
        entityManager.persist(request);
        return request;
    }


}
