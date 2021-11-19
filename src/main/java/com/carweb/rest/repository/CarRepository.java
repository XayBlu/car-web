package com.carweb.rest.repository;

import com.carweb.rest.model.CarEntity;
import io.swagger.annotations.Api;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@Api(tags = "Car entity")
@RepositoryRestResource(path = "cars")
public interface CarRepository  extends CrudRepository<CarEntity, Long> {


}
