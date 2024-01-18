package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.domain.participant.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.services.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public ResponseEntity<List<Match>> findAllMatches() {

        List<Match> matchList = matchService.findAllMatches();

        if (matchList.isEmpty())
            return ResponseEntity.noContent().build();

        matchList.forEach(this::addSingleMatchLink);
        return ResponseEntity.ok().body(matchList);
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<? extends Match>> findMatchesBySport(@RequestParam Sports sport) {

        List<? extends Match> matchList = matchService.findMatchesBySport(sport);

        if (matchList.isEmpty())
            return ResponseEntity.noContent().build();

        matchList.forEach(this::addSingleMatchLink);
        return ResponseEntity.ok().body(matchList);
    }

    @GetMapping(path = "/{id}/players")
    public ResponseEntity<List<Participant>> findAllMatchPlayers(@PathVariable Long id) {

        var players = matchService.findAllMatchPlayers(id);

        players.forEach(player -> addPlayerLink(player, id));
        return ResponseEntity.ok().body(players);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Match> findMatchById(@PathVariable Long id) {

        var match = matchService.findMatchById(id);

        this.addMatchListLink(match);
        return ResponseEntity.ok().body(match);
    }

    @PostMapping
    public ResponseEntity<Match> saveMatch(@RequestBody @Valid MatchDto matchDto) {

        var match = matchService.saveMatch(matchDto);

        this.addMatchListLink(match);
        return ResponseEntity.status(HttpStatus.CREATED).body(match);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {

        matchService.deleteMatchById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Match> replaceMatch(@PathVariable Long id,
                                              @RequestBody @Valid MatchDto matchDto) {

        var match = matchService.replaceMatch(id, matchDto);

        this.addMatchListLink(match);
        return ResponseEntity.ok().body(match);
    }

    private void addSingleMatchLink(Match match) {

        var id = match.getId();
        var teamAId = match.getTeamA().getId();
        var teamBId = match.getTeamB().getId();

        match.add(linkTo(methodOn(this.getClass()).findMatchById(id)).withSelfRel());
        match.add(linkTo(methodOn(TeamController.class).findTeamById(teamAId)).withRel("Team A"));
        match.add(linkTo(methodOn(TeamController.class).findTeamById(teamBId)).withRel("Team B"));
        match.add(linkTo(methodOn(this.getClass()).findAllMatchPlayers(id)).withRel("Match players list"));
    }

    private void addMatchListLink(Match match) {

        var id = match.getId();
        var teamAId = match.getTeamA().getId();
        var teamBId = match.getTeamB().getId();

        match.add(linkTo(methodOn(this.getClass()).findAllMatches()).withRel("Match list"));
        match.add(linkTo(methodOn(TeamController.class).findTeamById(teamAId)).withRel("Team A"));
        match.add(linkTo(methodOn(TeamController.class).findTeamById(teamBId)).withRel("Team B"));
        match.add(linkTo(methodOn(this.getClass()).findAllMatchPlayers(id)).withRel("Match players list"));
    }

    private void addPlayerLink(Participant player, Long matchId) {

        Long id = player.getId();
        Long teamId = player.getTeam().getId();

        player.add(linkTo(methodOn(ParticipantController.class).findParticipantById(id)).withSelfRel());
        player.add(linkTo(methodOn(TeamController.class).findTeamById(teamId)).withRel("Team"));
        player.add(linkTo(methodOn(this.getClass()).findMatchById(matchId)).withRel("Match"));
    }

}
