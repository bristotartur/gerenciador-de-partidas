package com.bristotartur.gerenciadordepartidas.docs;

import com.bristotartur.gerenciadordepartidas.docs.examples.ExceptionResponseExamples;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestGoalDto;
import com.bristotartur.gerenciadordepartidas.dtos.response.ResponseGoalDto;
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

public final class GoalOperations {

    private GoalOperations() {
    }

    private static final String GOAL_RESPONSE_EXAMPLE = """
            {
              "goalId": 1,
              "player": "Eduardo",
              "team": "PAPA_LEGUAS",
              "goalTime": "14:20:30",
              "_links": {
                "goals": {
                  "href": "http://localhost:8080/gerenciador-de-partidas/api/goals"
                },
                "player": {
                  "href": "http://localhost:8080/gerenciador-de-partidas/api/participants/1"
                },
                "match": {
                  "href": "http://localhost:8080/gerenciador-de-partidas/api/matches/1"
                }
              }
            }
            """;

    private static final String GOAL_PAGE_RESPONSE_EXAMPLE = """
            {
              "content": [
                {
                  "goalId": 1,
                  "player": "Eduardo",
                  "team": "PAPA_LEGUAS",
                  "goalTime": "14:20:30",
                  "links": [
                    {
                      "rel": "self",
                      "href": "http://localhost:8080/gerenciador-de-partidas/api/goals/1"
                    },
                    {
                      "rel": "player",
                      "href": "http://localhost:8080/gerenciador-de-partidas/api/participants/1"
                    },
                    {
                      "rel": "match",
                      "href": "http://localhost:8080/gerenciador-de-partidas/api/matches/1"
                    }
                  ]
                }
              ],
              "pageable": {
                "pageNumber": 0,
                "pageSize": 1,
                "sort": {
                  "sorted": false,
                  "unsorted": true,
                  "empty": true
                },
                "offset": 0,
                "paged": true,
                "unpaged": false
              },
              "last": true,
              "totalPages": 1,
              "totalElements": 1,
              "first": true,
              "size": 1,
              "number": 0,
              "sort": {
                "sorted": false,
                "unsorted": true,
                "empty": true
              },
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
            summary = "Recupera todos os Gols",
            description = "Recupera uma lista paginada contendo todos os Gols disponíveis no sistema.",
            parameters = @Parameter(
                    name = "pageable",
                    description = "Objeto contendo informações sobre o formato da paginação",
                    in = ParameterIn.QUERY,
                    schema = @Schema(implementation = Pageable.class),
                    examples = @ExampleObject(
                            name = "sportEventPageableExample",
                            summary = "Exemplo de pageable para Gols",
                            value = PAGEABLE_EXAMPLE
                    )
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Lista paginada dos Gols disponíveis no sistema",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class),
                            examples = @ExampleObject(
                                    name = "goalPageExample",
                                    summary = "Exemplo de página de Gols",
                                    value = GOAL_PAGE_RESPONSE_EXAMPLE
                            )
                    )
            )
    )
    public @interface ListAllGoalsOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Recupera todos os Gols de uma partida",
            description = "Recupera uma lista paginada contendo todos os Gols relacionados a uma Partida específica.",
            parameters = {
                    @Parameter(
                            name = "match",
                            description = "Identificador único da Partida",
                            required = true,
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", format = "int64"),
                            examples = @ExampleObject(
                                    name = "matchIdExample",
                                    summary = "Exemplo de ID de Partida",
                                    value = "1"
                            )
                    ),
                    @Parameter(
                            name = "type",
                            description = "Tipo de esporte da Partida",
                            required = true,
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string"),
                            examples = {
                                    @ExampleObject(name = "futsalExample", summary = "Exemplo de futsal", value = "futsal"),
                                    @ExampleObject(name = "handballExample", summary = "Exemplo de handebol", value = "handball"),
                                    @ExampleObject(name = "unsupportedForGoalsExample", summary = "Exemplo de esporte não suportado para Gols", value = "volleyball"),
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
                                    summary = "Exemplo de pageable para Gols",
                                    value = PAGEABLE_EXAMPLE
                            )
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista paginada dos Gols relacionados a Partida",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class),
                                    examples = @ExampleObject(
                                            name = "goalPageExample",
                                            summary = "Exemplo de página de Gols",
                                            value = GOAL_PAGE_RESPONSE_EXAMPLE
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
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Esporte válido, mas não suportado para Gols",
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
    public @interface ListGoalsFromMatchOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Recupera um Gol pelo ID",
            description = "Recupera detalhes de um Gol específico com base no ID fornecido.",
            parameters = @Parameter(
                    name = "id",
                    description = "Identificador único do Gol",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "integer", format = "int64"),
                    examples = @ExampleObject(
                            name = "goalIdExample",
                            summary = "Exemplo de ID de Gol",
                            value = "1"
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Gol correspondente ao ID",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseGoalDto.class),
                                    examples = @ExampleObject(
                                            name = "goalExample",
                                            summary = "Exemplo de Gol",
                                            value = GOAL_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Gol não encontrado",
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
    public @interface FindGoalByIdOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Adiciona um novo Gol",
            description = "Gera um novo Gol a partir dos dados fornecidos.",
            requestBody = @RequestBody(
                    description = "Corpo de requisição contendo detalhes do Gol a ser criado",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestGoalDto.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Gol adicionado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseGoalDto.class),
                                    examples = @ExampleObject(
                                            name = "goalExample",
                                            summary = "Exemplo de Gol",
                                            value = GOAL_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitação inválida para adicionar o Gol",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "badRequestExceptionOnValidation",
                                            summary = "Exemplo de Bad Request Exception na validação de corpo de requisição",
                                            value = ExceptionResponseExamples.BAD_REQUEST_ON_VALIDATION_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Partida ou Jogador relacionado ao Gol não encontrado",
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
                            description = "O estado da Partida relacionada ao Gol não permite com que ele seja adicionado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "conflictExceptionExample",
                                            summary = "Exemplo de Conflict Exception",
                                            value = ExceptionResponseExamples.CONFLICT_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Jogador associado ao Gol não está presente na Partida",
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
    public @interface SaveGoalOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Remove um Gol",
            description = "Remove um Gol do sistema com base no seu ID",
            parameters = @Parameter(
                    name = "id",
                    description = "Identificador único do Gol",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "integer", format = "int64"),
                    examples = @ExampleObject(
                            name = "goalIdExample",
                            summary = "Exemplo de ID de Gol",
                            value = "1"
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Gol removido com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Gol não encontrado",
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
                            description = "O estado da Partida relacionada ao Gol não permite com que ele seja removido",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "conflictExceptionExample",
                                            summary = "Exemplo de Conflict Exception",
                                            value = ExceptionResponseExamples.CONFLICT_RESPONSE_EXAMPLE
                                    )
                            )
                    )
            }
    )
    public @interface DeleteGoalOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Atualiza um Gol",
            description = "Atualiza os detalhes de um Gol a partir de seu ID e dos dados fornecidos.",
            parameters = @Parameter(
                    name = "id",
                    description = "Identificador único do Gol",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "integer", format = "int64"),
                    examples = @ExampleObject(
                            name = "goalIdExample",
                            summary = "Exemplo de ID de Gol",
                            value = "1"
                    )
            ),
            requestBody = @RequestBody(
                    description = "Corpo de requisição contendo os novos dados do Gol a ser atualizado",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestGoalDto.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Gol atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseSportEventDto.class),
                                    examples = @ExampleObject(
                                            name = "goalExample",
                                            summary = "Exemplo de Gol",
                                            value = GOAL_RESPONSE_EXAMPLE
                                    )

                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitação inválida para atualizar o Gol",
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
                            description = "Gol ou alguma das entidades relacionadas a ele não encontrados",
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
                            description = "O estado da Partida relacionada ao Gol impede com que ele seja atualizado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDetails.class),
                                    examples = @ExampleObject(
                                            name = "conflictExceptionExample",
                                            summary = "Exemplo de Conflict Exception",
                                            value = ExceptionResponseExamples.CONFLICT_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "O corpo de requisição possui dados inválidos que impedem a atualização do Gol",
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
    public @interface ReplaceGoalOperation {
    }

}
