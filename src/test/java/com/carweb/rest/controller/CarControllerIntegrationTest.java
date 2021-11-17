package com.carweb.rest.controller;

import com.carweb.rest.TestUtils;
import com.carweb.rest.dto.CarResponseDTO;
import com.carweb.rest.model.CarEntity;
import com.carweb.rest.repository.CarRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarRepository carRepository;

    private String createCarPayLoad;
    private String BASE_URL;

    @BeforeEach
    void setUp() throws IOException {
        BASE_URL = "http://localhost:" + port + "/api/cars/";
        createCarPayLoad = Files.readString(Path.of("src/test/resources/stubs/create_car_post_body.json"));
        carRepository.deleteAll();
    }

    @Test
    void shouldRetrieveExistingCarResource() throws JsonProcessingException {
        var savedCar = carRepository.save(TestUtils.createCarEntity("Toyota", "Yaris"));
        Assertions.assertThat(
                restTemplate.getForObject("http://localhost:" + port + "/api/cars/" + savedCar.getId(),
                        String.class)).contains(savedCar.getMake().getName());
    }

    @Test
    void shouldCreateNewCarThenRetrieveResource() throws URISyntaxException {
        var uri = new URI(BASE_URL);

        var headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        var request = new HttpEntity<>(createCarPayLoad, headers);

        ResponseEntity<CarResponseDTO> response = restTemplate.postForEntity(uri, request, CarResponseDTO.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        ResponseEntity<CarEntity> carLookupResult = restTemplate.getForEntity(
                URI.create(response.getBody().getLink("self").get().getHref()), CarEntity.class);

        assertThat(carLookupResult.getStatusCode().is2xxSuccessful()).isTrue();

        assertThat(carLookupResult.getBody().getColour()).isEqualTo("Red");
        assertThat(carLookupResult.getBody().getYear()).isEqualTo(2018);
        assertThat(carLookupResult.getBody().getMake().getName()).isEqualTo("BMW");
        assertThat(carLookupResult.getBody().getModel().getName()).isEqualTo("X1");
    }

    @Test
    void shouldCreateNewCarThenUpdateResource() throws URISyntaxException, JsonProcessingException {
        var uri = new URI(BASE_URL);

        var headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        var request = new HttpEntity<>(createCarPayLoad, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        CarEntity result = carRepository.findById(1l).get();
        assertThat(result.getColour()).isEqualTo("Red");
        assertThat(result.getYear()).isEqualTo(2018);
        assertThat(result.getMake().getName()).isEqualTo("BMW");
        assertThat(result.getModel().getName()).isEqualTo("X1");

        CarEntity carEntity = TestUtils.createCarEntity("Audi", "A3");
        HttpEntity<CarEntity> updateEntity = new HttpEntity<>(carEntity, headers);

        restTemplate.put(URI.create(BASE_URL + 1l), updateEntity);

        CarEntity updatedResult = restTemplate.getForObject(URI.create(BASE_URL + 1l), CarEntity.class);
        assertThat(updatedResult.getColour()).isEqualTo("Blue");
        assertThat(updatedResult.getYear()).isEqualTo(2017);
        assertThat(updatedResult.getMake().getName()).isEqualTo("Audi");
        assertThat(updatedResult.getModel().getName()).isEqualTo("A3");
    }

    @Test
    void shouldCreateNewCarThenDeleteResource() throws URISyntaxException {
        var uri = new URI(BASE_URL);
        var headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        var request = new HttpEntity<>(createCarPayLoad, headers);

        var response = restTemplate.postForEntity(uri, request, CarResponseDTO.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        var resourceSelfLinkURI = URI.create(response.getBody().getLink("self").get().getHref());

        var newCarResource = restTemplate.getForEntity(resourceSelfLinkURI, CarEntity.class);

        assertThat(newCarResource.getBody().getColour()).isEqualTo("Red");
        assertThat(newCarResource.getBody().getYear()).isEqualTo(2018);
        assertThat(newCarResource.getBody().getMake().getName()).isEqualTo("BMW");
        assertThat(newCarResource.getBody().getModel().getName()).isEqualTo("X1");


        restTemplate.delete(resourceSelfLinkURI);

        ResponseEntity<CarEntity> resourceAfterDelete = restTemplate.getForEntity(
                resourceSelfLinkURI, CarEntity.class);

        assertThat(resourceAfterDelete.getStatusCode().is4xxClientError()).isTrue();
    }
}
