package com.bristotartur.gerenciadordepartidas.dtos.response;

import com.bristotartur.gerenciadordepartidas.enums.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
@Schema(description = "Corpo de resposta para Gols")
public class ResponseGoalDto extends RepresentationModel<ResponseGoalDto> {

    @Schema(description = "ID do Gol", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private final Long goalId;

    @Schema(description = "Nome do Jogador", example = "Eduardo")
    private final String player;

    @Schema(description = "Equipe do Jogador", example = "PAPA_LEGUAS")
    private final Team team;

    @Schema(description = "Horário do Gol", example = "14:05:00")
    private final LocalTime goalTime;

}
