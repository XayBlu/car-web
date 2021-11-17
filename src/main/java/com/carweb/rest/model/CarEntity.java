package com.carweb.rest.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;

@Entity(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@RestResource(path = "cars", rel = "cars")
public class CarEntity {

    @Id
    @GeneratedValue
    private long id;

    private String colour;
    private int year;

    @OneToOne(cascade = CascadeType.ALL)
    @RestResource(path = "make", rel="make")
    @JoinColumn(name = "make_id")
    private Make make;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "model_id")
    @RestResource(path = "model", rel="model")
    private Model model;


}
