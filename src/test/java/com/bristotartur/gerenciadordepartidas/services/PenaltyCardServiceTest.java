package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.input.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.*;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.PenaltyCardRepository;
import com.bristotartur.gerenciadordepartidas.services.actions.PenaltyCardService;
import com.bristotartur.gerenciadordepartidas.services.matches.MatchService;
import com.bristotartur.gerenciadordepartidas.utils.*;
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

import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PenaltyCardServiceTest {

    @Autowired
    private PenaltyCardService penaltyCardService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PenaltyCardRepository penaltyCardRepository;
    @Autowired
    private MatchService matchService;

    private Edition edition;
    private Participant playerA;
    private Participant playerB;
    private MatchDto futsalDto;
    private MatchDto handballDto;

    @BeforeEach
    void setUp() {

        edition = EditionTestUtil.createNewEdition(Status.IN_PROGRESS, entityManager);
        var teamA = Team.MESTRES_DE_OBRAS;
        var teamB = Team.PAPA_LEGUAS;

        var sportEventA = SportEventTestUtil.createNewSportEvent(
                Sports.FUTSAL, Modality.MASCULINE, Status.SCHEDULED, edition, entityManager
        );
        var sportEventB = SportEventTestUtil.createNewSportEvent(
                Sports.HANDBALL, Modality.MASCULINE, Status.SCHEDULED, edition, entityManager
        );
        playerA = ParticipantTestUtil.createNewParticipant("1-42", teamA, edition, entityManager);
        playerB = ParticipantTestUtil.createNewParticipant("1-51", teamB, edition, entityManager);

        futsalDto = MatchTestUtil.createNewMatchDto(
                Sports.FUTSAL, teamA, teamB, List.of(playerA.getId(), playerB.getId()), sportEventA.getId()
        );
        handballDto = MatchTestUtil.createNewMatchDto(
                Sports.HANDBALL, teamA, teamB, List.of(playerA.getId(), playerB.getId()), sportEventB.getId()
        );
    }

    @Test
    @DisplayName("Should retrieve all PenaltyCards in paged form when searching for all PenaltyCards")
    void Should_RetrieveAllPenaltyCardsInPagedForm_When_SearchingForAllPenaltyCards() {

        var pageable = PageRequest.of(0, 2);
        var futsalMatch = matchService.saveMatch(futsalDto);

        var penaltyCards = List.of(
                PenaltyCardTestUtil.createNewPenaltyCard(PenaltyCardColor.RED, playerA, futsalMatch, entityManager),
                PenaltyCardTestUtil.createNewPenaltyCard(PenaltyCardColor.YELLOW, playerA, futsalMatch, entityManager));

        var penaltyCardPage = new PageImpl<>(penaltyCards, pageable, penaltyCards.size());
        var result = penaltyCardService.findAllPenaltyCards(pageable);

        assertEquals(result.getContent(), penaltyCardPage.getContent());
        assertEquals(result.getTotalPages(), penaltyCardPage.getTotalPages());
    }

    @Test
    @DisplayName("Should find PenaltyCard when existing PenaltyCard ID is passed to search")
    void Should_FindPenaltyCard_When_ExistingPenaltyCardIdIsPassedToSearch() {


        var handballMatch = matchService.saveMatch(handballDto);
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

        assertThrows(NotFoundException.class, () -> penaltyCardService.findPenaltyCardById(id));
        assertThrows(NotFoundException.class, () -> penaltyCardService.deletePenaltyCardById(id));
        assertThrows(NotFoundException.class, () -> penaltyCardService.replacePenaltyCard(id, penaltyCardDto));
    }

    @Test
    @DisplayName("Should create new ExposingPenaltyCardDto when PenaltyCard is passed to create ExposingPenaltyCardDto")
    void Should_CreateNewExposingPenaltyCardDto_When_PenaltyCardIsPassedToCratePenaltyCardDto() {

        var futsalMatch = matchService.saveMatch(futsalDto);
        var penaltyCard = PenaltyCardTestUtil
                .createNewPenaltyCard(PenaltyCardColor.RED, playerA, futsalMatch, entityManager);

        var result = penaltyCardService.createExposingPenaltyCardDto(penaltyCard);

        assertEquals(result.getColor(), penaltyCard.getColor());
        assertEquals(result.getPlayer(), penaltyCard.getPlayer().getName());
    }

    @Test
    @DisplayName("Should save PenaltyCard when valid PenaltyCardDto is passed to save")
    void Should_SavePenaltyCard_When_ValidPenaltyCardDtoIsPassedToSave() {

        var futsalMatch = matchService.saveMatch(futsalDto);
        var penaltyCardDto = PenaltyCardTestUtil
                .createNewPenaltyCardDto(Sports.FUTSAL, PenaltyCardColor.RED, playerA.getId(), futsalMatch.getId());

        var result = penaltyCardService.savePenaltyCard(penaltyCardDto);

        assertEquals(result, penaltyCardRepository.findById(result.getId()).get());
    }

    @Test
    @DisplayName("Should delete PenaltyCard from database when PenaltyCard ID is passed to delete")
    void Should_DeletePenaltyCardFromDatabase_When_PenaltyCardIdIsPassedToDelete() {

        var handballMatch = matchService.saveMatch(handballDto);
        var penaltyCard = PenaltyCardTestUtil
                .createNewPenaltyCard(PenaltyCardColor.RED, playerA, handballMatch, entityManager);

        var id = penaltyCard.getId();
        penaltyCardService.deletePenaltyCardById(id);

        assertTrue(penaltyCardRepository.findById(id).isEmpty());
    }

    @Test
    @DisplayName("Should update PenaltyCard when PenaltyCardDto with new values is passed")
    void Should_UpdatePenaltyCard_When_PenaltyCardDtoWithNewValuesIsPassed() {

        var handballMatch = matchService.saveMatch(handballDto);
        var penaltyCard = PenaltyCardTestUtil
                .createNewPenaltyCard(PenaltyCardColor.YELLOW, playerA, handballMatch, entityManager);

        var penaltyCardDto = PenaltyCardTestUtil
                .createNewPenaltyCardDto(Sports.HANDBALL, PenaltyCardColor.RED, playerB.getId(), handballMatch.getId());

        var result = penaltyCardService.replacePenaltyCard(penaltyCard.getId(), penaltyCardDto);

        assertNotEquals(result, penaltyCard);
    }

}