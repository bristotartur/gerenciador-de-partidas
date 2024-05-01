package com.bristotartur.gerenciadordepartidas.docs.examples;

import com.bristotartur.gerenciadordepartidas.docs.EditionOperations;
import com.bristotartur.gerenciadordepartidas.handlers.ExceptionDetails;
import com.bristotartur.gerenciadordepartidas.handlers.ValidationExceptionDetails;

/**
 * Classe utilitária que define exemplos de corpos de resposta para exceções para documentar a API.
 *
 * @see EditionOperations
 * @see ExceptionDetails
 * @see ValidationExceptionDetails
 */
public final class ExceptionResponseExamples {

    private ExceptionResponseExamples() {
    }

    public static final String BAD_REQUEST_RESPONSE_EXAMPLE = """
            {
              "title": "Bad Request Exception.",
              "status": 400,
              "details": "Detalhes da exceção.",
              "developerMessage": "com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException",
              "timestamp": "2024-02-24T16:36:10.84352"
            }
            """;

    public static final String BAD_REQUEST_ON_VALIDATION_RESPONSE_EXAMPLE = """
            {
                "title": "Bad Request Exception.",
                "status": 400,
                "details": "Detalhes da exceção.",
                "developerMessage": "org.springframework.web.bind.MethodArgumentNotValidException",
                "timestamp": "2024-03-08T19:05:40.1916915",
                "fields": "Campos inválidos",
                "fieldsMessages": "Motivo de cada campo estar inválido"
            }
            """;

    public static final String NOT_FOUND_RESPONSE_EXAMPLE = """
            {
              "title": "Not Found Exception.",
              "status": 404,
              "details": "Detalhes da exceção.",
              "developerMessage": "com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException",
              "timestamp": "2024-02-24T16:36:10.84352"
            }
            """;

    public static final String CONFLICT_RESPONSE_EXAMPLE = """
            {
              "title": "Conflict Exception.",
              "status": 409,
              "details": "Detalhes da exceção.",
              "developerMessage": "com.bristotartur.gerenciadordepartidas.exceptions.ConflictException",
              "timestamp": "2024-02-24T16:36:10.84352"
            }
            """;

    public static final String UNPROCESSABLE_ENTITY_RESPONSE_EXAMPLE = """
            {
              "title": "Unprocessable Entity Exception.",
              "status": 422,
              "details": "Detalhes da exceção.",
              "developerMessage": "com.bristotartur.gerenciadordepartidas.exceptions.UnprocessableEntityException",
              "timestamp": "2024-02-24T16:36:10.84352"
            }
            """;

}
