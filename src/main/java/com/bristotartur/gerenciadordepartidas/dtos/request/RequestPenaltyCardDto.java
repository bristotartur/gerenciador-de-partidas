package com.bristotartur.gerenciadordepartidas.dtos.request;

import com.bristotartur.gerenciadordepartidas.enums.PenaltyCardColor;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record RequestPenaltyCardDto(@NotNull PenaltyCardColor color,
                                    @NotNull LocalTime penaltyCardTime,
                                    @NotNull Long playerId,
                                    @NotNull Long matchId,
                                    @NotNull Sports sport) {
}
