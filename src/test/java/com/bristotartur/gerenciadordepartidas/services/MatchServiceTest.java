package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.MatchRepository;
import com.bristotartur.gerenciadordepartidas.utils.MatchTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.ParticipantTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.TeamTestUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
class MatchServiceTest {

    @Autowired
    private MatchService matchService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private GeneralMatchSportService generalMatchSportService;

    private Team teamA;
    private Team teamB;
    private Team teamC;
    private List<Participant> players = new LinkedList<>();
    private List<Participant> invalidPlayers = new LinkedList<>();

    @BeforeEach
    void setUp() {

        teamA = TeamTestUtil.createNewTeam(TeamName.PAPA_LEGUAS, entityManager);
        teamB = TeamTestUtil.createNewTeam(TeamName.TWISTER, entityManager);
        teamC = TeamTestUtil.createNewTeam(TeamName.UNICONTTI, entityManager);

        players.add(ParticipantTestUtil.createNewParticipant("3-53", teamA, entityManager));
        players.add(ParticipantTestUtil.createNewParticipant("3-13", teamB, entityManager));

        invalidPlayers.add(ParticipantTestUtil.createNewParticipant("3-53", teamA, entityManager));
        invalidPlayers.add(ParticipantTestUtil.createNewParticipant("3-13", teamB, entityManager));
        invalidPlayers.add(ParticipantTestUtil.createNewParticipant("3-81", teamC, entityManager));
    }

    @Test
    @DisplayName("Should retrieve all Matches from repository when searching for all Matches")
    void Should_RetrieveAllMatchesFromRepository_When_SearchingForAllMatches() {

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players);
        var matches = List.of(
                generalMatchSportService.saveMatch(match, Sports.FUTSAL),
                generalMatchSportService.saveMatch(match, Sports.HANDBALL));

        var result = matchService.findAllMatches();

        assertEquals(result, matches);
    }

    @Test
    @DisplayName("Should retrieve all Matches of a specific sport when specific sport is passed to search")
    void Should_RetrieveMatchesOfSpecificSport_When_SpecificSportIsPassedToSearch() {

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players);
        var futsalMatches = List.of(
                generalMatchSportService.saveMatch(match, Sports.FUTSAL),
                generalMatchSportService.saveMatch(match, Sports.FUTSAL));

        var genericMatchList = new LinkedList<>(List.of(generalMatchSportService.saveMatch(match, Sports.HANDBALL)));

        futsalMatches.forEach(genericMatchList::add);
        var result = matchService.findMatchesBySport(Sports.FUTSAL);

        assertEquals(result, futsalMatches);
        assertNotEquals(result, genericMatchList);
    }

    @Test
    @DisplayName("Should find Match when existing Match ID is passed to search")
    void Should_FindMatch_When_ExistingMatchIdIsPassedToSearch() {

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players, Modality.MIXED);
        var volleyballMatch = generalMatchSportService.saveMatch(match, Sports.VOLLEYBALL);

        var result = matchService.findMatchById(volleyballMatch.getId());

        assertEquals(result, volleyballMatch);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing Match ID is passed to any method")
    void Should_ThrowNotFoundException_When_NonExistingMatchIdIsPassedToAnyMethod() {

        var id = getRandomLongId();
        var matchDto = MatchTestUtil.createNewMatchDto(any(), any(), any(), any());

        assertThrows(NotFoundException.class, () -> {
            matchService.findMatchById(id);
        });
        assertThrows(NotFoundException.class, () -> {
            matchService.deleteMatchById(id);
        });
        assertThrows(NotFoundException.class, () -> {
            matchService.replaceMatch(id, matchDto);
        });
    }

    @Test
    @DisplayName("Should retrieve all Match players when searching for Match players")
    void Should_RetrieveAllMatchPlayers_When_SearchingForMatchPlayers() {

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players, Modality.FEMININE);
        var handballMatch = generalMatchSportService.saveMatch(match, Sports.HANDBALL);

        var result = matchService.findAllMatchPlayers(handballMatch.getId());

        assertEquals(result, handballMatch.getPlayers());
    }

    @Test
    @DisplayName("Should save Match when valid MatchDto is passed to save")
    void Should_SaveMatch_When_ValidMatchDtoIsPassedToSave() {

        var playerIds = players.stream()
                .map(Participant::getId)
                .toList();

        var matchDto = MatchTestUtil.createNewMatchDto(Sports.TABLE_TENNIS, teamA.getId(), teamB.getId(), playerIds);
        var result = matchService.saveMatch(matchDto);

        assertEquals(result, matchRepository.findById(result.getId()).get());
    }

    @Test
    @DisplayName("Should throw BadRequestException when MatchDto with two equal team IDs is passed")
    void Should_ThrowBadRequestException_When_MatchDtoWithTwoEqualTeamIdsIsPassed() {

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players);
        var futsalMatch = generalMatchSportService.saveMatch(match, Sports.FUTSAL);

        var matchDto = MatchTestUtil.createNewMatchDto(any(), 1L, 1L, any());

        assertThrows(BadRequestException.class, () -> {
            matchService.saveMatch(matchDto);
        });
        assertThrows(BadRequestException.class, () -> {
            matchService.replaceMatch(futsalMatch.getId(), matchDto);
        });
    }

    @Test
    @DisplayName("Should delete Match from database when Match ID is passed to delete")
    void Should_DeleteMatchFromDatabase_When_MatchIdIsPassedToDelete() {

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players, Modality.MIXED);
        var futsalMatch = generalMatchSportService.saveMatch(match, Sports.CHESS);

        matchService.deleteMatchById(futsalMatch.getId());

        assertTrue(matchRepository.findById(futsalMatch.getId()).isEmpty());
    }

    @Test
    @DisplayName("Should update Match when MatchDto with new values is passed")
    void Should_UpdateMatch_When_MatchDtoWithNewValuesIsPassed() {

        var sport = Sports.TABLE_TENNIS;
        var originalModality = Modality.MASCULINE;

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players, originalModality);
        var tableTennisMatch = generalMatchSportService.saveMatch(match, sport);

        var playerIds = players.stream()
                .map(Participant::getId)
                .toList();

        var newModality = Modality.FEMININE;
        var matchDto = MatchTestUtil.createNewMatchDto(sport, teamA.getId(), teamB.getId(), playerIds, newModality);
        var result = matchService.replaceMatch(tableTennisMatch.getId(), matchDto);

        assertNotNull(result);
        assertNotEquals(result.getModality(), originalModality.name);
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid players are passed to save or update Match")
    void Should_ThrowBadRequestException_When_InvalidPlayersArePassedToSaveOrUpdateMatch() {

        var playerIds = invalidPlayers.stream()
                .map(Participant::getId)
                .toList();

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players, Modality.MIXED);
        var volleyballMatch = generalMatchSportService.saveMatch(match, Sports.VOLLEYBALL);

        var matchDto = MatchTestUtil.createNewMatchDto(Sports.VOLLEYBALL, teamA.getId(), teamC.getId(), playerIds);

        assertThrows(BadRequestException.class, () -> {
            matchService.saveMatch(matchDto);
        });
        assertThrows(BadRequestException.class, () -> {
            matchService.replaceMatch(volleyballMatch.getId(), matchDto);
        });
    }

}