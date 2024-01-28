package com.bristotartur.gerenciadordepartidas.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
public class ExposingPenaltyCardDto extends RepresentationModel<ExposingPenaltyCardDto> {

    private final String player;
    private final String color;
    private final LocalTime penaltyCardTime;

}
