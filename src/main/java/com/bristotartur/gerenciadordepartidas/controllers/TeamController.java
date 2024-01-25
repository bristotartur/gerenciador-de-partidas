package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.dtos.TeamDto;
import com.bristotartur.gerenciadordepartidas.services.people.TeamService;
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
@RequestMapping(value = "/gerenciador-de-partidas/api/teams")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<List<Team>> findAllTeams() {

        log.info("Request to find all Teams was made.");

        List<Team> teamList = teamService.findAllTeams();

        if (teamList.isEmpty())
            return ResponseEntity.noContent().build();

        teamList.forEach(this::addSingleTeamLink);
        return ResponseEntity.ok().body(teamList);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Team> findTeamById(@PathVariable Long id) {

        log.info("Request to find Team '{}' was made.", id);

        var team = teamService.findTeamById(id);
        this.addTeamListLink(team);

        return ResponseEntity.ok().body(team);
    }


    @PostMapping
    public ResponseEntity<Team> saveTeam(@RequestBody @Valid TeamDto teamDto) {

        log.info("Request to create a new Team was made.");

        var team = teamService.saveTeam(teamDto);
        this.addTeamListLink(team);

        return ResponseEntity.status(HttpStatus.CREATED).body(team);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTeamById(@PathVariable Long id) {

        log.info("Request to delete Team '{}' was made.", id);

        teamService.deleteTeamById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Team> replaceTeam(@PathVariable Long id,
                                            @RequestBody @Valid TeamDto teamDto) {

        log.info("Request to update Team '{}' was made.", id);

        var team = teamService.replaceTeam(id, teamDto);
        this.addTeamListLink(team);

        return ResponseEntity.ok().body(team);
    }

    private void addSingleTeamLink(Team team) {

        var id = team.getId();
        team.add(linkTo(methodOn(this.getClass()).findTeamById(id)).withSelfRel());
    }

    private void addTeamListLink(Team team) {
        team.add(linkTo(methodOn(this.getClass()).findAllTeams()).withRel("Team list"));
    }

}
