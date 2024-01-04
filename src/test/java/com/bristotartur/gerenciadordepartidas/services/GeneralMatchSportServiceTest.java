package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.BasketballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.FootballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.FootballMatchRepository;
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
    private FootballMatchRepository footballMatchRepository;

    @Test
    @DisplayName("Should save MatchSport when new MatchSport is created")
    void Should_SaveMatchSport_When_NewMatchSportIsCreated() {

        var existingMatchSport = generalMatchSportService.newMatchSport(Sports.FOOTBALL);
        var existingId = existingMatchSport.getId();

        var result = footballMatchRepository.findById(existingId).get();

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

        var mockFootballMatch = new FootballMatch();
        entityManager.merge(mockFootballMatch);

        var result = generalMatchSportService.findMatchSport(mockFootballMatch.getId(), Sports.FOOTBALL);

        assertNotNull(result);
        assertEquals(mockFootballMatch, result);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing MatchSport id is passed")
    void Should_ThrowNotFoundException_When_NonExistingMatchSportIdIsPassed() {

        var randomId = getRandomLongId();

        assertThrows(NotFoundException.class, () -> {
            generalMatchSportService.findMatchSport(randomId, Sports.FOOTBALL);
        });
    }

    @Test
    @DisplayName("Should find MatchSport for Goal when valid argument is passed")
    void Should_FindMatchSportFotGoal_When_ValidArgumentIsPassed() {

        var existingFootballMatch = new FootballMatch();
        entityManager.merge(existingFootballMatch);

        var existingId = existingFootballMatch.getId();
        var result = generalMatchSportService.findMatchSportForGoal(existingId, Sports.FOOTBALL);

        assertNotNull(result);
        assertEquals(existingFootballMatch, result);
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