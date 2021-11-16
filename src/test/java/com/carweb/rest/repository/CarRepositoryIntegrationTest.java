package com.carweb.rest.repository;

import com.carweb.rest.model.CarEntity;
import com.carweb.rest.model.Make;
import com.carweb.rest.model.Model;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CarRepositoryIntegrationTest {

    @Autowired
    private CarRepository carRepository;

    @Test
    void shouldCreateCarResource() {
        var carEntityIn = createCarEntity("Ford", "Focus");
        CarEntity savedResult = carRepository.save(carEntityIn);

        assertEquals(1, carRepository.count());
        CarEntity carEntityResult = carRepository.findById(savedResult.getId()).get();

        assertEquals(carEntityIn.getMake().getName(), carEntityResult.getMake().getName());
        assertEquals(carEntityIn.getModel().getName(), carEntityResult.getModel().getName());
    }

    private CarEntity createCarEntity(final String carMakeName, final String carModelName) {
        var carEntity = new CarEntity();

        var make = new Make();
        make.setName(carMakeName);
        carEntity.setMake(make);

        var model = new Model();
        model.setName(carModelName);
        carEntity.setModel(model);

        return carEntity;
    }

}
