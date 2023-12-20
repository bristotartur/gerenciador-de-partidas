package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.Match;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.TeamDto;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// TODO - atualizar a classe de testes de TeamMapper para testar a sobrecarga do método toExistingTeam()

class TeamMapperTest {

    private TeamMapper teamMapper = TeamMapper.INSTANCE;

    private TeamDto teamDto;
    private Team existingTeam1;
    private Team existingTeam2;
    private Match match1;
    private Match match2;

    @BeforeEach
    void setUp() {

        match1 = Match.builder().build();
        match2 = Match.builder().build();
        teamDto = new TeamDto(TeamName.ATOMICA, 1000);
        existingTeam1 = new Team(1L, "papa-léguas", 1500, List.of(match1), List.of(match2));
        existingTeam2 = new Team(2L, "twister", 300, List.of(match2), List.of(match1));
    }

    @Test
    @DisplayName("Should map parsed enum value to String field when a valid enum is passed")
    void Should_MapParsedEnumValue_When_ValidEnumIsPassed() {

        var team = teamMapper.toNewTeam(teamDto);

        assertThat(team.getName()).isEqualTo(teamDto.teamName().name);
    }

    @Test
    @DisplayName("Should map points field to 0 when TeamDto is mapped to a new team")
    void Should_MapPointsFieldTo0_When_TeamDtoIsMappedToNewTeam() {

        var team = teamMapper.toNewTeam(teamDto);

        assertThat(team.getPoints()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should update Team name field when TeamDto with different name is passed")
    void Should_UpdateTeamNameField_When_TeamDtoWithDifferentNameIsPassed() {

        var team = teamMapper.toExistingTeam(1L, teamDto);

        assertThat(team.getName()).isNotEqualTo(existingTeam1.getName());
    }

    @Test
    @DisplayName("Should update Team points field when TeamDto with different points is passed")
    void Should_UpdateTeamPointsField_When_TeamDtoWithDifferentPointsIsPassed() {

        var team = teamMapper.toExistingTeam(2L, teamDto);

        assertThat(team.getPoints()).isNotEqualTo(existingTeam2.getPoints());
    }
}