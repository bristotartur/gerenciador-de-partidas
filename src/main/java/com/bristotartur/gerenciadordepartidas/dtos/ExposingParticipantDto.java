package com.bristotartur.gerenciadordepartidas.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@RequiredArgsConstructor
@Getter
public class ExposingParticipantDto extends RepresentationModel<ExposingParticipantDto> {

    private final Long participantId;
    private final String name;
    private final String classNumber;
    private final String team;
    private final Long editionId;

}
