package de.hskl.swtp.carpooling.util;

import de.hskl.swtp.carpooling.model.Position;
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