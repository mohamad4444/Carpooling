package de.hskl.swtp.carpooling.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static final Logger log =
            LoggerFactory.getLogger(ScheduledTasks.class);
    private final RequestDBAccess requestDBAccess;
    private final OfferDBAccess offerDBAccess;

    public ScheduledTasks(RequestDBAccess requestDBAccess, OfferDBAccess offerDBAccess) {
        this.requestDBAccess = requestDBAccess;
        this.offerDBAccess = offerDBAccess;
    }


    @Scheduled(fixedRate = 60_000) // 60 seconds
    public void closeExpiredOffersWithNoBids() {
        log.info("closeExpiredOffersWithNoBids");
        requestDBAccess.deleteExpiredRequests();
        offerDBAccess.deleteExpiredOffers();
    }

}