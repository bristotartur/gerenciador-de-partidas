package com.bristotartur.gerenciadordepartidas.dtos.exposing;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@RequiredArgsConstructor
@Getter
public class ExposingSportEventDto
        extends RepresentationModel<ExposingSportEventDto> implements ExposableEventData<SportEvent> {

    private final Long sportEventId;
    private final Sports type;
    private final Modality modality;
    private final Team firstPlace;
    private final Team secondPlace;
    private final Team thirdPlace;
    private final Team fourthPlace;
    private final Team fifthPlace;
    private final Integer totalMatches;
    private final Status eventStatus;
}
