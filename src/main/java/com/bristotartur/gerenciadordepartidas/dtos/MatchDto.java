package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record MatchDto(@NotNull Sports sport,
                       @NotNull Long teamAId,
                       @NotNull Long teamBId,
                       @NotNull List<Long> playerIds,
                       @NotNull Modality modality,
                       @NotNull Status matchStatus,
                       @NotNull LocalDateTime matchStart,
                       @NotNull LocalDateTime matchEnd) {
}
