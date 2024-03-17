package com.bristotartur.gerenciadordepartidas.dtos.request;

import com.bristotartur.gerenciadordepartidas.enums.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RequestParticipantDto(@NotBlank String name,
                                    @NotBlank String classNumber,
                                    @NotNull Team team,
                                    @NotNull Long editionId) {
}
