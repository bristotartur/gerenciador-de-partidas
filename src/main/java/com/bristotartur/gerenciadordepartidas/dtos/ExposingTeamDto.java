package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@RequiredArgsConstructor
@Getter
public class ExposingTeamDto extends RepresentationModel<ExposingTeamDto> {

    private final Long teamId;
    private final String teamName;
    private final Integer points;

}
