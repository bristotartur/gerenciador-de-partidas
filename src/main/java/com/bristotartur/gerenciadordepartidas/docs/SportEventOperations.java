package com.bristotartur.gerenciadordepartidas.docs;

import com.bristotartur.gerenciadordepartidas.docs.examples.ExceptionResponseExamples;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestSportEventDto;
import com.bristotartur.gerenciadordepartidas.dtos.response.ResponseSportEventDto;
import com.bristotartur.gerenciadordepartidas.handlers.ExceptionDetails;
import com.bristotartur.gerenciadordepartidas.handlers.ValidationExceptionDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public final class SportEventOperations {

    private SportEventOperations() {
    }

    public static final String SPORT_EVENT_RESPONSE_EXAMPLE = """
            {
              "sportEventId": 1,
              "type": "FUTSAL",
              "modality": "MASCULINE",
              "firstPlace": "NONE",
              "secondPlace": "NONE",
              "thirdPlace": "NONE",
              "fourthPlace": "NONE",
              "fifthPlace": "NONE",
              "totalMatches": 14,
              "eventStatus": "SCHEDULED",
              "_links": {
                "sportEventList": {
                  "href": "http://localhost:8080/gerenciador-de-partidas/api/sport-events"
                },
                "edition": {
                  "href": "http://localhost:8080/gerenciador-de-partidas/api/editions/1"
                }
              }
            }
            """;

    public static final String SPORT_EVENT_PAGE_RESPONSE_EXAMPLE = """
            {
              "content": [
                {
                  "sportEventId": 1,
                  "type": "FUTSAL",
                  "modality": "MASCULINE",
                  "firstPlace": "NONE",
                  "secondPlace": "NONE",
                  "thirdPlace": "NONE",
                  "fourthPlace": "NONE",
                  "fifthPlace": "NONE",
                  "totalMatches": 14,
                  "eventStatus": "SCHEDULED",
                  "links": [
                    {
                      "rel": "self",
                      "href": "http://localhost:8080/gerenciador-de-partidas/api/sport-events/1"
                    },
                    {
                      "rel": "edition",
                      "href": "http://localhost:8080/gerenciador-de-partidas/api/editions/1"
                    }
                  ]
                }
              ],
              "pageable": {
                "pageNumber": 0,
                "pageSize": 12,
                "sort": {
                  "empty": true,
                  "sorted": false,
                  "unsorted": true
                },
                "offset": 0,
                "paged": true,
                "unpaged": false
              },
              "last": true,
              "totalElements": 1,
              "totalPages": 1,
              "size": 12,
              "number": 0,
              "sort": {
                "empty": true,
                "sorted": false,
                "unsorted": true
              },
              "first": true,
              "numberOfElements": 1,
              "empty": false
            }
            """;

    public static final String PAGEABLE_EXAMPLE = """
            {
              "page": 0,
              "size": 14
            }
            """;

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Recupera todos os Eventos Esportivos",
            description = "Recupera uma lista paginada contendo todos os Eventos Esportivos disponíveis no sistema.",
            parameters = @Parameter(
                    name = "pageable",
                    description = "Objeto contendo informações sobre o formato da paginação",
                    in = ParameterIn.QUERY,
                    schema = @Schema(implementation = Pageable.class),
                    examples = @ExampleObject(
                            name = "sportEventPageableExample",
                            summary = "Exemplo de pageable para Eventos Esportivos",
                            value = PAGEABLE_EXAMPLE
                    )
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Lista paginada dos Eventos Esportivos disponíveis no sistema",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class),
                            examples = @ExampleObject(
                                    name = "sportEventPageExample",
                                    summary = "Exemplo de página de Eventos Esportivos",
                                    value = SPORT_EVENT_PAGE_RESPONSE_EXAMPLE
                            )
                    )
            )
    )
    public @interface ListAllSportEventsOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Recupera todos os Eventos Esportivos de uma Edição",
            description = "Recupera uma lista paginada contendo todos os Eventos Esportivos relacionados a uma Edição específica.",
            parameters = {
                    @Parameter(
                            name = "edition",
                            description = "Identificador único da Edição",
                            required = true,
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", format = "int64"),
                            examples = @ExampleObject(
                                    name = "editionIdExample",
                                    summary = "Exemplo de ID de Edição",
                                    value = "1"
                            )
                    ),
                    @Parameter(
                            name = "pageable",
                            description = "Objeto contendo informações sobre o formato da paginação",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Pageable.class),
                            examples = @ExampleObject(
                                    name = "sportEventPageableExample",
                                    summary = "Exemplo de pageable para Eventos Esportivos",
                                    value = PAGEABLE_EXAMPLE
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista paginada dos Eventos Esportivos relacionados a uma Edição",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class),
                                    examples = @ExampleObject(
                                            name = "sportEventPageExample",
                                            summary = "Exemplo de página de Eventos Esportivos",
                                            value = SPORT_EVENT_PAGE_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Edição não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "notFoundExceptionExample",
                                            summary = "Exemplo de Not Found Exception",
                                            value = ExceptionResponseExamples.NOT_FOUND_RESPONSE_EXAMPLE
                                    )
                            )
                    )
            }
    )
    public @interface ListSportEventsFromEditionOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Recupera todos os Eventos Esportivos de um tipo de esporte",
            description = "Recupera uma lista paginada contendo todos os Eventos Esportivos de um determinado tipo de esporte.",
            parameters = {
                    @Parameter(
                            name = "sport",
                            description = "Tipo de esporte",
                            required = true,
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string"),
                            examples = {
                                    @ExampleObject(name = "futsalExample", summary = "Exemplo de futsal", value = "futsal"),
                                    @ExampleObject(name = "handballExample",summary = "Exemplo de handebol", value = "handball"),
                                    @ExampleObject(name = "basketballExample", summary = "Exemplo de basquete", value = "basketball"),
                                    @ExampleObject(name = "volleyballExample", summary = "Exemplo de vôlei", value = "volleyball"),
                                    @ExampleObject(name = "tableTennisExample", summary = "Exemplo de tênis de mesa", value = "table-tennis"),
                                    @ExampleObject(name = "chessExample", summary = "Exemplo de xadrez", value = "chess"),
                                    @ExampleObject(name = "invalidExample", summary = "Exemplo de esporte inválido", value = "voleibol")
                            }
                    ),
                    @Parameter(
                            name = "pageable",
                            description = "Objeto contendo informações sobre o formato da paginação",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Pageable.class),
                            examples = @ExampleObject(
                                    name = "sportEventPageableExample",
                                    summary = "Exemplo de pageable para Eventos Esportivos",
                                    value = PAGEABLE_EXAMPLE
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista paginada dos Eventos Esportivos do mesmo tipo de esporte",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class),
                                    examples = @ExampleObject(
                                            name = "sportEventPageExample",
                                            summary = "Exemplo de página de Eventos Esportivos",
                                            value = SPORT_EVENT_PAGE_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Esporte inválido",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "badRequestExceptionExample",
                                            summary = "Exemplo de Bad Request Exception",
                                            value = ExceptionResponseExamples.BAD_REQUEST_RESPONSE_EXAMPLE
                                    )
                            )
                    )
            }
    )
    public @interface ListSportEventsBySportOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Recupera um Evento Esportivo pelo ID",
            description = "Recupera detalhes de um Evento Esportivo com base no ID fornecido.",
            parameters = @Parameter(
                    name = "id",
                    description = "Identificador único do Evento Esportivo",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "integer", format = "int64"),
                    examples = @ExampleObject(
                            name = "sportEventIdExample",
                            summary = "Exemplo de ID de Evento Esportivo",
                            value = "1"
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Evento Esportivo correspondente ao ID",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseSportEventDto.class),
                                    examples = @ExampleObject(
                                            name = "sportEventExample",
                                            summary = "Exemplo de Evento Esportivo",
                                            value = SPORT_EVENT_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "Evento Esportivo não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "notFoundExceptionExample",
                                            summary = "Exemplo de Not Found Exception",
                                            value = ExceptionResponseExamples.NOT_FOUND_RESPONSE_EXAMPLE
                                    )
                            )
                    )
            }

    )
    public @interface FindSportEventByIdOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Adiciona um novo Evento Esportivo",
            description = "Gera um novo Evento Esportivo a partir dos dados fornecidos.",
            requestBody = @RequestBody(
                    description = "Corpo de requisição contendo detalhes do Evento Esportivo a ser criado",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestSportEventDto.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Evento Esportivo adicionado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseSportEventDto.class),
                                    examples = @ExampleObject(
                                            name = "sportEventExample",
                                            summary = "Exemplo de Evento Esportivo",
                                            value = SPORT_EVENT_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitação inválida para adicionar o Evento Esportivo",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "badRequestExceptionOnValidationResponseExample",
                                            summary = "Exemplo de Bad Request Exception na validação de corpo de requisição",
                                            value = ExceptionResponseExamples.BAD_REQUEST_ON_VALIDATION_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Edição não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "notFoundExceptionExample",
                                            summary = "Exemplo de Not Found Exception",
                                            value = ExceptionResponseExamples.NOT_FOUND_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Estado do Evento Esportivo é conflituoso com o estado de outras entidades",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "conflictException",
                                            summary = "Exemplo de Conflict Exception",
                                            value = ExceptionResponseExamples.CONFLICT_RESPONSE_EXAMPLE
                                    )
                            )
                    )
            }
    )
    public @interface SaveSportEventOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Remove um Evento Esportivo",
            description = "Remove um Evento Esportivo do sistema com base no seu ID.",
            parameters = @Parameter(
                    name = "id",
                    description = "Identificador único do Evento Esportivo",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "integer", format = "int64"),
                    examples = @ExampleObject(
                            name = "sportEventIdExample",
                            summary = "Exemplo de ID de Evento Esportivo",
                            value = "1"
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Evento Esportivo removido com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Evento Esportivo não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "notFoundExceptionExample",
                                            summary = "Exemplo de Not Found Exception",
                                            value = ExceptionResponseExamples.NOT_FOUND_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Evento Esportivo não pode ser removido",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "unprocessableEntityExceptionExample",
                                            summary = "Exemplo de Unprocessable Entity Exception",
                                            value = ExceptionResponseExamples.UNPROCESSABLE_ENTITY_RESPONSE_EXAMPLE
                                    )
                            )
                    )
            }
    )
    public @interface DeleteSportEventOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Atualiza um Evento Esportivo",
            description = "Substitui os detalhes de um Evento Esportivo a partir de seu ID e dos dados fornecidos.",
            parameters = @Parameter(
                    name = "id",
                    description = "Identificador único do Evento Esportivo",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "integer", format = "int64"),
                    examples = @ExampleObject(
                            name = "sportEventIdExample",
                            summary = "Exemplo de ID de Evento Esportivo",
                            value = "1"
                    )
            ),
            requestBody = @RequestBody(
                    description = "Corpo de requisição contendo os novos dados do Evento Esportivo a ser atualizado",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestSportEventDto.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Evento Esportivo atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseSportEventDto.class),
                                    examples = @ExampleObject(
                                            name = "sportEventExample",
                                            summary = "Exemplo de Evento Esportivo",
                                            value = SPORT_EVENT_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitação inválida para atualizar o Evento Esportivo",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "badRequestExceptionOnValidationResponseExample",
                                            summary = "Exemplo de Bad Request Exception na validação de corpo de requisição",
                                            value = ExceptionResponseExamples.BAD_REQUEST_ON_VALIDATION_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Edição ou Evento Esportivo não encontrados",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "notFoundExceptionExample",
                                            summary = "Exemplo de Not Found Exception",
                                            value = ExceptionResponseExamples.NOT_FOUND_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Estado do Evento Esportivo é conflituoso com o estado de outras entidades",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "conflictException",
                                            summary = "Exemplo de Conflict Exception",
                                            value = ExceptionResponseExamples.CONFLICT_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Evento Esportivo não pode ser atualizado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "unprocessableEntityExceptionExample",
                                            summary = "Exemplo de Unprocessable Entity Exception",
                                            value = ExceptionResponseExamples.UNPROCESSABLE_ENTITY_RESPONSE_EXAMPLE
                                    )
                            )
                    )
            }
    )
    public @interface ReplaceSportEventOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Atualiza o status de um Evento Esportivo pelo ID",
            description = "Atualiza o status de um Evento Esportivo existente com base no seu ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Identificador único do Evento Esportivo",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "integer", format = "int64"),
                            examples = @ExampleObject(
                                    name = "sportEventIdExample",
                                    summary = "Exemplo de ID de Evento Esportivo",
                                    value = "1"
                            )
                    ),
                    @Parameter(
                            name = "status",
                            description = "Novo status desejado para o Evento Esportivo",
                            required = true,
                            in = ParameterIn.QUERY,
                            examples = {
                                    @ExampleObject(name = "scheduledExample", summary = "Status 'agendado'", value = "SCHEDULED"),
                                    @ExampleObject(name = "inProgressExample", summary = "Status 'em andamento'", value = "IN_PROGRESS"),
                                    @ExampleObject(name = "endedExample", summary = "Status 'encerrado'", value = "ENDED"),
                                    @ExampleObject(name = "openForEditsExample", summary = "Status 'aberto para edições'", value = "OPEN_FOR_EDITS")
                            }
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Status do Evento Esportivo atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseSportEventDto.class),
                                    examples = @ExampleObject(
                                            name = "sportEventExample",
                                            summary = "Exemplo de Evento Esportivo",
                                            value = SPORT_EVENT_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitação inválida para atualizar o status",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "badRequestExceptionExample",
                                            summary = "Exemplo de Bad Request Exception",
                                            value = ExceptionResponseExamples.BAD_REQUEST_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Evento Esportivo não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "notFoundExceptionExample",
                                            summary = "Exemplo de Not Found Exception",
                                            value = ExceptionResponseExamples.NOT_FOUND_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Status negado devido a questões de lógica de negócios",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "unprocessableEntityExceptionExample",
                                            summary = "Exemplo de Unprocessable Entity Exception",
                                            value = ExceptionResponseExamples.UNPROCESSABLE_ENTITY_RESPONSE_EXAMPLE
                                    )
                            )
                    )
            }
    )
    public @interface UpdateSportEventStatusOperation {
    }

}
