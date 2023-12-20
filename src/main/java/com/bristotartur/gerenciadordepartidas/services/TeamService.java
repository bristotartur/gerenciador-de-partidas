package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.TeamDto;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.TeamMapper;
import com.bristotartur.gerenciadordepartidas.repositories.TeamRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private static final String NOT_FOUND_MESSAGE = "Equipe não encontrada";

    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    public Team findTeamById(@PathVariable Long id) {

        var team = teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));

        return team;
    }

    public Team saveTeam(@RequestBody @Valid TeamDto teamDto) {

        var savedTeam = teamRepository.save(TeamMapper.INSTANCE.toNewTeam(teamDto));

        return savedTeam;
    }

    public void deleteTeamById(@PathVariable Long id) {
        teamRepository.deleteById(id);
    }

    @Transactional
    public Team replaceTeam(@PathVariable Long id, @RequestBody @Valid TeamDto teamDto) {

        this.findTeamById(id);

        var team = TeamMapper.INSTANCE.toExistingTeam(id, teamDto);
        var updatedTeam = teamRepository.save(team);

        return updatedTeam;
    }
}
