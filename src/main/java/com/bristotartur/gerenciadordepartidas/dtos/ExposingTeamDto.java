package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@RequiredArgsConstructor
@Getter
public class ExposingTeamDto extends RepresentationModel<Team> {

    private final String name;
    private final Integer points;

}
