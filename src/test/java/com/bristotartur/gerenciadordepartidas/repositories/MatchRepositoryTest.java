package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestMatchDto;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import com.bristotartur.gerenciadordepartidas.services.matches.MatchService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MatchRepositoryTest {

    @Autowired
    private MatchRepository<Match> matchRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MatchService matchService;

    private Edition edition;
    private Team teamA;
    private Team teamB;
    private final List<Long> playersIds = new LinkedList<>();
    private RequestMatchDto chessDto;

    @BeforeEach
    void setUp() {

        edition = EditionTestUtil.createNewEdition(Status.IN_PROGRESS, entityManager);
        teamA = Team.PAPA_LEGUAS;
        teamB = Team.TWISTER;

        var sportEvent = SportEventTestUtil.createNewSportEvent(
                Sports.FUTSAL, Modality.MASCULINE, Status.SCHEDULED, edition, entityManager
        );
        var players = List.of(
                ParticipantTestUtil.createNewParticipant("3-31", teamA, edition, entityManager),
                ParticipantTestUtil.createNewParticipant("3-61", teamB, edition, entityManager)
        );
        playersIds.addAll(players.stream()
                .map(Participant::getId)
                .toList());

        chessDto = MatchTestUtil.createNewMatchDto(Sports.FUTSAL, teamA, teamB, playersIds, sportEvent.getId(), sportEvent.getModality());
        sportEvent.setMatches(List.of(MatchTestUtil.createNewMatch(teamA, teamB, players, sportEvent)));
        entityManager.merge(sportEvent);
    }

    @Test
    @DisplayName("Should find Match type when Searching for Match type")
    void Should_FindMatchType_When_SearchingForMatchType() {

        var sport = Sports.FUTSAL;
        var chessMatch = matchService.saveMatch(chessDto);

        var result = matchRepository.findMatchTypeById(chessMatch.getId(), entityManager);

        assertEquals(sport.name(), result);
    }

}