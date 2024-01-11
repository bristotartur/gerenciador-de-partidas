package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.enums.Sports;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record GoalDto(@NotNull LocalTime goalTime,
                      @NotNull Long teamId,
                      @NotNull Long matchSportId,
                      @NotNull Sports sport) {
}
