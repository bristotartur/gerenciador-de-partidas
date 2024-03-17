package com.bristotartur.gerenciadordepartidas.dtos.response;

import com.bristotartur.gerenciadordepartidas.enums.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
public class ResponseGoalDto extends RepresentationModel<ResponseGoalDto> {

    private final Long goalId;
    private final String player;
    private final Team team;
    private final LocalTime goalTime;

}
