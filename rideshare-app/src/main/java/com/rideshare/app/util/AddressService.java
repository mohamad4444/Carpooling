package com.rideshare.app.util;

import com.rideshare.app.model.Position;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final NominatimRESTService nominatimRESTService;

    public AddressService(NominatimRESTService nominatimRESTService) {
        this.nominatimRESTService = nominatimRESTService;
    }

    public Position getGeoData(NominatimQuery geoQuery) {
        return nominatimRESTService.getGeoData(geoQuery);
    }
}