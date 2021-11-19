package com.carweb.rest.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class DataMuseClient {

    public static final String BASE_URL = "https://api.datamuse.com/words";
    private final RestTemplate restTemplate;

    public DataMuseClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DataMuseResponse lookUpSimilarWords(final String word) {
        return restTemplate.getForEntity(BASE_URL, DataMuseResponse.class, Map.of("ml", word)).getBody();
    }
}
