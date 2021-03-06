package com.carweb.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
public class CarResponseDTO extends RepresentationModel<CarResponseDTO> {
    private String content;
}
