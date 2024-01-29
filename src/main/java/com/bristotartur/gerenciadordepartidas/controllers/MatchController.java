package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingMatchDto;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingParticipantDto;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.services.events.MatchService;
import com.bristotartur.gerenciadordepartidas.services.people.ParticipantService;
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
    private final ParticipantService participantService;

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
        var sport = Sports.findSportByValue(sportType);

        log.info("Request to get Match page of number '{}' and size '{}' with type '{}' was made.", number, size, sport);

        var dtos = this.createExposingDtoPage(matchService.findMatchesBySport(sport, pageable));
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping(path = "/{id}/players")
    public ResponseEntity<Page<ExposingParticipantDto>> listMatchPlayers(@PathVariable Long id, Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        log.info("Request to get Player page of number '{}' and size '{}' from Macth '{}' was made.", number, size, id);

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

        var match = matchService.findMatchById(id);
        var dto = this.addMatchListLink(match, PageRequest.of(0, 12));

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<ExposingMatchDto> saveMatch(@RequestBody @Valid MatchDto matchDto) {

        log.info("Request to create a new Match of type '{}' was made.", matchDto.sport());

        var match = matchService.saveMatch(matchDto);
        var dto = this.addMatchListLink(match, PageRequest.of(0, 12));

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

        var match = matchService.replaceMatch(id, matchDto);
        var dto = this.addMatchListLink(match, PageRequest.of(0, 12));

        return ResponseEntity.ok().body(dto);
    }

    private Page<ExposingMatchDto> createExposingDtoPage(Page<? extends Match> matchPage) {

        var matches = matchPage.getContent();
        var dtos = matches.stream()
                .map(this::addSingleMatchLink)
                .toList();

        return new PageImpl<>(dtos, matchPage.getPageable(), matchPage.getSize());
    }

    private ExposingMatchDto addSingleMatchLink(Match match) {

        var id = match.getId();
        var teamAId = match.getTeamA().getId();
        var teamBId = match.getTeamB().getId();

        var dto = matchService.createExposingMatchDto(match);
        var pageable = PageRequest.of(0, 12);

        dto.add(linkTo(methodOn(this.getClass()).findMatchById(id)).withSelfRel());
        dto.add(linkTo(methodOn(TeamController.class).findTeamById(teamAId)).withRel("team_a"));
        dto.add(linkTo(methodOn(TeamController.class).findTeamById(teamBId)).withRel("team_b"));
        dto.add(linkTo(methodOn(this.getClass()).listMatchPlayers(id,pageable)).withRel("match_players"));
        this.addExtraLinks(dto, match.getId(), pageable);

        return dto;
    }

    private ExposingMatchDto addMatchListLink(Match match, Pageable pageable) {

        var id = match.getId();
        var teamAId = match.getTeamA().getId();
        var teamBId = match.getTeamB().getId();

        var dto = matchService.createExposingMatchDto(match);
        var sport = dto.getSport().value;

        dto.add(linkTo(methodOn(this.getClass()).listAllMatches(pageable)).withRel("matches"));
        dto.add(linkTo(methodOn(TeamController.class).findTeamById(teamAId)).withRel("team_a"));
        dto.add(linkTo(methodOn(TeamController.class).findTeamById(teamBId)).withRel("team_b"));
        dto.add(linkTo(methodOn(this.getClass()).listMatchPlayers(id, pageable)).withRel("match_players"));
        dto.add(linkTo(methodOn(this.getClass()).listMatchesBySport(sport, pageable)).withRel("matches_of_same_type"));
        this.addExtraLinks(dto, match.getId(), pageable);

        return dto;
    }

    private ExposingParticipantDto addPlayerLink(Participant player, Long matchId) {

        var id = player.getId();
        var teamId = player.getTeam().getId();
        var dto = participantService.createExposingParticipantDto(player);

        dto.add(linkTo(methodOn(ParticipantController.class).findParticipantById(id)).withSelfRel());
        dto.add(linkTo(methodOn(TeamController.class).findTeamById(teamId)).withRel("team"));
        dto.add(linkTo(methodOn(this.getClass()).findMatchById(matchId)).withRel("match"));

        return dto;
    }

    private void addExtraLinks(ExposingMatchDto dto, Long matchId, Pageable pageable) {

        var sport = dto.getSport().value;

        if (sport.equals(Sports.FUTSAL.value) || sport.equals(Sports.HANDBALL.value)) {
            dto.add(linkTo(methodOn(GoalController.class).listGoalsFromMatch(matchId, sport, pageable)).withRel("goals"));
            // TODO link para cartões de penalidade
        }
    }

}
