package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.TeamDto;
import com.bristotartur.gerenciadordepartidas.services.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<List<Team>> findAllTeams() {
        return ResponseEntity.ok()
                .body(teamService.findAllTeams());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Team> findTeamById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(teamService.findTeamById(id));
    }


    @PostMapping
    public ResponseEntity<Team> saveTeam(@RequestBody @Valid TeamDto teamDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teamService.saveTeam(teamDto));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTeamById(@PathVariable Long id) {

        teamService.deleteTeamById(id);

        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PutMapping(path = "/{id}")
    public ResponseEntity<Team> replaceTeam(@PathVariable Long id,
                                            @RequestBody @Valid TeamDto teamDto) {
        return ResponseEntity.ok()
                .body(teamService.replaceTeam(id, teamDto));
    }
}
