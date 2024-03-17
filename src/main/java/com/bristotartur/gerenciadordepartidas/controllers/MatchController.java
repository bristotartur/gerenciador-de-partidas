package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.exposing.ExposingMatchDto;
import com.bristotartur.gerenciadordepartidas.dtos.exposing.ExposingParticipantDto;
import com.bristotartur.gerenciadordepartidas.dtos.input.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.mappers.ParticipantMapper;
import com.bristotartur.gerenciadordepartidas.services.matches.MatchService;
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
@RequestMapping("/gerenciador-de-partidas/api/matches")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MatchController {

    private final MatchService matchService;
    private final ParticipantMapper participantMapper;

    @GetMapping
    public ResponseEntity<Page<ExposingMatchDto>> listAllMatches(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        log.info("Request to get Match page of number '{}' and size '{}' was made.", number, size);

        var dtos = this.createExposingDtoPage(matchService.findAllMatches(pageable));
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping(path = "/list")
    public ResponseEntity<Page<ExposingMatchDto>> listMatchesBySport(@RequestParam String sportType, Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var sport = Sports.findSportLike(sportType);

        log.info("Request to get Match page of number '{}' and size '{}' with type '{}' was made.", number, size, sport);

        var dtos = this.createExposingDtoPage(matchService.findMatchesBySport(sport, pageable));
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping(path = "/from")
    public ResponseEntity<Page<ExposingMatchDto>> listMatchesFromSportEvent(@RequestParam("sport-event") Long sportEventId,
                                                                            Pageable pageable) {
        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        log.info("Request to get Match page of number '{}' and size '{}' from SportEvent '{}' was made.", number, size, sportEventId);

        var dtos = this.createExposingDtoPage(matchService.findMatchesBySportEvent(sportEventId, pageable));
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping(path = "/{id}/players")
    public ResponseEntity<Page<ExposingParticipantDto>> listMatchPlayers(@PathVariable Long id, Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        log.info("Request to get Player page of number '{}' and size '{}' from Match '{}' was made.", number, size, id);

        var players = matchService.findAllMatchPlayers(id, pageable);
        var dtos = players.getContent().stream()
                .map(player -> this.addPlayerLink(player, id))
                .toList();

        var dtoPage = new PageImpl<>(dtos, pageable, dtos.size());
        return ResponseEntity.ok().body(dtoPage);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExposingMatchDto> findMatchById(@PathVariable Long id) {

        log.info("Request to find Match '{}' was made.", id);

        var dto = this.createSingleExposingDto(matchService.findMatchById(id));
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<ExposingMatchDto> saveMatch(@RequestBody @Valid MatchDto matchDto) {

        log.info("Request to create a new Match of type '{}' was made.", matchDto.sport());

        var dto = this.createSingleExposingDto(matchService.saveMatch(matchDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {

        log.info("Request to delete Match '{}' was made.", id);

        matchService.deleteMatchById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ExposingMatchDto> replaceMatch(@PathVariable Long id,
                                                         @RequestBody @Valid MatchDto matchDto) {

        log.info("Request to update Match '{}' of type '{}' was made.", id, matchDto.sport());

        var dto = this.createSingleExposingDto(matchService.replaceMatch(id, matchDto));
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping(path = "/{id}/update")
    public ResponseEntity<ExposingMatchDto> updateMatchStatus(@PathVariable Long id,
                                                              @RequestParam("status") String matchStatus) {

        var status = Status.findStatusLike(matchStatus);
        log.info("Request to update Match '{}' to status '{}' was made.", id, status);

        var dto = this.createSingleExposingDto(matchService.updateMatchStatus(id, status));
        return ResponseEntity.ok().body(dto);
    }

    private ExposingMatchDto createSingleExposingDto(Match match) {

        var id = match.getId();

        var dto = matchService.createExposingMatchDto(match);
        var sport = dto.getSport().value;
        var pageable = PageRequest.of(0, 12);

        dto.add(linkTo(methodOn(this.getClass()).listAllMatches(pageable)).withRel("matches"));
        dto.add(linkTo(methodOn(this.getClass()).listMatchPlayers(id, pageable)).withRel("matchPlayers"));
        dto.add(linkTo(methodOn(this.getClass()).listMatchesBySport(sport, pageable)).withRel("matchesOfSameTpe"));
        this.addExtraLinks(dto, match.getId(), pageable);

        return dto;
    }

    public Page<ExposingMatchDto> createExposingDtoPage(Page<? extends Match> matchPage) {

        var matches = matchPage.getContent();
        var dtos = matches.stream()
                .map(this::addSingleMatchLink)
                .toList();

        return new PageImpl<>(dtos, matchPage.getPageable(), matchPage.getSize());
    }

    private ExposingMatchDto addSingleMatchLink(Match match) {

        var id = match.getId();

        var dto = matchService.createExposingMatchDto(match);
        var pageable = PageRequest.of(0, 12);

        dto.add(linkTo(methodOn(this.getClass()).findMatchById(id)).withSelfRel());
        dto.add(linkTo(methodOn(this.getClass()).listMatchPlayers(id,pageable)).withRel("matchPlayers"));
        dto.add(linkTo(methodOn(this.getClass()).listMatchesBySport(dto.getSport().name(), pageable)).withRel("matchesOfSameType"));
        this.addExtraLinks(dto, match.getId(), pageable);

        return dto;
    }

    private ExposingParticipantDto addPlayerLink(Participant player, Long matchId) {

        var id = player.getId();
        var dto = participantMapper.toNewExposingParticipantDto(player);

        dto.add(linkTo(methodOn(ParticipantController.class).findParticipantById(id)).withSelfRel());
        dto.add(linkTo(methodOn(this.getClass()).findMatchById(matchId)).withRel("match"));

        return dto;
    }

    private void addExtraLinks(ExposingMatchDto dto, Long matchId, Pageable pageable) {

        var sport = dto.getSport();

        if (sport.equals(Sports.FUTSAL) || sport.equals(Sports.HANDBALL)) {
            dto.add(linkTo(methodOn(GoalController.class).listGoalsFromMatch(matchId, sport.value, pageable)).withRel("goals"));
            // TODO link para cartões de penalidade
        }
    }

}
