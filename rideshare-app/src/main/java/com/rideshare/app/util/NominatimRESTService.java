package com.rideshare.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rideshare.app.model.Position;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NominatimRESTService {
    private final RestClient restClient;

    public NominatimRESTService() {
        this.restClient = RestClient.create();
    }

    public Position getGeoData(NominatimQuery query) {
        String url = "https://nominatim.openstreetmap.org/search?format=json" + "&" + query.getQueryString();
        System.out.println(url);
        String positionsString = restClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Position[] myObjects = mapper.readValue(positionsString, Position[].class);
            return new Position(myObjects[0].getLongitude(), myObjects[0].getLatitude());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}