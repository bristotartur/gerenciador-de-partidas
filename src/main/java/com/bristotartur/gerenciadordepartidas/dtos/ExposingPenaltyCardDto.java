package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.domain.actions.PenaltyCard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
public class ExposingPenaltyCardDto extends RepresentationModel<PenaltyCard> {

    private final String player;
    private final String color;
    private final LocalTime penaltyCardTime;

}
