package com.bristotartur.gerenciadordepartidas.dtos.input;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EditionDto(@NotNull LocalDate opening,
                         @NotNull LocalDate closure) {
}
