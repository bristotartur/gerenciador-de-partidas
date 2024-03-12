package com.bristotartur.gerenciadordepartidas.handlers;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@Schema(description = "Corpo de resposta para exceções")
public class ExceptionDetails {

    @Schema(description = "Título da exceção")
    private String title;

    @Schema(description = "Código de status HTTP")
    private Integer status;

    @Schema(description = "Detalhes da exceção")
    private String details;

    @Schema(description = "Mensagem do desenvolvedor")
    private String developerMessage;

    @Schema(description = "Data e horário da exceção")
    private LocalDateTime timestamp;

}
