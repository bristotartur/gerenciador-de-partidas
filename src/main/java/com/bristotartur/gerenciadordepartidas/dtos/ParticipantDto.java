package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ParticipantDto(@NotBlank String name,
                             @NotBlank String classNumber,
                             @NotNull TeamName team,
                             @NotNull Long editionId) {
}
