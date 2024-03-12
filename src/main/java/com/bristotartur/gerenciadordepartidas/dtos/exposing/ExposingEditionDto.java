package com.bristotartur.gerenciadordepartidas.dtos.exposing;

import com.bristotartur.gerenciadordepartidas.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
@Schema(description = "DTO para representar uma Edição")
public class ExposingEditionDto extends RepresentationModel<ExposingEditionDto> {

    @Schema(description = "Identificador único da Edição", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private final Long editionId;

    @Schema(description = "Pontuação da equipe Atômica", example = "0")
    private final Integer atomica;

    @Schema(description = "Pontuação da equipe Mestres", example = "0")
    private final Integer mestres;

    @Schema(description = "Pontuação da equipe Papa-Léguas", example = "0")
    private final Integer papaLeguas;

    @Schema(description = "Pontuação da equipe Twister", example = "0")
    private final Integer twister;

    @Schema(description = "Pontuação da equipe Unicontti", example = "0")
    private final Integer unicontti;

    @Schema(description = "Status da Edição", example = "SCHEDULED")
    private final Status editionStatus;

    @Schema(description = "Data de abertura", example = "2024-04-21")
    private final LocalDate opening;

    @Schema(description = "Data de encerramento", example = "2024-05-04")
    private final LocalDate closure;

}
