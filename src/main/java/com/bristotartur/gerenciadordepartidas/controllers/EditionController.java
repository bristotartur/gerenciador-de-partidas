package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.dtos.input.EditionDto;
import com.bristotartur.gerenciadordepartidas.dtos.exposing.ExposingEditionDto;
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

    @GetMapping
    public ResponseEntity<Page<ExposingEditionDto>> listAllEditions(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();

        log.info("Request to get Edition page of number '{}' and size '{}' was made.", number, size);

        var dtoPage = this.createExposingDtoPage(editionService.findAllEditions(pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExposingEditionDto> findEditionById(Long id) {

        log.info("Request to find Goal '{}' was made.", id);

        var edition = editionService.findEditionById(id);
        var dto = this.addEditionListLink(edition);

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<ExposingEditionDto> saveEdition(@RequestBody @Valid EditionDto editionDto) {

        log.info("Request to create Edition was made.");

        var edition = editionService.saveEdition(editionDto);
        var dto = this.addEditionListLink(edition);

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

        var edition = editionService.replaceEdition(id, editionDto);
        var dto = this.addEditionListLink(edition);

        return ResponseEntity.ok().body(dto);
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
        var dto = editionService.createExposingEditionDto(edition);

        dto.add(linkTo(methodOn(this.getClass()).findEditionById(id)).withSelfRel());
        return dto;
    }

    private ExposingEditionDto addEditionListLink(Edition edition) {

        var dto = editionService.createExposingEditionDto(edition);
        var pageRequest = PageRequest.of(0, 12);

        dto.add(linkTo(methodOn(this.getClass()).listAllEditions(pageRequest)).withRel("editions"));
        return dto;
    }


}
