package com.carweb.rest.controller;

import com.carweb.rest.TestUtils;
import com.carweb.rest.dto.CarResponseDTO;
import com.carweb.rest.model.CarEntity;
import com.carweb.rest.repository.CarRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
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
                restTemplate.getForObject("http://localhost:" + port + "/api/cars/" + savedCar.getCarId(),
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

        var createCarResponse = restTemplate.postForEntity(uri, request, CarResponseDTO.class);

        assertThat(createCarResponse.getStatusCode().is2xxSuccessful()).isTrue();

        var newCarURI = URI.create(createCarResponse.getBody().getLink("self").get().getHref());
        var carResult = restTemplate.getForEntity(newCarURI, CarEntity.class).getBody();

        AssertionsForClassTypes.assertThat(carResult).extracting("colour", "year")
                .doesNotContainNull()
                .containsExactly("Red", 2018);
        assertThat(carResult.getMake().getName()).isEqualTo("BMW");
        assertThat(carResult.getModel().getName()).isEqualTo("X1");

        var carEntity = TestUtils.createCarEntity("Ford", "Focus");
        var updateEntity = new HttpEntity<>(carEntity, headers);

        restTemplate.put(newCarURI, updateEntity);

        var updatedResult = restTemplate.getForObject(newCarURI, CarEntity.class);
        assertThat(updatedResult).extracting("colour", "year")
                .doesNotContainNull()
                .containsExactly("Blue", 2017);

        assertThat(updatedResult.getMake().getName()).isEqualTo("Ford");
        assertThat(updatedResult.getModel().getName()).isEqualTo("Focus");
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
