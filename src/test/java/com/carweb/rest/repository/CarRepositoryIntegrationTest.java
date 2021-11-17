package com.carweb.rest.repository;

import com.carweb.rest.TestUtils;
import com.carweb.rest.model.CarEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CarRepositoryIntegrationTest {

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    void setU() {
        carRepository.deleteAll();
    }

    @Test
    void shouldCreateCarResource() throws JsonProcessingException {
        var carEntityIn = TestUtils.createCarEntity("Ford", "Focus");
        CarEntity savedResult = carRepository.save(carEntityIn);

        assertEquals(1, carRepository.count());
        CarEntity carEntityResult = carRepository.findById(savedResult.getId()).get();

        assertEquals(carEntityIn.getMake().getName(), carEntityResult.getMake().getName());
        assertEquals(carEntityIn.getModel().getName(), carEntityResult.getModel().getName());
    }

}
