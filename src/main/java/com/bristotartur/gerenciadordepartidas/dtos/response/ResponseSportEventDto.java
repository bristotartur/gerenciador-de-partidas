package com.bristotartur.gerenciadordepartidas.dtos.response;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@RequiredArgsConstructor
@Getter
@Schema(description = "Corpo de resposta para Eventos Esportivos")
public class ResponseSportEventDto
        extends RepresentationModel<ResponseSportEventDto> implements ExposableEventData<SportEvent> {

    @Schema(description = "Identificador único do Evento Esportivo", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private final Long sportEventId;

    @Schema(description = "Tipo de esporte do Evento Esportivo", example = "FUTSAL")
    private final Sports type;

    @Schema(description = "Modalidade do Evento Esportivo", example = "MASCULINE")
    private final Modality modality;

    @Schema(description = "Primeiro colocado do evento", example = "NONE")
    private final Team firstPlace;

    @Schema(description = "Segundo colocado do evento", example = "NONE")
    private final Team secondPlace;

    @Schema(description = "Terceiro colocado do evento", example = "NONE")
    private final Team thirdPlace;

    @Schema(description = "Quarto colocado do evento", example = "NONE")
    private final Team fourthPlace;

    @Schema(description = "Quinto colocado do evento", example = "NONE")
    private final Team fifthPlace;

    @Schema(description = "Total de partidas do evento", example = "12")
    private final Integer totalMatches;

    @Schema(description = "Status do evento", example = "SCHEDULED")
    private final Status eventStatus;

}
