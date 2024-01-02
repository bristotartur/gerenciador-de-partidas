package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.BasketballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.FootballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GeneralMatchSportServiceTest {

    @Mock
    private FootballMatchService footballMatchService;
    @Mock
    private BasketballMatchService basketballMatchService;

    @InjectMocks
    private GeneralMatchSportService generalMatchSportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(footballMatchService, basketballMatchService);
    }

    @Test
    @DisplayName("Should save MatchSport when new MatchSport is created")
    void Should_SaveMatchSport_When_NewMatchSportIsCreated() {

        FootballMatch mockFootballMatch = new FootballMatch();

        when(footballMatchService.saveFootballMatch(mockFootballMatch)).thenReturn(mockFootballMatch);

        MatchSport savedMatchSport = generalMatchSportService.newMatchSport(Sports.FOOTBALL);

        assertNotNull(savedMatchSport);
        verify(footballMatchService).saveFootballMatch(mockFootballMatch);
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid Sport is passed on findMatchSportForGoal")
    void Should_ThrowBadRequestException_When_InvalidSportIsPassedOnFindMatchSportForGoal() {
        assertThrows(BadRequestException.class, () -> {
            generalMatchSportService.findMatchSportForGoal(1L, Sports.CHESS);
        });
    }


    @Test
    @DisplayName("Should find MatchSport when existing MatchSport id is passed")
    void Should_FindMatchSport_When_ExistingMatchSportIdIsPassed() {

        long randomId = getRandomLongId();
        var mockFootballMatch = FootballMatch.builder().id(randomId).build();

        when(footballMatchService.findFootballMatchById(randomId)).thenReturn(mockFootballMatch);

        MatchSport savedMatchSport = generalMatchSportService.findMatchSport(Sports.FOOTBALL, randomId);

        assertNotNull(savedMatchSport);
        verify(footballMatchService).findFootballMatchById(randomId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing MatchSport id is passed")
    void Should_ThrowNotFoundException_When_NonExistingMatchSportIdIsPassed() {

        long randomId = getRandomLongId();

        when(footballMatchService.findFootballMatchById(randomId))
                .thenThrow(new NotFoundException(ExceptionMessages.FOOTBALL_MATCH_NOT_FOUND.message));

        assertThrows(NotFoundException.class, () -> {
            generalMatchSportService.findMatchSport(Sports.FOOTBALL, randomId);
        });
    }

    @Test
    @DisplayName("Should find MatchSport for Goal when valid argument is passed")
    void Should_FindMatchSportFotGoal_When_ValidArgumentIsPassed() {

        FootballMatch mockFootballMatch = new FootballMatch();
        when(generalMatchSportService.findMatchSportForGoal(anyLong(), Sports.FOOTBALL)).thenReturn(mockFootballMatch);

        MatchSport resultFootball = generalMatchSportService.findMatchSportForGoal(1L, Sports.FOOTBALL);

        assertNotNull(resultFootball);
        verify(footballMatchService).findFootballMatchById(1L);
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid sport is passed on findMatchSportForCard")
    void Should_ThrowBadRequestException_When_InvalidArgumentIsPassedOnFindMatchSportForPenalCard() {
        assertThrows(BadRequestException.class, () -> {
            generalMatchSportService.findMatchSportForCard(1L, Sports.CHESS);
        });
    }

    @Test
    @DisplayName("Should find MatchSport for PenaltyCard when valid argument is passed")
    void Should_FindMatchSportForPenaltyCard_When_ValidArgumentIsPassed() {

        BasketballMatch mockBasketballMatch = new BasketballMatch();
        when(generalMatchSportService.findMatchSportForCard(anyLong(), Sports.BASKETBALL))
                .thenReturn(mockBasketballMatch);

        MatchSport resultBasket = generalMatchSportService.findMatchSportForCard(1L, Sports.BASKETBALL);
        verify(basketballMatchService).findBasketballMatchById(1L);
    }
}