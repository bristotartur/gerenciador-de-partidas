package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.TeamDto;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TeamMapperTest {

    @Autowired
    private TeamMapper teamMapper;

    private TeamDto teamDto1;
    private TeamDto teamDto2;
    private Team existingTeam;

    @BeforeEach
    void setUp() {

        teamDto1 = new TeamDto(TeamName.ATOMICA, 1000);
        teamDto2 = new TeamDto(TeamName.PAPA_LEGUAS, 1500);

        existingTeam = Team.builder()
                .id(1L)
                .name(TeamName.PAPA_LEGUAS.name)
                .points(1500)
                .build();
    }

    @Test
    @DisplayName("Should map parsed enum value to String field when a valid enum is passed")
    void Should_MapParsedEnumValue_When_ValidEnumIsPassed() {

        var team = teamMapper.toNewTeam(teamDto1);

        assertThat(team.getName()).isEqualTo(teamDto1.teamName().name);
    }

    @Test
    @DisplayName("Should map points field to 0 when TeamDto is mapped to a new team")
    void Should_MapPointsFieldTo0_When_TeamDtoIsMappedToNewTeam() {

        var team = teamMapper.toNewTeam(teamDto1);

        assertThat(team.getPoints()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should update Team fields when TeamDto with different values is passed")
    void Should_UpdateTeamFields_When_TeamDtoWithDifferentValuesIsPassed() {

        var team = teamMapper.toExistingTeam(1L, teamDto1);

        assertThat(team.getName()).isNotEqualTo(existingTeam.getName());
        assertThat(team.getPoints()).isNotEqualTo(existingTeam.getPoints());
    }

    @Test
    @DisplayName("Should not update Team fields when TeamDto with the same values is passed")
    void Should_UpdateTeamFields_When_TeamDtoWithSameValuesIsPassed() {

        var team = teamMapper.toExistingTeam(1L, teamDto2);

        assertThat(team.getPoints()).isEqualTo(existingTeam.getPoints());
        assertThat(team.getName()).isEqualTo(existingTeam.getName());
    }

}