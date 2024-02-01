package com.bristotartur.gerenciadordepartidas.dtos.input;

import com.bristotartur.gerenciadordepartidas.enums.Status;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EditionDto(@NotNull Status editionStatus,
                         @NotNull LocalDate opening,
                         @NotNull LocalDate closure) {
}
