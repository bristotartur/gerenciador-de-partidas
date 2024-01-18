package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.structure.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.MatchStatus;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.services.GeneralMatchSportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class MatchMapperTest {

    @Autowired
    private MatchMapper matchMapper;
    @Autowired
    private GeneralMatchSportService generalMatchSportService;

    private Match createNewMatch(Sports sport) {

        return Match.builder()
                .teamA(createNewTeam())
                .teamB(createNewTeam())
                .players(createNewPlayerList())
                .teamScoreA(3)
                .teamScoreB(2)
                .modality(Modality.MASCULINE.name)
                .matchStatus(MatchStatus.ENDED.name)
                .matchStart(LocalDateTime.of(2024, 1, 10, 13, 57, 0))
                .matchEnd(LocalDateTime.of(2024, 1, 10, 14, 30, 0))
                .build();
    }

    private MatchDto createNewMatchDto(Sports sport) {

        var playerIds = createNewPlayerList().stream()
                .map(player -> player.getId())
                .toList();

        return MatchDto.builder()
                .teamAId(createNewTeam().getId())
                .teamBId(createNewTeam().getId())
                .sport(sport)
                .playerIds(playerIds)
                .teamScoreA(3)
                .teamScoreB(2)
                .modality(Modality.MASCULINE)
                .matchStatus(MatchStatus.ENDED)
                .matchStart(LocalDateTime.of(2024, 1, 10, 13, 57, 00))
                .matchEnd(LocalDateTime.of(2024, 1, 10, 14, 30, 00))
                .build();
    }

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

        var matchDto = createNewMatchDto(Sports.CHESS);
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
        var matchDto = createNewMatchDto(sport);

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
        var matchDto = createNewMatchDto(sport);

        var existingMatch = createNewMatch(Sports.BASKETBALL);
        var existingId = existingMatch.getId();

        var updatedMatch = matchMapper.
                toExistingMatch(existingId, matchDto, players, teamA, teamB);

        assertNotEquals(existingMatch, updatedMatch);
    }

}