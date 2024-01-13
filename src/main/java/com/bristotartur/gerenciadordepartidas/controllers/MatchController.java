package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
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
@RequestMapping(value = "/api/matches")
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
    }

    private void addMatchListLink(Match match) {

        var teamAId = match.getTeamA().getId();
        var teamBId = match.getTeamB().getId();

        match.add(linkTo(methodOn(this.getClass()).findAllMatches()).withRel("Match list"));
        match.add(linkTo(methodOn(TeamController.class).findTeamById(teamAId)).withRel("Team A"));
        match.add(linkTo(methodOn(TeamController.class).findTeamById(teamBId)).withRel("Team B"));
    }

}
