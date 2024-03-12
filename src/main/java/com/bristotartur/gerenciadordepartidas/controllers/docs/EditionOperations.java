package com.bristotartur.gerenciadordepartidas.controllers.docs;

import com.bristotartur.gerenciadordepartidas.controllers.EditionController;
import com.bristotartur.gerenciadordepartidas.dtos.exposing.ExposingEditionDto;
import com.bristotartur.gerenciadordepartidas.dtos.input.EditionDto;
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

/**
 * Classe utilitária para a definição da documentação dos endpoints referentes ao
 * controller {@link EditionController}.
 *
 * @see ExceptionResponseExamples
 * @see ExceptionDetails
 * @see ValidationExceptionDetails
 */
public final class EditionOperations {

    private EditionOperations() {
    }

    private static final String EDITION_RESPONSE_EXAMPLE = """
            {
              "editionId": 1,
              "atomica": 0,
              "mestres": 0,
              "papaLeguas": 0,
              "twister": 0,
              "unicontti": 0,
              "editionStatus": "SCHEDULED",
              "opening": "2024-04-21",
              "closure": "2024-05-04",
              "_links": {
                "editions": {
                  "href": "http://localhost:8080/gerenciador-de-partidas/api/editions"
                },
                "sportEvents": {
                  "href": "http://localhost:8080/gerenciador-de-partidas/api/sport-events/from?edition=1"
                }
              }
            }
            """;

    private static final String EDITION_PAGE_RESPONSE_EXAMPLE = """
            {
              "content": [
                {
                  "editionId": 1,
                  "atomica": 0,
                  "mestres": 0,
                  "papaLeguas": 0,
                  "twister": 0,
                  "unicontti": 0,
                  "editionStatus": "SCHEDULED",
                  "opening": "2024-04-21",
                  "closure": "2024-05-04",
                  "links": [
                    {
                      "rel": "self",
                      "href": "http://localhost:8080/gerenciador-de-partidas/api/editions/1"
                    },
                    {
                      "rel": "sportEvents",
                      "href": "http://localhost:8080/gerenciador-de-partidas/api/sport-events/from?edition=1"
                    }
                  ]
                }
              ],
              "pageable": {
                "pageNumber": 0,
                "pageSize": 12,
                "sort": {
                  "empty": true,
                  "unsorted": true,
                  "sorted": false
                },
                "offset": 0,
                "unpaged": false,
                "paged": true
              },
              "last": true,
              "totalElements": 1,
              "totalPages": 1,
              "size": 12,
              "number": 0,
              "sort": {
                "empty": true,
                "unsorted": true,
                "sorted": false
              },
              "numberOfElements": 1,
              "first": true,
              "empty": false
            }
            """;

    public static final String PAGEABLE_EXAMPLE = """
            {
              "page": 0,
              "size": 1,
              "sort": [
                "id"
              ]
            }
            """;

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Recupera todas as Edições",
            description = "Recupera uma lista paginada contendo todas as Edições disponíveis no sistema.",
            parameters = @Parameter(
                    name = "pageable",
                    description = "Objeto contendo informações sobre o formato da paginação",
                    in = ParameterIn.QUERY,
                    schema = @Schema(implementation = Pageable.class),
                    examples = @ExampleObject(
                            name = "edition PageableExample",
                            summary = "Exemplo de pageable para Eventos Esportivos",
                            value = PAGEABLE_EXAMPLE
                    )
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Lista paginada das Edições disponíveis no sistema",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class),
                            examples = @ExampleObject(
                                    name = "editionPageExample",
                                    summary = "Exemplo de página de Edições",
                                    value = EDITION_PAGE_RESPONSE_EXAMPLE
                            )
                    )
            )
    )
    public @interface ListAllEditionsOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Recupera uma Edição pelo ID",
            description = "Recupera detalhes de uma Edição específica com base no ID fornecido.",
            parameters = @Parameter(
                    name = "id",
                    description = "Identificador único da Edição",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "integer", format = "int64")
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Edição encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExposingEditionDto.class),
                                    examples = @ExampleObject(
                                            name = "editionExample",
                                            summary = "Exemplo de Edição",
                                            value = EDITION_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Edição não encontrada",
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
    public @interface FindEditionByIdOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Adiciona uma nova Edição",
            description = "Gera uma nova Edição a partir dos dados fornecidos.",
            requestBody = @RequestBody(
                    description = "Objeto contendo detalhes da Edição a ser criada",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditionDto.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Edição adicionada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExposingEditionDto.class),
                                    examples = @ExampleObject(
                                            name = "editionExample",
                                            summary = "Exemplo de Edição",
                                            value = EDITION_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Solicitação inválida para adicionar a Edição",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationExceptionDetails.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "badRequestExceptionExample",
                                                    summary = "Exemplo de Bad Request Exception",
                                                    value = ExceptionResponseExamples.BAD_REQUEST_RESPONSE_EXAMPLE
                                            ),
                                            @ExampleObject(
                                                    name = "badRequestExceptionOnValidationResponseExample",
                                                    summary = "Exemplo de Bad Request Exception na validação de corpo de requisição",
                                                    value = ExceptionResponseExamples.BAD_REQUEST_ON_VALIDATION_RESPONSE_EXAMPLE
                                            )
                                    }
                            )
                    )
            }
    )

    public @interface SaveEditionOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Remove uma Edição",
            description = "Remove uma Edição do sistema com base no seu ID.",
            parameters = @Parameter(
                    name = "id",
                    description = "Identificador único da Edição",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "integer", format = "int64")
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Edição removida com sucesso"
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
                            responseCode = "422",
                            description = "Edição não pode ser removida",
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
    public @interface DeleteEditionOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Atualiza uma Edição",
            description = "Substitui os detalhes de uma Edição existente no sistema com base no seu ID.",
            parameters = @Parameter(
                    name = "id",
                    description = "Identificador único da Edição",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "integer", format = "int64")
            ),
            requestBody = @RequestBody(
                    description = "Objeto contendo os novos detalhes da edição",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditionDto.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Edição atualizada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExposingEditionDto.class),
                                    examples = @ExampleObject(
                                            name = "editionExample",
                                            summary = "Exemplo de Edição",
                                            value = EDITION_RESPONSE_EXAMPLE
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitação inválida para atualizar a Edição",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationExceptionDetails.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "badRequestExceptionExample",
                                                    summary = "Exemplo de Bad Request Exception",
                                                    value = ExceptionResponseExamples.BAD_REQUEST_RESPONSE_EXAMPLE
                                            ),
                                            @ExampleObject(
                                                    name = "badRequestExceptionOnValidationResponseExample",
                                                    summary = "Exemplo de Bad Request Exception na validação de corpo de requisição",
                                                    value = ExceptionResponseExamples.BAD_REQUEST_ON_VALIDATION_RESPONSE_EXAMPLE
                                            )
                                    }
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
    public @interface ReplaceEditionOperation {
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Atualiza o status de uma Edição por ID",
            description = "Atualiza o status de uma Edição existente com base no ID fornecido.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Identificador único da Edição",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "integer", format = "int64")
                    ),
                    @Parameter(
                            name = "status",
                            description = "Novo status desejado para a Edição",
                            required = true,
                            in = ParameterIn.QUERY
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Status da Edição atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExposingEditionDto.class),
                                    examples = @ExampleObject(
                                            name = "editionExample",
                                            summary = "Exemplo de Edição",
                                            value = EDITION_RESPONSE_EXAMPLE
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
                            description = "Estado da Edição é conflituoso com o estado de outras entidades",
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
    public @interface UpdateEditionStatusOperation {
    }

}
