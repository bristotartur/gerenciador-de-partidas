package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.controllers.docs.SportEventOperations;
import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.dtos.exposing.ExposingSportEventDto;
import com.bristotartur.gerenciadordepartidas.dtos.input.SportEventDto;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.mappers.SportEventMapper;
import com.bristotartur.gerenciadordepartidas.services.events.SportEventService;
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
@RequestMapping("/gerenciador-de-partidas/api/sport-events")
@RequiredArgsConstructor
@Transactional
@Slf4j
@Tag(name = "Sport Event")
public class SportEventController {

    private final SportEventService sportEventService;
    private final SportEventMapper sportEventMapper;

    @SportEventOperations.ListAllSportEventsOperation
    @GetMapping
    public ResponseEntity<Page<ExposingSportEventDto>> listAllSportEvents(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();

        log.info("Request to get SportEvent page of number '{}' and size '{}' was made.", number, size);

        var dtoPage = this.createExposingDtoPage(sportEventService.findAllEvents(pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @SportEventOperations.ListSportEventsFromEditionOperation
    @GetMapping(path = "/from")
    public ResponseEntity<Page<ExposingSportEventDto>> listSportEventsFromEdition(@RequestParam("edition") Long editionId,
                                                                                  Pageable pageable) {
        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();

        log.info("Request to get SportEvent page of number '{}' and size '{}' from Edition '{}' was made.", number, size, editionId);

        var dtoPage = this.createExposingDtoPage(sportEventService.findAllEventsFromEdition(editionId, pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @SportEventOperations.ListSportEventsBySportOperation
    @GetMapping(path = "/list")
    public ResponseEntity<Page<ExposingSportEventDto>> listSportEventsBySport(@RequestParam("sport") String sportType,
                                                                             Pageable pageable) {
        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var sport = Sports.findSportLike(sportType);

        log.info("Request to get SportEvent page of number '{}' and size '{}' of type '{}' was made.", number, size, sport);

        var dtoPage = this.createExposingDtoPage(sportEventService.findAllEventsOfType(sport, pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @SportEventOperations.FindSportEventByIdOperation
    @GetMapping(path = "/{id}")
    public ResponseEntity<ExposingSportEventDto> findSportEventById(@PathVariable Long id) {

        log.info("Request to find SportEvent '{}' was made.", id);

        var dto = this.createSingleExposingDto(sportEventService.findEventById(id));
        return ResponseEntity.ok().body(dto);
    }

    @SportEventOperations.SaveSportEventOperation
    @PostMapping
    public ResponseEntity<ExposingSportEventDto> saveSportEvent(@RequestBody @Valid SportEventDto sportEventDto) {

        log.info("Request to create SportEvent was made.");

        var dto = this.createSingleExposingDto(sportEventService.saveEvent(sportEventDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @SportEventOperations.DeleteSportEventOperation
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteSportEvent(@PathVariable Long id) {

        log.info("Request to delete SportEvent '{}' was made", id);

        sportEventService.deleteEventById(id);
        return ResponseEntity.noContent().build();
    }

    @SportEventOperations.ReplaceSportEventOperation
    @PutMapping(path = "/{id}")
    public ResponseEntity<ExposingSportEventDto> replaceSportEvent(@PathVariable Long id,
                                                                   @RequestBody @Valid SportEventDto sportEventDto) {
        log.info("Request to update SportEvent '{}' was made.", id);

        var dto = this.createSingleExposingDto(sportEventService.replaceEvent(id, sportEventDto));
        return ResponseEntity.ok().body(dto);
    }

    @SportEventOperations.UpdateSportEventStatusOperation
    @PutMapping(path = "/{id}/update")
    public ResponseEntity<ExposingSportEventDto> updateSportEventStatus(@PathVariable Long id,
                                                                        @RequestParam("status") String eventStatus) {
        var status = Status.findStatusLike(eventStatus);
        log.info("Request to update SportEvent '{}' to status '{}' was made.", id, status);

        var dto = this.createSingleExposingDto(sportEventService.updateEventStatus(id, status));
        return ResponseEntity.ok().body(dto);
    }

    private ExposingSportEventDto createSingleExposingDto(SportEvent sportEvent) {

        var editionId = sportEvent.getId();
        var dto = sportEventMapper.toNewExposingSportEventDto(sportEvent);

        var pageable = PageRequest.of(0, 12);

        dto.add(linkTo(methodOn(this.getClass()).listAllSportEvents(pageable)).withRel("sportEventList"));
        dto.add(linkTo(methodOn(EditionController.class).findEditionById(editionId)).withRel("edition"));
        return dto;
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
        var dto = sportEventMapper.toNewExposingSportEventDto(sportEvent);

        dto.add(linkTo(methodOn(this.getClass()).findSportEventById(id)).withSelfRel());
        dto.add(linkTo(methodOn(EditionController.class).findEditionById(editionId)).withRel("edition"));
        return dto;
    }

}
