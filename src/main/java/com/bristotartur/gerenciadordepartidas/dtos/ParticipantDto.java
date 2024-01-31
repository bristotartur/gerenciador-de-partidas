package com.bristotartur.gerenciadordepartidas.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ParticipantDto(@NotBlank String name,
                             @NotBlank String classNumber,
                             @NotNull Long teamId,
                             @NotNull Long editionId) {
}
