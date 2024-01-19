package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.utils.EntityTestUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.EntityTestUtil.createNewMatchDto;
import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
class MatchMapperTest {

    @Autowired
    private MatchMapper matchMapper;
    @Autowired
    private EntityManager entityManager;

    private List<Participant> createNewPlayerList() {

        List<Participant> players = new LinkedList<>();

        for (int i = 0; i < 10; i++) {

            players.add(Participant.builder()
                    .name("sa")
                    .classNumber("2-53")
                    .team(createNewTeam())
                    .build());
        }
        return players;
    }

    private Team createNewTeam() {

        return Team.builder()
                .id(getRandomLongId())
                .points(300)
                .build();
    }

    @Test
    @DisplayName("Should convert scores fields to zero when mapped to new Match")
    void Should_ConvertScoresToZero_When_MappedToNewMatch() {

        var matchDto = createNewMatchDto(Sports.CHESS, entityManager);
        var match = matchMapper.toNewMatch(matchDto, any(), any(), any());

        assertEquals(match.getTeamScoreA(), 0);
        assertEquals(match.getTeamScoreB(), 0);
    }

    @Test
    @DisplayName("Should map entities to their referent fields in Match when they are passed to new Match")
    void Should_MapEntitiesToTheirReferentFieldsInMatch_When_TheyArePassedToNewMatch() {

        var sport = Sports.VOLLEYBALL;
        var players = createNewPlayerList();
        var teamA = createNewTeam();
        var teamB = createNewTeam();
        var matchDto = createNewMatchDto(sport, entityManager);

        var match = matchMapper.toNewMatch(matchDto, players, teamA, teamB);

        assertEquals(match.getTeamA(), teamA);
        assertEquals(match.getTeamB(), teamB);
    }

    @Test
    @DisplayName("Should update Match when new values are passed")
    void Should_UpdateMatch_When_NewValuesArePassed() {

        var sport = Sports.HANDBALL;
        var players = createNewPlayerList();
        var teamA = createNewTeam();
        var teamB = createNewTeam();
        var matchDto = createNewMatchDto(sport, entityManager);

        var existingMatch = EntityTestUtil.createNewMatch(Sports.BASKETBALL, entityManager);
        var existingId = existingMatch.getId();

        var updatedMatch = matchMapper.
                toExistingMatch(existingId, matchDto, players, teamA, teamB);

        assertNotEquals(existingMatch, updatedMatch);
    }

}