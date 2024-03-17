package com.bristotartur.gerenciadordepartidas.dtos.response;

import com.bristotartur.gerenciadordepartidas.enums.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@RequiredArgsConstructor
@Getter
public class ResponseParticipantDto extends RepresentationModel<ResponseParticipantDto> {

    private final Long participantId;
    private final String name;
    private final String classNumber;
    private final Team team;

}
