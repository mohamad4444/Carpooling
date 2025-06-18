package de.hskl.swtp.carpooling.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DBBackgroundTasks
{
private final UserDBAccess dbAccess;

    @Autowired
    public DBBackgroundTasks(UserDBAccess dbAccess) {

    }

    public void closeExpiredOffersWithNoBids() {
    }

    public void changeStateOfExpiredOffersWithBidsAndCreateSale() {
    }
}