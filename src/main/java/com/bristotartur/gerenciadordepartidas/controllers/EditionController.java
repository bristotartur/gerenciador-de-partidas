package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.dtos.exposing.ExposingEditionDto;
import com.bristotartur.gerenciadordepartidas.dtos.input.EditionDto;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.mappers.EditionMapper;
import com.bristotartur.gerenciadordepartidas.services.events.EditionService;
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
public class EditionController {

    private final EditionService editionService;
    private final EditionMapper editionMapper;

    @GetMapping
    public ResponseEntity<Page<ExposingEditionDto>> listAllEditions(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();

        log.info("Request to get Edition page of number '{}' and size '{}' was made.", number, size);

        var dtoPage = this.createExposingDtoPage(editionService.findAllEditions(pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExposingEditionDto> findEditionById(@PathVariable Long id) {

        log.info("Request to find Edition '{}' was made.", id);

        var dto = this.createSingleExposingDto(editionService.findEditionById(id));
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<ExposingEditionDto> saveEdition(@RequestBody @Valid EditionDto editionDto) {

        log.info("Request to create Edition was made.");

        var dto = this.createSingleExposingDto(editionService.saveEdition(editionDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteEdition(@PathVariable Long id) {

        log.info("Requeest to delete Edition was made.");

        editionService.deleteEditionById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ExposingEditionDto> replaceEdition(@PathVariable Long id,
                                                             @RequestBody @Valid EditionDto editionDto) {
        log.info("Request to update Edition was made.");

        var dto = this.createSingleExposingDto(editionService.replaceEdition(id, editionDto));
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping(path = "/{id}/update")
    public ResponseEntity<ExposingEditionDto> updateEditionStatus(@PathVariable Long id,
                                                                  @RequestParam("status") String editionStatus) {
        var status = Status.findStatusLike(editionStatus);
        log.info("Request to update Edition '{}' with to status '{}' was made.", id, status);

        var dto = this.createSingleExposingDto(editionService.updateEditionStatus(id, status));
        return ResponseEntity.ok().body(dto);
    }

    private ExposingEditionDto createSingleExposingDto(Edition edition) {

        var dto = editionMapper.toNewExposingEditionDto(edition);
        var id = edition.getId();
        var pageable = PageRequest.of(0, 12);

        dto.add(linkTo(methodOn(this.getClass()).listAllEditions(pageable)).withRel("editions"));
        dto.add(linkTo(methodOn(SportEventController.class).listSportEventsFromEdition(id, pageable)).withRel("sportEvents"));
        return dto;
    }

    private Page<ExposingEditionDto> createExposingDtoPage(Page<Edition> editionsPage) {

        var editions = editionsPage.getContent();

        var dtos = editions.stream()
                .map(this::addSingleEditionLink)
                .toList();

        return new PageImpl<>(dtos, editionsPage.getPageable(), editions.size());
    }

    private ExposingEditionDto addSingleEditionLink(Edition edition) {

        var id = edition.getId();
        var dto = editionMapper.toNewExposingEditionDto(edition);
        var pageable = PageRequest.of(0, 12);

        dto.add(linkTo(methodOn(this.getClass()).findEditionById(id)).withSelfRel());
        dto.add(linkTo(methodOn(SportEventController.class).listSportEventsFromEdition(edition.getId(), pageable)).withRel("sportEvents"));
        return dto;
    }

}
