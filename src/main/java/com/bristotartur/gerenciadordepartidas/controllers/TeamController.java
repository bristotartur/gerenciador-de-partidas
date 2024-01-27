package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingTeamDto;
import com.bristotartur.gerenciadordepartidas.dtos.TeamDto;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import com.bristotartur.gerenciadordepartidas.services.people.TeamService;
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
@RequestMapping(value = "/gerenciador-de-partidas/api/teams")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<Page<ExposingTeamDto>> listAllTeams(Pageable pageable) {

        var pageNumber = pageable.getPageNumber();
        var pageSize = pageable.getPageSize();

        log.info("Request to get Team page of number '{}' and size '{}' was made.", pageNumber, pageSize);

        var dtoPage = this.createExposingDtoPage(teamService.findAllTeams(pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExposingTeamDto> findTeamById(@PathVariable Long id) {

        log.info("Request to find Team '{}' was made.", id);

        var team = teamService.findTeamById(id);
        var dto = this.addTeamListLink(team, PageRequest.of(0, 5));

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(path = "/find")
    public ResponseEntity<ExposingTeamDto> findTeamByName(@RequestParam TeamName name) {

        log.info("Request to find Team with name '{}' was made.", name);

        var team = teamService.findTeamByName(name);
        var dto = this.addTeamListLink(team, PageRequest.of(0, 5));

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<ExposingTeamDto> saveTeam(@RequestBody @Valid TeamDto teamDto) {

        log.info("Request to create a new Team was made.");

        var team = teamService.saveTeam(teamDto);
        var dto = this.addTeamListLink(team, PageRequest.of(0, 5));

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ExposingTeamDto> replaceTeam(@PathVariable Long id,
                                                       @RequestBody @Valid TeamDto teamDto) {

        log.info("Request to update Team '{}' was made.", id);

        var team = teamService.replaceTeam(id, teamDto);
        var dto = this.addTeamListLink(team, PageRequest.of(0, 5));

        return ResponseEntity.ok().body(dto);
    }

    private Page<ExposingTeamDto> createExposingDtoPage(Page<Team> teamPage) {

        var teams = teamPage.getContent();
        var dtos = teams.stream()
                .map(this::addSingleTeamLink)
                .toList();

        return new PageImpl<>(dtos, teamPage.getPageable(), teamPage.getSize());
    }

    private ExposingTeamDto addSingleTeamLink(Team team) {

        var id = team.getId();
        var dto = teamService.createExposingTeamDto(team);

        dto.add(linkTo(methodOn(this.getClass()).findTeamById(id)).withSelfRel());
        return dto;
    }

    private ExposingTeamDto addTeamListLink(Team team, Pageable pageable) {

        var dto = teamService.createExposingTeamDto(team);

        dto.add(linkTo(methodOn(this.getClass()).listAllTeams(pageable)).withRel("teams"));
        return dto;
    }

}
