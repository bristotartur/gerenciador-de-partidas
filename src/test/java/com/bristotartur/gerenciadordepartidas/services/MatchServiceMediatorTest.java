package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.events.ChessMatch;
import com.bristotartur.gerenciadordepartidas.domain.events.FutsalMatch;
import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.MatchRepository;
import com.bristotartur.gerenciadordepartidas.services.events.MatchServiceMediator;
import com.bristotartur.gerenciadordepartidas.utils.MatchTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.ParticipantTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.TeamTestUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MatchServiceMediatorTest {

    @Autowired
    private MatchServiceMediator matchServiceMediator;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MatchRepository<Match> matchRepository;

    private Team teamA;
    private Team teamB;
    private final List<Participant> players = new LinkedList<>();

    @BeforeEach
    void setUp() {

        teamA = TeamTestUtil.createNewTeam(TeamName.UNICONTTI, entityManager);
        teamB = TeamTestUtil.createNewTeam(TeamName.ATOMICA, entityManager);

        players.add(ParticipantTestUtil.createNewParticipant("3-31", teamA, entityManager));
        players.add(ParticipantTestUtil.createNewParticipant("3-61", teamB, entityManager));
    }

    @Test
    @DisplayName("Should retrieve all Matches of a specific sport in paged form when specific sport is passed to search")
    void Should_RetrieveAllMatchesOfSpecificSportInPagedForm_When_SpecificSportIsPassedToSearch() {

        var pageable = PageRequest.of(0, 3);

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players);
        var matches = List.of(
                matchServiceMediator.saveMatch(match, Sports.FUTSAL),
                matchServiceMediator.saveMatch(match, Sports.FUTSAL),
                matchServiceMediator.saveMatch(match, Sports.HANDBALL));


        var matchPage = new PageImpl<>(matches, pageable, matches.size());
        var result = matchServiceMediator.findMatchesBySport(Sports.FUTSAL, pageable);

        assertNotEquals(result.getContent(), matchPage.getContent());
        assertEquals(result.getTotalPages(), matchPage.getTotalPages());

        for (Match r : result.getContent()) {
            assertInstanceOf(FutsalMatch.class, r);
        }
    }

    @Test
    @DisplayName("Should save Match of an specific sport when new Match is passed to save")
    void Should_SaveMatchOfAnSpecifSport_When_NewMatchIsPassedToSave() {

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players);
        var result = matchServiceMediator.saveMatch(match, Sports.CHESS);

        assertEquals(result, matchRepository.findById(result.getId()).get());
        assertInstanceOf(ChessMatch.class, result);
    }

    @Test
    @DisplayName("Should find Match when existing Match ID is passed to search")
    void Should_FindMatch_When_ExistingMatchIdIsPassedToSearch() {

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players);
        var futsalMatch = matchServiceMediator.saveMatch(match, Sports.TABLE_TENNIS);

        var result = matchServiceMediator.findMatch(futsalMatch.getId(), Sports.TABLE_TENNIS);

        assertEquals(result, futsalMatch);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing Match ID is passed to any search")
    void Should_ThrowNotFoundException_When_NonExistingMatchIdIsPassedToAnySearch() {

        var randomId = getRandomLongId();

        assertThrows(NotFoundException.class, () -> matchServiceMediator.findMatch(randomId, Sports.FUTSAL));
        assertThrows(NotFoundException.class, () -> matchServiceMediator.findMatchForGoal(randomId, Sports.FUTSAL));
        assertThrows(NotFoundException.class, () -> matchServiceMediator.findMatchForCard(randomId, Sports.HANDBALL));
    }

    @Test
    @DisplayName("Should throw NotFoundException when existing Match ID with invalid sport is passed to any search")
    void Should_ThrowNotFoundException_When_ExistingIdWithInvalidSportIsPassedToSearchAnySearch() {

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players);
        var futsalMatch = matchServiceMediator.saveMatch(match, Sports.FUTSAL);

        assertThrows(NotFoundException.class, () -> matchServiceMediator.findMatch(futsalMatch.getId(), Sports.CHESS));
        assertThrows(NotFoundException.class, () -> matchServiceMediator.findMatchForGoal(futsalMatch.getId(), Sports.HANDBALL));
        assertThrows(NotFoundException.class, () -> matchServiceMediator.findMatchForCard(futsalMatch.getId(), Sports.BASKETBALL));
    }

    @Test
    @DisplayName("Should find Match for Goal when valid argument is passed")
    void Should_FindMatchFotGoal_When_ValidArgumentIsPassed() {

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players);
        var futsalMatch = matchServiceMediator.saveMatch(match, Sports.FUTSAL);

        var result = matchServiceMediator.findMatchForGoal(futsalMatch.getId(), Sports.FUTSAL);

        assertEquals(result, futsalMatch);
    }

    @Test
    @DisplayName("Should find Match for PenaltyCard when valid argument is passed")
    void Should_FindMatchSportForPenaltyCard_When_ValidArgumentIsPassed() {

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players);
        var handballMatch = matchServiceMediator.saveMatch(match, Sports.HANDBALL);

        var result = matchServiceMediator.findMatchForGoal(handballMatch.getId(), Sports.HANDBALL);

        assertEquals(result, handballMatch);
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid Sport is passed to find Match for any action")
    void Should_ThrowBadRequestException_When_InvalidSportIsPassedToFindMatchForAnyAction() {

        var randomId = getRandomLongId();

        assertThrows(BadRequestException.class, () -> matchServiceMediator.findMatchForGoal(randomId, Sports.CHESS));
        assertThrows(BadRequestException.class, () -> matchServiceMediator.findMatchForCard(randomId, Sports.CHESS));
    }

}