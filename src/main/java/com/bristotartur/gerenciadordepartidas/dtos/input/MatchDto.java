package com.bristotartur.gerenciadordepartidas.dtos.input;

import com.bristotartur.gerenciadordepartidas.enums.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record MatchDto(@NotNull Sports sport,
                       @NotNull Importance matchImportance,
                       @NotNull Team teamA,
                       @NotNull Team teamB,
                       @NotNull List<Long> playerIds,
                       @NotNull Long eventId,
                       @NotNull Modality modality,
                       @NotNull LocalDateTime matchStart,
                       @NotNull LocalDateTime matchEnd) {
}
