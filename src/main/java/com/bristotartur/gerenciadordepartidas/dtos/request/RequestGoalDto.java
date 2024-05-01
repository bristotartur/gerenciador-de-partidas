package com.bristotartur.gerenciadordepartidas.dtos.request;

import com.bristotartur.gerenciadordepartidas.enums.Sports;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalTime;

@Builder
@Schema(description = "DTO de requisição para Gols")
public record RequestGoalDto(

        @Schema(description = "Horário do Gol", example = "14:05:00")
        @NotNull
        LocalTime goalTime,

        @Schema(description = "ID do Jogador", example = "1")
        @NotNull
        Long playerId,

        @Schema(description = "ID da Partida", example = "1")
        @NotNull
        Long matchId,

        @Schema(description = "Tipo de esporte da Partida em que o Gol ocorreu", example = "FUTSAL")
        @NotNull
        Sports sport
) {
}
