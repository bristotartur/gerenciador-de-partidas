package com.bristotartur.gerenciadordepartidas.dtos.input;

import com.bristotartur.gerenciadordepartidas.enums.PenaltyCardColor;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record PenaltyCardDto(@NotNull PenaltyCardColor color,
                             @NotNull LocalTime penaltyCardTime,
                             @NotNull Long playerId,
                             @NotNull Long matchId,
                             @NotNull Sports sport) {
}
