package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import jakarta.validation.constraints.NotNull;

public record TeamDto(@NotNull TeamName teamName,
                      @NotNull Integer points) {
}
