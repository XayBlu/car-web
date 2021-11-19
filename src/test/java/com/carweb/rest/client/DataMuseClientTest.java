package com.carweb.rest.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class DataMuseClientTest {


    @Mock
    private RestTemplate restTemplate;

    private DataMuseClient dataMuseClient;

    @BeforeEach
    void setUp() {
        dataMuseClient = new DataMuseClient(restTemplate);
    }

    @Test
    void shouldMakeCorrectCallToDataMuse() {
        var museResponse = new DataMuseResponse(List.of(new DataMuseResponse.Word("test")));
        when(restTemplate.getForEntity( DataMuseClient.BASE_URL, DataMuseResponse.class, Map.of("ml", "focus")))
                .thenReturn(ResponseEntity.ok(museResponse));

        dataMuseClient.lookUpSimilarWords("focus");

        verify(restTemplate).getForEntity( DataMuseClient.BASE_URL, DataMuseResponse.class, Map.of("ml", "focus"));
    }
}