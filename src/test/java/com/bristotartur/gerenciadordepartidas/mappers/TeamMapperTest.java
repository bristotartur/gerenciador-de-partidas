package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.TeamDto;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TeamMapperTest {

    @Autowired
    private TeamMapper teamMapper;
    private TeamDto teamDtoA;
    private TeamDto teamDtoB;
    private Team existingTeam;

    @BeforeEach
    void setUp() {

        teamDtoA = new TeamDto(TeamName.ATOMICA, 1000);
        teamDtoB = new TeamDto(TeamName.PAPA_LEGUAS, 1500);

        existingTeam = Team.builder()
                .id(1L)
                .name(TeamName.PAPA_LEGUAS.name)
                .points(1500)
                .build();
    }

    @Test
    @DisplayName("Should map parsed enum value to String field when a valid enum is passed")
    void Should_MapParsedEnumValue_When_ValidEnumIsPassed() {

        var team = teamMapper.toNewTeam(teamDtoA);

        assertThat(team.getName()).isEqualTo(teamDtoA.teamName().name);
    }

    @Test
    @DisplayName("Should map points field to 0 when TeamDto is mapped to a new team")
    void Should_MapPointsFieldTo0_When_TeamDtoIsMappedToNewTeam() {

        var team = teamMapper.toNewTeam(teamDtoA);

        assertThat(team.getPoints()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should update Team fields when TeamDto with different values is passed")
    void Should_UpdateTeamFields_When_TeamDtoWithDifferentValuesIsPassed() {

        var team = teamMapper.toExistingTeam(1L, teamDtoA);

        assertThat(team.getName()).isNotEqualTo(existingTeam.getName());
        assertThat(team.getPoints()).isNotEqualTo(existingTeam.getPoints());
    }

    @Test
    @DisplayName("Should not update Team fields when TeamDto with the same values is passed")
    void Should_UpdateTeamFields_When_TeamDtoWithSameValuesIsPassed() {

        var team = teamMapper.toExistingTeam(1L, teamDtoB);

        assertThat(team.getPoints()).isEqualTo(existingTeam.getPoints());
        assertThat(team.getName()).isEqualTo(existingTeam.getName());
    }

}