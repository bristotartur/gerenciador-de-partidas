package com.bristotartur.gerenciadordepartidas.dtos.request;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "DTO de requisição de Eventos Esportivos")
public record RequestSportEventDto(

        @Schema(description = "Tipo de esporte do Evento Esportivo", example = "FUTSAL")
        @NotNull
        Sports type,

        @Schema(description = "Modalidade do Evento Esportivo", example = "MASCULINE")
        @NotNull
        Modality modality,

        @Schema(description = "Total de partidas do Evento Esportivo", example = "14")
        @NotNull
        Integer totalMatches,

        @Schema(description = "Identificador único da Edição do Evento Esportivo", example = "1")
        @NotNull
        Long editionId

) implements TransferableEventData<SportEvent> {
}
