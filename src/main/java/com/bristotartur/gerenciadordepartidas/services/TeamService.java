package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.TeamDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.TeamMapper;
import com.bristotartur.gerenciadordepartidas.repositories.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamService {

    private final TeamMapper teamMapper;
    private final TeamRepository teamRepository;

    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    public Team findTeamById( Long id) {

        var team = teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.TEAM_NOT_FOUND.message));

        return team;
    }

    public Team saveTeam(TeamDto teamDto) {

        var savedTeam = teamRepository.save(teamMapper.toNewTeam(teamDto));

        return savedTeam;
    }

    public void deleteTeamById(Long id) {
        teamRepository.deleteById(id);
    }

    @Transactional
    public Team replaceTeam(Long id, TeamDto teamDto) {

        this.findTeamById(id);

        var team = teamMapper.toExistingTeam(id, teamDto);
        var updatedTeam = teamRepository.save(team);

        return updatedTeam;
    }
}
