package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.dtos.PenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.enums.PenaltyCardColor;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.PenaltyCardRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.EntityTestUtil.*;
import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;

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
    private FutsalMatchService futsalMatchService;
    @Autowired
    private HandballMatchService handballMatchService;

    @Test
    @DisplayName("Should retrieve all PenaltyCards from repository when searching for all PenaltyCards")
    void Should_RetrieveAllPenaltyCardsFromRepository_When_SearchingForAllPenaltyCards() {

        var sport = Sports.FUTSAL;
        var match = futsalMatchService.saveMatch(createNewMatch(sport, entityManager));

        List<PenaltyCard> existingPenaltyCards = List.of(
                createNewPenaltyCard(sport, PenaltyCardColor.RED, match),
                createNewPenaltyCard(sport, PenaltyCardColor.YELLOW, match));

        existingPenaltyCards.forEach(penaltyCard -> entityManager.merge(penaltyCard));

        List<PenaltyCard> penaltyCardList = penaltyCardService.findAllPenaltyCards();

        assertEquals(existingPenaltyCards, penaltyCardList);
    }

    @Test
    @DisplayName("Should find PenaltyCard when existing PenaltyCard ID is passed to search")
    void Should_FindPenaltyCard_When_ExistingPenaltyCardIdIsPassedToSearch() {

        var sport = Sports.FUTSAL;
        var match = futsalMatchService.saveMatch(createNewMatch(sport, entityManager));
        var existingPenaltyCard = createNewPenaltyCard(sport, PenaltyCardColor.RED, match);

        entityManager.merge(existingPenaltyCard);

        var existingId = existingPenaltyCard.getId();
        var penaltyCard = penaltyCardService.findPenaltyCardById(existingId);

        assertEquals(penaltyCard, existingPenaltyCard);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing PenaltyCard ID is passed to search")
    void Should_ThrowNotFoundException_When_NonExistingPenaltyCardIdIsPassedToSearch() {

        var id = getRandomLongId();

        assertThrows(NotFoundException.class, () -> {
            penaltyCardService.findPenaltyCardById(id);
        });
    }

    @Test
    @DisplayName("Should save PenaltyCard when valid PenaltyCardDto is passed to save")
    void Should_SavePenaltyCard_When_ValidPenaltyCardDtoIsPassedToSave() {

        var sport = Sports.HANDBALL;
        var match = handballMatchService.saveMatch(createNewMatch(sport, entityManager));
        var penaltyCardDto = createNewPenaltyCardDto(sport, PenaltyCardColor.RED, match);
        var savedId = penaltyCardService.savePenaltyCard(penaltyCardDto).getId();

        var savedPenaltyCard = penaltyCardRepository.findById(savedId).get();

        assertNotNull(savedPenaltyCard);
    }

    @Test
    @DisplayName("Should delete PenaltyCard from database when PenaltyCard ID is passed to delete")
    void Should_DeletePenaltyCardFromDatabase_When_PenaltyCardIdIsPassedToDelete() {

        var sport = Sports.FUTSAL;
        var match = futsalMatchService.saveMatch(createNewMatch(sport, entityManager));
        var existingPenaltyCard = createNewPenaltyCard(sport, PenaltyCardColor.YELLOW, match);

        entityManager.merge(existingPenaltyCard);

        var existingId = existingPenaltyCard.getId();
        penaltyCardService.deletePenaltyCardById(existingId);

        assertTrue(penaltyCardRepository.findById(existingId).isEmpty());
    }

    @Test
    @DisplayName("Should update PenaltyCard when PenaltyCardDto with new values is passed")
    void Should_UpdatePenaltyCard_When_PenaltyCardDtoWithNewValuesIsPassed() {

        var sport = Sports.FUTSAL;
        var match = futsalMatchService.saveMatch(createNewMatch(sport, entityManager));
        var existingPenaltyCard = createNewPenaltyCard(sport, PenaltyCardColor.RED, match);

        entityManager.merge(existingPenaltyCard);

        var existingId = existingPenaltyCard.getId();
        var newSport = Sports.HANDBALL;
        var newMatch = handballMatchService.saveMatch(createNewMatch(sport, entityManager));
        var penaltyCardDto = createNewPenaltyCardDto(newSport, PenaltyCardColor.RED, newMatch);

        var updatedPenaltyCard = penaltyCardService.replacePenaltyCard(existingId, penaltyCardDto);

        assertNotNull(updatedPenaltyCard);
        assertNotEquals(existingPenaltyCard, updatedPenaltyCard);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing ID is passed to replace PenaltyCard")
    void Should_ThrowNotFoundException_When_NonExistingIdIsPassedToReplacePenaltyCard() {

        var id = getRandomLongId();
        var penaltyCardDto = PenaltyCardDto.builder().build();

        assertThrows(NotFoundException.class, () -> {
            penaltyCardService.replacePenaltyCard(id, penaltyCardDto);
        });
    }

}