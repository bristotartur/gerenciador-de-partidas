package com.bristotartur.gerenciadordepartidas.handlers;

import com.bristotartur.gerenciadordepartidas.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleNotFoundException(NotFoundException exception) {

        return new ResponseEntity<>(ExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .title("NotFoundException.")
                .details(exception.getMessage())
                .developerMessage(exception.getClass().getName())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDetails> handleBadRequestException(BadRequestException exception) {

        return new ResponseEntity<>(ExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title("BadRequestException.")
                .details(exception.getMessage())
                .developerMessage(exception.getClass().getName())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionDetails> handleConflictException(ConflictException exception) {

        return new ResponseEntity<>(ExceptionDetails.builder().
                timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .title("ConflictException.")
                .details(exception.getMessage())
                .developerMessage(exception.getClass().getName())
                .build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionDetails> handleForbiddenException(ForbiddenException exception) {

        return new ResponseEntity<>(ExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .title("ForbiddenException")
                .details(exception.getMessage())
                .developerMessage(exception.getClass().getName())
                .build(), HttpStatus.FORBIDDEN);
    }

}
