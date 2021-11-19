package com.carweb.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RestResource(path = "make", rel = "make")
public class Make {

    @Id
    @GeneratedValue
    private long makeId;

    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updateDateTime;

}
