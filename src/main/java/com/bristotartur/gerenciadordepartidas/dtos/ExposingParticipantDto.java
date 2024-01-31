package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@RequiredArgsConstructor
@Getter
public class ExposingParticipantDto extends RepresentationModel<ExposingParticipantDto> {

    private final Long participantId;
    private final String name;
    private final String classNumber;
    private final TeamName team;
    private final Long editionId;

}
