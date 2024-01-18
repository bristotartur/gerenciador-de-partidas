package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.MatchRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.bristotartur.gerenciadordepartidas.utils.EntityTestUtil.createNewMatch;
import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GeneralMatchSportServiceTest {

    @Autowired
    private GeneralMatchSportService generalMatchSportService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private FutsalMatchService futsalMatchService;
    @Autowired
    private BasketballMatchService basketballMatchService;

    @Test
    @DisplayName("Should save Match when new Match is passed to save")
    void Should_SaveMatch_When_NewMatchIsPassedToSave() {

        var existingMatchSport = generalMatchSportService.saveMatch(createNewMatch(Sports.FUTSAL, entityManager), Sports.FUTSAL);
        var existingId = existingMatchSport.getId();

        var result = matchRepository.findById(existingId).get();

        assertNotNull(existingMatchSport);
        assertEquals(existingMatchSport, result);
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid Sport is passed to find Match for Goal")
    void Should_ThrowBadRequestException_When_InvalidSportIsPassedToFindMatchForGoal() {

        var randomId = getRandomLongId();

        assertThrows(BadRequestException.class, () -> {
            generalMatchSportService.findMatchForGoal(randomId, Sports.CHESS);
        });
    }

    @Test
    @DisplayName("Should find Match when existing Match ID is passed to search")
    void Should_FindMatch_When_ExistingMatchIdIsPassedToSearch() {

        var existingFutsalMatch = futsalMatchService.saveMatch(createNewMatch(Sports.FUTSAL, entityManager));

        var result = generalMatchSportService.findMatch(existingFutsalMatch.getId(), Sports.FUTSAL);

        assertNotNull(result);
        assertEquals(existingFutsalMatch, result);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing Match ID is passed")
    void Should_ThrowNotFoundException_When_NonExistingMatchIdIsPassed() {

        var randomId = getRandomLongId();

        assertThrows(NotFoundException.class, () -> {
            generalMatchSportService.findMatch(randomId, Sports.FUTSAL);
        });
    }

    @Test
    @DisplayName("Should find Match for Goal when valid argument is passed")
    void Should_FindMatchFotGoal_When_ValidArgumentIsPassed() {

        var existingFutsalMatch = futsalMatchService.saveMatch(createNewMatch(Sports.FUTSAL, entityManager));

        var existingId = existingFutsalMatch.getId();
        var result = generalMatchSportService.findMatchForGoal(existingId, Sports.FUTSAL);

        assertNotNull(result);
        assertEquals(existingFutsalMatch, result);
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid sport is passed to find Match for PenaltyCard")
    void Should_ThrowBadRequestException_When_InvalidArgumentIsPassedToFindMatchForPenalCard() {

        var randomId = getRandomLongId();

        assertThrows(BadRequestException.class, () -> {
            generalMatchSportService.findMatchForCard(randomId, Sports.CHESS);
        });
    }

    @Test
    @DisplayName("Should find Match for PenaltyCard when valid argument is passed")
    void Should_FindMatchSportForPenaltyCard_When_ValidArgumentIsPassed() {

        var basketballMatch = basketballMatchService.saveMatch(createNewMatch(Sports.BASKETBALL, entityManager));

        var existingId = basketballMatch.getId();
        var result = generalMatchSportService.findMatchForCard(existingId, Sports.BASKETBALL);

        assertEquals(basketballMatch, result);
    }
}