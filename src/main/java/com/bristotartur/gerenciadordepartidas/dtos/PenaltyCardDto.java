package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.enums.PenaltyCardColor;
import jakarta.validation.constraints.NotNull;

public record PenaltyCardDto(@NotNull PenaltyCardColor color,
                             @NotNull Team team) {
}
