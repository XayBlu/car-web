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
public class Make {

    @Id
    @GeneratedValue
    private long makeId;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "make")
    private List<CarEntity> car;
}
