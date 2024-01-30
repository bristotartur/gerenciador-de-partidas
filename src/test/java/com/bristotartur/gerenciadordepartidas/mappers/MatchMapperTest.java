package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import com.bristotartur.gerenciadordepartidas.utils.MatchTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.TeamTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MatchMapperTest {

    @Autowired
    private MatchMapper matchMapper;

    @Test
    @DisplayName("Should convert scores fields to zero when mapped to new Match")
    void Should_ConvertScoresToZero_When_MappedToNewMatch() {

        var matchDto = MatchTestUtil.createNewMatchDto(Sports.CHESS, any(), any(), any());
        var result = matchMapper.toNewMatch(matchDto, any(), any(), any());

        assertEquals(result.getTeamScoreA(), 0);
        assertEquals(result.getTeamScoreB(), 0);
    }

    @Test
    @DisplayName("Should map entities to their referent fields in Match when they are passed to new Match")
    void Should_MapEntitiesToTheirReferentFieldsInMatch_When_TheyArePassedToNewMatch() {

        var teamA = TeamTestUtil.createNewTeam(TeamName.ATOMICA);
        var teamB = TeamTestUtil.createNewTeam(TeamName.MESTRES_DE_OBRAS);
        var matchDto = MatchTestUtil.createNewMatchDto(Sports.VOLLEYBALL, any(), any(), any());

        var result = matchMapper.toNewMatch(matchDto, any(), teamA, teamB);

        assertEquals(result.getTeamA(), teamA);
        assertEquals(result.getTeamB(), teamB);
    }

    @Test
    @DisplayName("Should update Match when new values are passed")
    void Should_UpdateMatch_When_NewValuesArePassed() {

        var sport = Sports.HANDBALL;
        var teamA = TeamTestUtil.createNewTeam(TeamName.PAPA_LEGUAS);
        var teamB = TeamTestUtil.createNewTeam(TeamName.TWISTER);
        var teamC = TeamTestUtil.createNewTeam(TeamName.UNICONTTI);

        var match = MatchTestUtil.createNewMatch(teamA, teamB, any());
        var matchDto = MatchTestUtil.createNewMatchDto(Sports.HANDBALL, any(), any(), any());

        var result = matchMapper.toExistingMatch(getRandomLongId(), matchDto, any(), teamA, teamC);

        assertNotEquals(result, match);
    }

    @Test
    @DisplayName("Should map Match fields to their equivalent fields in ExposingMatchDto when Match passed to map")
    void Should_MapMatchFieldsToTheirEquivalentFieldsInExposingMatchDto_When_MatchIsPassedToMap() {

        var teamA = TeamTestUtil.createNewTeam(TeamName.PAPA_LEGUAS);
        var teamB = TeamTestUtil.createNewTeam(TeamName.MESTRES_DE_OBRAS);
        var match = MatchTestUtil.createNewMatch(teamA, teamB, any());

        var sport = Sports.FUTSAL;
        var result = matchMapper.toNewExposingMatchDto(match, sport);

        assertEquals(result.getTeamA(),teamA.getName());
        assertEquals(result.getTeamB(),teamB.getName());
        assertEquals(result.getSport(), sport);
    }

}