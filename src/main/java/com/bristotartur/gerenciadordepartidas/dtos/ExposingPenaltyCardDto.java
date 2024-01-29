package com.bristotartur.gerenciadordepartidas.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
public class ExposingPenaltyCardDto extends RepresentationModel<ExposingPenaltyCardDto> {

    private final Long penaltyCardId;
    private final String color;
    private final String player;
    private final String team;
    private final LocalTime penaltyCardTime;

}
