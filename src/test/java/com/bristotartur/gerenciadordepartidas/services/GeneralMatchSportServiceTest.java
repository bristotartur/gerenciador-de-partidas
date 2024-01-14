package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.BasketballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.FutsalMatch;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.FutsalMatchRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    private FutsalMatchRepository futsalMatchRepository;

    @Test
    @DisplayName("Should save MatchSport when new MatchSport is created")
    void Should_SaveMatchSport_When_NewMatchSportIsCreated() {

        var existingMatchSport = generalMatchSportService.newMatchSport(Sports.FUTSAL);
        var existingId = existingMatchSport.getId();

        var result = futsalMatchRepository.findById(existingId).get();

        assertNotNull(existingMatchSport);
        assertEquals(existingMatchSport, result);
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid Sport is passed on findMatchSportForGoal")
    void Should_ThrowBadRequestException_When_InvalidSportIsPassedOnFindMatchSportForGoal() {

        var randomId = getRandomLongId();

        assertThrows(BadRequestException.class, () -> {
            generalMatchSportService.findMatchSportForGoal(randomId, Sports.CHESS);
        });
    }

    @Test
    @DisplayName("Should find MatchSport when existing MatchSport id is passed")
    void Should_FindMatchSport_When_ExistingMatchSportIdIsPassed() {

        var mockFutsalMatch = new FutsalMatch();
        entityManager.merge(mockFutsalMatch);

        var result = generalMatchSportService.findMatchSport(mockFutsalMatch.getId(), Sports.FUTSAL);

        assertNotNull(result);
        assertEquals(mockFutsalMatch, result);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing MatchSport id is passed")
    void Should_ThrowNotFoundException_When_NonExistingMatchSportIdIsPassed() {

        var randomId = getRandomLongId();

        assertThrows(NotFoundException.class, () -> {
            generalMatchSportService.findMatchSport(randomId, Sports.FUTSAL);
        });
    }

    @Test
    @DisplayName("Should find MatchSport for Goal when valid argument is passed")
    void Should_FindMatchSportFotGoal_When_ValidArgumentIsPassed() {

        var existingFutsalMatch = new FutsalMatch();
        entityManager.merge(existingFutsalMatch);

        var existingId = existingFutsalMatch.getId();
        var result = generalMatchSportService.findMatchSportForGoal(existingId, Sports.FUTSAL);

        assertNotNull(result);
        assertEquals(existingFutsalMatch, result);
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid sport is passed on findMatchSportForCard")
    void Should_ThrowBadRequestException_When_InvalidArgumentIsPassedOnFindMatchSportForPenalCard() {

        var randomId = getRandomLongId();

        assertThrows(BadRequestException.class, () -> {
            generalMatchSportService.findMatchSportForCard(randomId, Sports.CHESS);
        });
    }

    @Test
    @DisplayName("Should find MatchSport for PenaltyCard when valid argument is passed")
    void Should_FindMatchSportForPenaltyCard_When_ValidArgumentIsPassed() {

        var existingBasketballMatch = new BasketballMatch();
        entityManager.merge(existingBasketballMatch);

        var existingId = existingBasketballMatch.getId();
        var result = generalMatchSportService.findMatchSportForCard(existingId, Sports.BASKETBALL);

        assertNotNull(result);
        assertEquals(existingBasketballMatch, result);
    }
}