package com.bristotartur.gerenciadordepartidas.exceptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionDetails {

    private String title;
    private Integer status;
    private String details;
    private String developerMessage;
    private LocalDateTime timestamp;
}
