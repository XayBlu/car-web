package com.carweb.rest.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Model {

    @Id
    @GeneratedValue
    private long modelId;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "model")
    private List<CarEntity> cars;
}
