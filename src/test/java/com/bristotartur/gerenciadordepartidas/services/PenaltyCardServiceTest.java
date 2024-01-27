package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.actions.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.enums.PenaltyCardColor;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.PenaltyCardRepository;
import com.bristotartur.gerenciadordepartidas.services.actions.PenaltyCardService;
import com.bristotartur.gerenciadordepartidas.services.events.MatchServiceMediator;
import com.bristotartur.gerenciadordepartidas.utils.MatchTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.ParticipantTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.PenaltyCardTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.TeamTestUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
class PenaltyCardServiceTest {

    @Autowired
    private PenaltyCardService penaltyCardService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PenaltyCardRepository penaltyCardRepository;
    @Autowired
    private MatchServiceMediator matchServiceMediator;

    private Participant playerA;
    private Participant playerB;
    private Match match;

    @BeforeEach
    void setUp() {

        var teamA = TeamTestUtil.createNewTeam(TeamName.MESTRES_DE_OBRAS, entityManager);
        var teamB = TeamTestUtil.createNewTeam(TeamName.PAPA_LEGUAS, entityManager);

        playerA = ParticipantTestUtil.createNewParticipant("1-42", teamA, entityManager);
        playerB = ParticipantTestUtil.createNewParticipant("1-51", teamB, entityManager);

        match = MatchTestUtil.createNewMatch(teamA, teamB, List.of(playerA, playerB));
    }

    @Test
    @DisplayName("Should retrieve all PenaltyCards from repository when searching for all PenaltyCards")
    void Should_RetrieveAllPenaltyCardsFromRepository_When_SearchingForAllPenaltyCards() {

        var futsalMatch = matchServiceMediator.saveMatch(match, Sports.FUTSAL);

        List<PenaltyCard> penaltyCards = List.of(
                PenaltyCardTestUtil.createNewPenaltyCard(PenaltyCardColor.RED, playerA, futsalMatch, entityManager),
                PenaltyCardTestUtil.createNewPenaltyCard(PenaltyCardColor.YELLOW, playerA, futsalMatch, entityManager));

        List<PenaltyCard> result = penaltyCardService.findAllPenaltyCards();

        assertEquals(result, penaltyCards);
    }

    @Test
    @DisplayName("Should find PenaltyCard when existing PenaltyCard ID is passed to search")
    void Should_FindPenaltyCard_When_ExistingPenaltyCardIdIsPassedToSearch() {

        var handballMatch = matchServiceMediator.saveMatch(match, Sports.HANDBALL);
        var penaltyCard = PenaltyCardTestUtil
                .createNewPenaltyCard(PenaltyCardColor.RED, playerB, handballMatch, entityManager);

        var result = penaltyCardService.findPenaltyCardById(penaltyCard.getId());

        assertEquals(result, penaltyCard);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing PenaltyCard ID is passed to any method")
    void Should_ThrowNotFoundException_When_NonExistingPenaltyCardIdIsPassedToAnyMethod() {

        var id = getRandomLongId();
        var penaltyCardDto = PenaltyCardTestUtil.createNewPenaltyCardDto(any(), any(), any(), any());

        assertThrows(NotFoundException.class, () -> {
            penaltyCardService.findPenaltyCardById(id);
        });
        assertThrows(NotFoundException.class, () -> {
           penaltyCardService.deletePenaltyCardById(id);
        });
        assertThrows(NotFoundException.class, () -> {
            penaltyCardService.replacePenaltyCard(id, penaltyCardDto);
        });
    }

    @Test
    @DisplayName("Should create new ExposingPenaltyCardDto when PenaltyCard is passed to create ExposingPenaltyCardDto")
    void Should_CreateNewExposingPenaltyCardDto_When_PenaltyCardIsPassedToCratePenaltyCardDto() {

        var sport = Sports.FUTSAL;
        var futsalMatch = matchServiceMediator.saveMatch(match, sport);
        var penaltyCard = PenaltyCardTestUtil
                .createNewPenaltyCard(PenaltyCardColor.RED, playerA, futsalMatch, entityManager);

        var result = penaltyCardService.createExposingPenaltyCardDto(penaltyCard);

        assertEquals(result.getColor(), penaltyCard.getColor());
        assertEquals(result.getPlayer(), penaltyCard.getPlayer().getName());
    }

    @Test
    @DisplayName("Should save PenaltyCard when valid PenaltyCardDto is passed to save")
    void Should_SavePenaltyCard_When_ValidPenaltyCardDtoIsPassedToSave() {

        var sport = Sports.FUTSAL;
        var futsalMatch = matchServiceMediator.saveMatch(match, sport);
        var penaltyCardDto = PenaltyCardTestUtil
                .createNewPenaltyCardDto(sport, PenaltyCardColor.RED, playerA.getId(), futsalMatch.getId());

        var result = penaltyCardService.savePenaltyCard(penaltyCardDto);

        assertEquals(result, penaltyCardRepository.findById(result.getId()).get());
    }

    @Test
    @DisplayName("Should delete PenaltyCard from database when PenaltyCard ID is passed to delete")
    void Should_DeletePenaltyCardFromDatabase_When_PenaltyCardIdIsPassedToDelete() {

        var handballMatch = matchServiceMediator.saveMatch(match, Sports.HANDBALL);
        var penaltyCard = PenaltyCardTestUtil
                .createNewPenaltyCard(PenaltyCardColor.RED, playerA, handballMatch, entityManager);

        var id = penaltyCard.getId();
        penaltyCardService.deletePenaltyCardById(id);

        assertTrue(penaltyCardRepository.findById(id).isEmpty());
    }

    @Test
    @DisplayName("Should update PenaltyCard when PenaltyCardDto with new values is passed")
    void Should_UpdatePenaltyCard_When_PenaltyCardDtoWithNewValuesIsPassed() {

        var handballMatch = matchServiceMediator.saveMatch(match, Sports.HANDBALL);
        var penaltyCard = PenaltyCardTestUtil
                .createNewPenaltyCard(PenaltyCardColor.YELLOW, playerA, handballMatch, entityManager);

        var penaltyCardDto = PenaltyCardTestUtil
                .createNewPenaltyCardDto(Sports.HANDBALL, PenaltyCardColor.RED, playerB.getId(), handballMatch.getId());

        var result = penaltyCardService.replacePenaltyCard(penaltyCard.getId(), penaltyCardDto);
        System.out.println(penaltyCard.getId());

        assertNotEquals(result, penaltyCard);
    }

}