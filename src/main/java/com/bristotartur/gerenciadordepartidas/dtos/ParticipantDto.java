package com.bristotartur.gerenciadordepartidas.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Builder
public record ParticipantDto(@NotBlank String name,
                             @NotBlank String classNumber,
                             @NotNull Long teamId) {
}
