package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import com.bristotartur.gerenciadordepartidas.services.matches.MatchServiceMediator;
import com.bristotartur.gerenciadordepartidas.utils.EditionTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.MatchTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.ParticipantTestUtil;
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
    private MatchServiceMediator matchServiceMediator;

    private TeamName teamA;
    private TeamName teamB;
    private final List<Participant> players = new LinkedList<>();

    @BeforeEach
    void setUp() {

        var edition = EditionTestUtil.createNewEdition(Status.IN_PROGRESS, entityManager);
        teamA = TeamName.PAPA_LEGUAS;
        teamB = TeamName.TWISTER;

        players.add(ParticipantTestUtil.createNewParticipant("3-53", teamA, edition, entityManager));
        players.add(ParticipantTestUtil.createNewParticipant("3-13", teamB, edition, entityManager));
    }

    @Test
    @DisplayName("Should find Match type when Searching for Match type")
    void Should_FindMatchType_When_SearchingForMatchType() {

        var sport = Sports.CHESS;
        var match = MatchTestUtil.createNewMatch(teamA, teamB, players, Modality.MIXED);
        var chessMatch = matchServiceMediator.saveMatch(match, sport);

        var result = matchRepository.findMatchTypeById(chessMatch.getId(), entityManager);

        assertEquals(sport.name(), result);
    }
}