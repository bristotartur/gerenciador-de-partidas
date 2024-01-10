package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.enums.MatchStatus;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MatchDto(@NotNull Sports sport,
                       @NotNull Long teamAId,
                       @NotNull Long teamBId,
                       @NotNull Integer teamScoreA,
                       @NotNull Integer teamScoreB,
                       @NotNull Modality modality,
                       @NotNull MatchStatus matchStatus,
                       @NotNull LocalDateTime matchStart,
                       @NotNull LocalDateTime matchEnd) {
}
