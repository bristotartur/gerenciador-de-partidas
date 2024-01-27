package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.domain.actions.Goal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
public class ExposingGoalDto extends RepresentationModel<Goal> {

    private final String player;
    private final LocalTime goalTime;

}
