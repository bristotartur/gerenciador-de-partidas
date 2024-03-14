package com.bristotartur.gerenciadordepartidas.dtos.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "DTO de requisição de Edições")
public record EditionDto(

        @Schema(description = "Data de início da da Edição", example = "2024-04-01")
        @NotNull LocalDate opening,

        @Schema(description = "Data de encerramento da Edição", example = "2024-05-15")
        @NotNull LocalDate closure) {
}
