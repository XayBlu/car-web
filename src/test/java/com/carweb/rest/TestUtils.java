package com.carweb.rest;

import com.carweb.rest.model.CarEntity;
import com.carweb.rest.model.Make;
import com.carweb.rest.model.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class TestUtils {
    public static CarEntity createCarEntity(final String carMakeName, final String carModelName)
            throws JsonProcessingException {
        var carEntity = new CarEntity();
        carEntity.setColour("Blue");
        carEntity.setYear(2017);

        var make = new Make();
        make.setName(carMakeName);
        carEntity.setMake(make);

        var model = new Model();
        model.setName(carModelName);
        carEntity.setModel(model);

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(carEntity));

        return carEntity;
    }
}
