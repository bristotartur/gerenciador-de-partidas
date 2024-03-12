package com.bristotartur.gerenciadordepartidas.handlers;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Schema(description = "Corpo de resposta para exceções de validação")
public class ValidationExceptionDetails extends ExceptionDetails {

    @Schema(description = "Os campos inválidos")
    private String fields;

    @Schema(description = "O motivo de cada campo estar inválido")
    private String fieldsMessages;

}
