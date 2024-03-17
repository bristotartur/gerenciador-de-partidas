package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.controllers.docs.EditionOperations;
import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.dtos.response.ResponseEditionDto;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestEditionDto;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.mappers.EditionMapper;
import com.bristotartur.gerenciadordepartidas.services.events.EditionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/gerenciador-de-partidas/api/editions")
@RequiredArgsConstructor
@Transactional
@Slf4j
@Tag(name = "Edition")
public class EditionController {

    private final EditionService editionService;
    private final EditionMapper editionMapper;

    @EditionOperations.ListAllEditionsOperation
    @GetMapping
    public ResponseEntity<Page<ResponseEditionDto>> listAllEditions(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();

        log.info("Request to get Edition page of number '{}' and size '{}' was made.", number, size);

        var dtoPage = this.createExposingDtoPage(editionService.findAllEditions(pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @EditionOperations.FindEditionByIdOperation
    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseEditionDto> findEditionById(@PathVariable Long id) {

        log.info("Request to find Edition '{}' was made.", id);

        var dto = this.createSingleExposingDto(editionService.findEditionById(id));
        return ResponseEntity.ok().body(dto);
    }

    @EditionOperations.SaveEditionOperation
    @PostMapping
    public ResponseEntity<ResponseEditionDto> saveEdition(@RequestBody @Valid RequestEditionDto requestEditionDto) {

        log.info("Request to create Edition was made.");

        var dto = this.createSingleExposingDto(editionService.saveEdition(requestEditionDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @EditionOperations.DeleteEditionOperation
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteEdition(@PathVariable Long id) {

        log.info("Requeest to delete Edition was made.");

        editionService.deleteEditionById(id);
        return ResponseEntity.noContent().build();
    }

    @EditionOperations.ReplaceEditionOperation
    @PutMapping(path = "/{id}")
    public ResponseEntity<ResponseEditionDto> replaceEdition(@PathVariable Long id,
                                                             @RequestBody @Valid RequestEditionDto requestEditionDto) {
        log.info("Request to update Edition was made.");

        var dto = this.createSingleExposingDto(editionService.replaceEdition(id, requestEditionDto));
        return ResponseEntity.ok().body(dto);
    }

    @EditionOperations.UpdateEditionStatusOperation
    @PutMapping(path = "/{id}/update")
    public ResponseEntity<ResponseEditionDto> updateEditionStatus(@PathVariable Long id,
                                                                  @RequestParam("status") String editionStatus) {
        var status = Status.findStatusLike(editionStatus);
        log.info("Request to update Edition '{}' with to status '{}' was made.", id, status);

        var dto = this.createSingleExposingDto(editionService.updateEditionStatus(id, status));
        return ResponseEntity.ok().body(dto);
    }

    private ResponseEditionDto createSingleExposingDto(Edition edition) {

        var dto = editionMapper.toNewExposingEditionDto(edition);
        var id = edition.getId();
        var pageable = PageRequest.of(0, 12);

        dto.add(linkTo(methodOn(this.getClass()).listAllEditions(pageable)).withRel("editions"));
        dto.add(linkTo(methodOn(SportEventController.class).listSportEventsFromEdition(id, pageable)).withRel("sportEvents"));
        return dto;
    }

    private Page<ResponseEditionDto> createExposingDtoPage(Page<Edition> editionsPage) {

        var editions = editionsPage.getContent();

        var dtos = editions.stream()
                .map(this::addSingleEditionLink)
                .toList();

        return new PageImpl<>(dtos, editionsPage.getPageable(), editions.size());
    }

    private ResponseEditionDto addSingleEditionLink(Edition edition) {

        var id = edition.getId();
        var dto = editionMapper.toNewExposingEditionDto(edition);
        var pageable = PageRequest.of(0, 12);

        dto.add(linkTo(methodOn(this.getClass()).findEditionById(id)).withSelfRel());
        dto.add(linkTo(methodOn(SportEventController.class).listSportEventsFromEdition(edition.getId(), pageable)).withRel("sportEvents"));
        return dto;
    }

}
