package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.dtos.exposing.ExposingSportEventDto;
import com.bristotartur.gerenciadordepartidas.dtos.input.SportEventDto;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.services.events.SportEventService;
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
@RequestMapping("/gerenciador-de-partidas/api/sport-events")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SportEventController {

    private final SportEventService sportEventService;

    @GetMapping
    public ResponseEntity<Page<ExposingSportEventDto>> listAllSportEvents(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();

        log.info("Request to get SportEvent page of number '{}' and size '{}' was made.", number, size);

        var dtoPage = this.createExposingDtoPage(sportEventService.findAllEvents(pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @GetMapping(path = "/from")
    public ResponseEntity<Page<ExposingSportEventDto>> listSportEventsFromEdition(@RequestParam("edition") Long editionId,
                                                                                  Pageable pageable) {
        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();

        log.info("Request to get SportEvent page of number '{}' and size '{}' from Edition '{}' was made.", number, size, editionId);

        var dtoPage = this.createExposingDtoPage(sportEventService.findAllEventsFromEdition(editionId, pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @GetMapping(path = "/list")
    public ResponseEntity<Page<ExposingSportEventDto>> listSportEventsbySport(@RequestParam String sportType,
                                                                             Pageable pageable) {
        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var sport = Sports.findSportLike(sportType);

        log.info("Request to get SportEvent page of number '{}' and size '{}' of type '{}' was made.", number, size, sport);

        var dtoPage = this.createExposingDtoPage(sportEventService.findAllEventsOfType(sport, pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExposingSportEventDto> findSportEventById(@PathVariable Long id) {

        log.info("Request to find SportEvent '{}' was made.", id);

        var dto = this.addSportEventListLink(sportEventService.findEventById(id));
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<ExposingSportEventDto> saveSportEvent(@RequestBody @Valid SportEventDto sportEventDto) {

        log.info("Request to create SportEvent was made.");

        var dto = this.addSportEventListLink(sportEventService.saveEvent(sportEventDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteSportEvent(@PathVariable Long id) {

        log.info("Request to delete SportEvent '{}' was made", id);

        sportEventService.deleteEventById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ExposingSportEventDto> replaceSportEvent(@PathVariable Long id,
                                                                   @RequestBody @Valid SportEventDto sportEventDto) {
        log.info("Request to update SportEvent '{}' was made.", id);

        var dto = this.addSportEventListLink(sportEventService.replaceEvent(id, sportEventDto));
        return ResponseEntity.ok().body(dto);
    }

    private Page<ExposingSportEventDto> createExposingDtoPage(Page<SportEvent> sportEventPage) {

        var events = sportEventPage.getContent();
        var dtos = events.stream()
                .map(this::addSingleSportEventLink)
                .toList();

        return new PageImpl<>(dtos, sportEventPage.getPageable(), events.size());
    }

    private ExposingSportEventDto addSingleSportEventLink(SportEvent sportEvent) {

        var id = sportEvent.getId();
        var editionId = sportEvent.getEdition().getId();
        var dto = (ExposingSportEventDto) sportEventService.createExposingEventDto(sportEvent);

        var pageable = PageRequest.of(0, 12);

        dto.add(linkTo(methodOn(this.getClass()).findSportEventById(id)).withSelfRel());
        dto.add(linkTo(methodOn(EditionController.class).findEditionById(editionId)).withRel("edition"));
        return dto;
    }

    private ExposingSportEventDto addSportEventListLink(SportEvent sportEvent) {

        var id = sportEvent.getId();
        var editionId = sportEvent.getId();
        var dto = (ExposingSportEventDto) sportEventService.createExposingEventDto(sportEvent);

        var pageable = PageRequest.of(0, 12);

        dto.add(linkTo(methodOn(this.getClass()).listAllSportEvents(pageable)).withRel("sportEventList"));
        dto.add(linkTo(methodOn(EditionController.class).findEditionById(editionId)).withRel("edition"));
        return dto;
    }

}
