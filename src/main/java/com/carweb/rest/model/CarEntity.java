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

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "make_id")
    @RestResource(path = "make", rel = "make")
    private Make make;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "model_id")
    @RestResource(path = "model", rel = "model")
    private Model model;

}
