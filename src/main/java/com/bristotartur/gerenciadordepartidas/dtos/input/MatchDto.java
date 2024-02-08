package com.bristotartur.gerenciadordepartidas.dtos.input;

import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record MatchDto(@NotNull Sports sport,
                       @NotNull Team teamA,
                       @NotNull Team teamB,
                       @NotNull List<Long> playerIds,
                       @NotNull Long eventId,
                       @NotNull Modality modality,
                       @NotNull Status matchStatus,
                       @NotNull LocalDateTime matchStart,
                       @NotNull LocalDateTime matchEnd) {
}
