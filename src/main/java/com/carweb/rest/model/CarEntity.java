package com.carweb.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@RestResource(path = "cars", rel = "cars")
public class CarEntity {

    @Id
    @GeneratedValue
    private long carId;

    private String colour;
    private int year;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updateDateTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "make_id")
    @RestResource(path = "make", rel = "make")
    private Make make;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "model_id")
    @RestResource(path = "model", rel = "model")
    private Model model;

}
