package com.bristotartur.gerenciadordepartidas.handlers;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class ExceptionDetails {

    private String title;
    private Integer status;
    private String details;
    private String developerMessage;
    private LocalDateTime timestamp;

}
