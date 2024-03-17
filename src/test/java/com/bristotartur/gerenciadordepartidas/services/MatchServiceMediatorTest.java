package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.matches.ChessMatch;
import com.bristotartur.gerenciadordepartidas.domain.matches.FutsalMatch;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestMatchDto;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.MatchRepository;
import com.bristotartur.gerenciadordepartidas.services.matches.MatchService;
import com.bristotartur.gerenciadordepartidas.services.matches.MatchServiceMediator;
import com.bristotartur.gerenciadordepartidas.utils.EditionTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.MatchTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.ParticipantTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.SportEventTestUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MatchServiceMediatorTest {

    @Autowired
    private MatchServiceMediator matchServiceMediator;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MatchRepository<Match> matchRepository;
    @Autowired
    private MatchService matchService;

    private Edition edition;
    private Team teamA;
    private Team teamB;
    private final List<Participant> players = new LinkedList<>();
    private final List<Long> playersIds = new LinkedList<>();
    private RequestMatchDto futsalDto;
    private RequestMatchDto handballDto;

    @BeforeEach
    void setUp() {

        edition = EditionTestUtil.createNewEdition(Status.IN_PROGRESS, entityManager);

        teamA = Team.UNICONTTI;
        teamB = Team.ATOMICA;

        var sportEventA = SportEventTestUtil.createNewSportEvent(
                Sports.FUTSAL, Modality.MASCULINE, Status.SCHEDULED, edition, entityManager
        );
        var sportEventB = SportEventTestUtil.createNewSportEvent(
                Sports.HANDBALL, Modality.MASCULINE, Status.SCHEDULED, edition, entityManager
        );
        players.addAll(List.of(
                ParticipantTestUtil.createNewParticipant("3-31", teamA, edition, entityManager),
                ParticipantTestUtil.createNewParticipant("3-61", teamB, edition, entityManager)
        ));
        playersIds.addAll(players.stream()
                .map(Participant::getId)
                .toList());

        futsalDto = MatchTestUtil.createNewMatchDto(Sports.FUTSAL, teamA, teamB, playersIds, sportEventA.getId());
        handballDto = MatchTestUtil.createNewMatchDto(Sports.HANDBALL, teamA, teamB, playersIds, sportEventB.getId());

        sportEventA.setMatches(List.of(MatchTestUtil.createNewMatch(teamA, teamB, players, sportEventA)));
        sportEventB.setMatches(List.of(MatchTestUtil.createNewMatch(teamA, teamB, players, sportEventB)));
        entityManager.merge(sportEventA);
        entityManager.merge(sportEventB);
    }

    @Test
    @DisplayName("Should retrieve all Matches of a specific sport in paged form when specific sport is passed to search")
    void Should_RetrieveAllMatchesOfSpecificSportInPagedForm_When_SpecificSportIsPassedToSearch() {

        var pageable = PageRequest.of(0, 3);

        var matches = List.of(
                matchService.saveMatch(futsalDto),
                matchService.saveMatch(futsalDto),
                matchService.saveMatch(handballDto));

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

        var sportEvent = SportEventTestUtil.createNewSportEvent(
                Sports.CHESS, Modality.MIXED, Status.SCHEDULED, edition, entityManager
        );
        sportEvent.setMatches(List.of(MatchTestUtil.createNewMatch(teamA, teamB, players, sportEvent)));
        entityManager.merge(sportEvent);

        var dto = MatchTestUtil.createNewMatchDto(
                Sports.CHESS, teamA, teamB, playersIds, sportEvent.getId(), sportEvent.getModality()
        );
        var match = matchService.saveMatch(dto);

        var result = matchServiceMediator.saveMatch(match, Sports.CHESS);

        assertEquals(result, matchRepository.findById(result.getId()).get());
        assertInstanceOf(ChessMatch.class, result);
    }

    @Test
    @DisplayName("Should find Match when existing Match ID is passed to search")
    void Should_FindMatch_When_ExistingMatchIdIsPassedToSearch() {

        var sportEvent = SportEventTestUtil.createNewSportEvent(
                Sports.TABLE_TENNIS, Modality.MASCULINE, Status.SCHEDULED, edition, entityManager
        );
        sportEvent.setMatches(List.of(MatchTestUtil.createNewMatch(teamA, teamB, players, sportEvent)));
        entityManager.merge(sportEvent);

        var dto = MatchTestUtil.createNewMatchDto(Sports.TABLE_TENNIS, teamA, teamB, playersIds, sportEvent.getId());
        var match = matchService.saveMatch(dto);

        var result = matchServiceMediator.findMatch(match.getId(), Sports.TABLE_TENNIS);

        assertEquals(result, match);
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

        var match = matchService.saveMatch(futsalDto);

        assertThrows(NotFoundException.class, () -> matchServiceMediator.findMatch(match.getId(), Sports.CHESS));
        assertThrows(NotFoundException.class, () -> matchServiceMediator.findMatchForGoal(match.getId(), Sports.HANDBALL));
        assertThrows(NotFoundException.class, () -> matchServiceMediator.findMatchForCard(match.getId(), Sports.BASKETBALL));
    }

    @Test
    @DisplayName("Should find Match for Goal when valid argument is passed")
    void Should_FindMatchFotGoal_When_ValidArgumentIsPassed() {

        var match = matchService.saveMatch(futsalDto);
        var result = matchServiceMediator.findMatchForGoal(match.getId(), Sports.FUTSAL);

        assertEquals(result, match);
    }

    @Test
    @DisplayName("Should find Match for PenaltyCard when valid argument is passed")
    void Should_FindMatchSportForPenaltyCard_When_ValidArgumentIsPassed() {

        var handballMatch = matchService.saveMatch(handballDto);
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