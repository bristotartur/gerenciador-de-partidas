package com.bristotartur.gerenciadordepartidas.dtos.exposing;

import com.bristotartur.gerenciadordepartidas.enums.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
public class ExposingGoalDto extends RepresentationModel<ExposingGoalDto> {

    private final Long goalId;
    private final String player;
    private final Team team;
    private final LocalTime goalTime;

}
