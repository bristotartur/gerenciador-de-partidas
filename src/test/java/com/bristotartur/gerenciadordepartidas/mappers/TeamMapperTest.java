package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import com.bristotartur.gerenciadordepartidas.utils.TeamTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class TeamMapperTest {

    @Autowired
    private TeamMapper teamMapper;

    @Test
    @DisplayName("Should map TeamName value when a TeamName is passed")
    void Should_MapTeamNameValue_When_TeamNameIsPassed() {

        var teamDto = TeamTestUtil.createNewTeamDto(TeamName.ATOMICA, 800);
        var result = teamMapper.toNewTeam(teamDto);

        assertEquals(result.getName(), teamDto.teamName().value);
    }

    @Test
    @DisplayName("Should map points field to 0 when TeamDto is mapped to a new team")
    void Should_MapPointsFieldTo0_When_TeamDtoIsMappedToNewTeam() {

        var teamDto = TeamTestUtil.createNewTeamDto(TeamName.PAPA_LEGUAS, 1000);
        var result = teamMapper.toNewTeam(teamDto);

        assertEquals(result.getPoints(),0);
    }

    @Test
    @DisplayName("Should update Team fields when TeamDto with different values is passed")
    void Should_UpdateTeamFields_When_TeamDtoWithDifferentValuesIsPassed() {

        var team = TeamTestUtil.createNewTeam(TeamName.MESTRES_DE_OBRAS);
        team.setPoints(500);

        var teamDto = TeamTestUtil.createNewTeamDto(TeamName.TWISTER, 800);
        var result = teamMapper.toExistingTeam(1L, teamDto);

        assertNotEquals(result.getName(), team.getName());
        assertNotEquals(result.getPoints(), team.getPoints());
    }

    @Test
    @DisplayName("Should not update Team fields when TeamDto with the same values is passed")
    void Should_UpdateTeamFields_When_TeamDtoWithSameValuesIsPassed() {

        var team = TeamTestUtil.createNewTeam(TeamName.UNICONTTI);
        team.setPoints(600);

        var teamDto = TeamTestUtil.createNewTeamDto(TeamName.UNICONTTI, 600);
        var result = teamMapper.toExistingTeam(1L, teamDto);

        assertEquals(result.getPoints(), team.getPoints());
        assertEquals(result.getName(), team.getName());
    }

}