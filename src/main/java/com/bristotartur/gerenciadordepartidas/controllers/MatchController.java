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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/gerenciador-de-partidas/api/matches")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MatchController {

    private final MatchService matchService;
    private final ParticipantService participantService;

    @GetMapping
    public ResponseEntity<List<ExposingMatchDto>> findAllMatches() {

        log.info("Request to find all Matches was made.");

        List<Match> matchList = matchService.findAllMatches();

        var dtos = matchList.stream()
                .map(this::addSingleMatchLink)
                .toList();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<ExposingMatchDto>> findMatchesBySport(@RequestParam Sports sport) {

        log.info("Request to find all Matches of type '{}' was made.", sport);

        List<? extends Match> matchList = matchService.findMatchesBySport(sport);

        var dtos = matchList.stream()
                .map(this::addSingleMatchLink)
                .toList();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping(path = "/{id}/players")
    public ResponseEntity<List<ExposingParticipantDto>> findAllMatchPlayers(@PathVariable Long id) {

        log.info("Request to find all players from Macth '{}' was made.", id);

        var players = matchService.findAllMatchPlayers(id);
        var dtos = players.stream()
                .map(player -> this.addPlayerLink(player, id))
                .toList();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExposingMatchDto> findMatchById(@PathVariable Long id) {

        log.info("Request to find Match '{}' was made.", id);

        var match = matchService.findMatchById(id);
        var dto = addMatchListLink(match);

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<ExposingMatchDto> saveMatch(@RequestBody @Valid MatchDto matchDto) {

        log.info("Request to create a new Match of type '{}' was made.", matchDto.sport());

        var match = matchService.saveMatch(matchDto);
        var dto = addMatchListLink(match);

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
        var dto = addMatchListLink(match);

        return ResponseEntity.ok().body(dto);
    }

    private ExposingMatchDto addSingleMatchLink(Match match) {

        var id = match.getId();
        var teamAId = match.getTeamA().getId();
        var teamBId = match.getTeamB().getId();

        var dto = matchService.createExposingMatchDto(match);

        dto.add(linkTo(methodOn(this.getClass()).findMatchById(id)).withSelfRel());
        dto.add(linkTo(methodOn(TeamController.class).findTeamById(teamAId)).withRel("team_a"));
        dto.add(linkTo(methodOn(TeamController.class).findTeamById(teamBId)).withRel("team_b"));
        dto.add(linkTo(methodOn(this.getClass()).findAllMatchPlayers(id)).withRel("match_players"));

        return dto;
    }

    private ExposingMatchDto addMatchListLink(Match match) {

        var id = match.getId();
        var teamAId = match.getTeamA().getId();
        var teamBId = match.getTeamB().getId();

        var dto = matchService.createExposingMatchDto(match);

        dto.add(linkTo(methodOn(this.getClass()).findAllMatches()).withRel("matches"));
        dto.add(linkTo(methodOn(TeamController.class).findTeamById(teamAId)).withRel("team_a"));
        dto.add(linkTo(methodOn(TeamController.class).findTeamById(teamBId)).withRel("team_b"));
        dto.add(linkTo(methodOn(this.getClass()).findAllMatchPlayers(id)).withRel("match_playes"));

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

}
