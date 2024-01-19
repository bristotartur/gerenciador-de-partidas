package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.structure.Match;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
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

import java.util.LinkedList;
import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.EntityTestUtil.*;
import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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
    private FutsalMatchService futsalMatchService;
    @Autowired
    private HandballMatchService handballMatchService;


    @Test
    @DisplayName("Should retrieve all Matches from repository when searching for all Matches")
    void Should_RetrieveAllMatchesFromRepository_When_SearchingForAllMatches() {

        List<Match> existingMatches = List.of(
                createNewMatch(Sports.FUTSAL, entityManager),
                createNewMatch(Sports.HANDBALL, entityManager));

        existingMatches.forEach(match -> entityManager.merge(match));

        List<Match> matchList = matchService.findAllMatches();

        assertEquals(existingMatches, matchList);
    }

    @Test
    @DisplayName("Should retrieve all Matches of a specific sport when specific sport is passed to search")
    void Should_RetrieveMatchesOfSpecificSport_When_SpecificSportIsPassedToSearch() {

        var futsalMatchList = List.of(
                futsalMatchService.saveMatch(createNewMatch(Sports.FUTSAL, entityManager)),
                futsalMatchService.saveMatch(createNewMatch(Sports.FUTSAL, entityManager)));

        var genericMatchList = new LinkedList<>(
                List.of(handballMatchService.saveMatch(createNewMatch(Sports.HANDBALL, entityManager)))
        );
        futsalMatchList.forEach(genericMatchList::add);

        var result = matchService.findMatchesBySport(Sports.FUTSAL);

        assertEquals(result, futsalMatchList);
        assertNotEquals(result, genericMatchList);
    }

    @Test
    @DisplayName("Should find Match when existing Match ID is passed to search")
    void Should_FindMatch_When_ExistingMatchIdIsPassedToSearch() {

        var existingMatch = createNewMatch(Sports.VOLLEYBALL, entityManager);

        entityManager.merge(existingMatch);

        var existingId = existingMatch.getId();
        var match = matchService.findMatchById(existingId);

        assertEquals(match, existingMatch);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing Match ID is passed to search")
    void Should_ThrowNotFoundException_When_NonExistingMatchIdIsPassedToSearch() {

        var id = getRandomLongId();

        assertThrows(NotFoundException.class, () -> {
            matchService.findMatchById(id);
        });
    }

    @Test
    @DisplayName("Should retrieve all Match players when searching for Match players")
    void Should_RetrieveAllMatchPlayers_When_SearchingForMatchPlayers() {

        var match = createNewMatch(Sports.TABLE_TENNIS, entityManager);
        entityManager.merge(match);

        var players = matchService.findAllMatchPlayers(match.getId());

        assertEquals(players, match.getPlayers());
    }

    @Test
    @DisplayName("Should save Match when valid MatchDto is passed to save")
    void Should_SaveMatch_When_ValidMatchDtoIsPassedToSave() {

        var matchDto = createNewMatchDto(Sports.TABLE_TENNIS, entityManager);
        var savedId = matchService.saveMatch(matchDto).getId();

        var savedMatch = matchRepository.findById(savedId).get();

        assertNotNull(savedMatch);
    }

    @Test
    @DisplayName("Should throw BadRequestException when MatchDto with two equal team IDs is passed")
    void Should_ThrowBadRequestException_When_MatchDtoWithTwoEqualTeamIdsIsPassed() {

        var existingId = futsalMatchService.saveMatch(createNewMatch(Sports.FUTSAL, entityManager)).getId();

        var matchDto = MatchDto.builder()
                .teamAId(1L)
                .teamBId(1L)
                .build();

        assertThrows(BadRequestException.class, () -> {
            matchService.saveMatch(matchDto);
        });
        assertThrows(BadRequestException.class, () -> {
            matchService.replaceMatch(existingId, matchDto);
        });
    }

    @Test
    @DisplayName("Should delete Match from database when Match ID is passed to delete")
    void Should_DeleteMatchFromDatabase_When_MatchIdIsPassedToDelete() {

        var existingMatch = createNewMatch(Sports.CHESS, entityManager);

        entityManager.merge(existingMatch);

        var existingId = existingMatch.getId();
        matchService.deleteMatchById(existingId);

        assertTrue(matchRepository.findById(existingId).isEmpty());
    }

    @Test
    @DisplayName("Should update Match when MatchDto with new values is passed")
    void Should_UpdateMatch_When_MatchDtoWithNewValuesIsPassed() {

        var sport = Sports.BASKETBALL;
        var match =  matchService.saveMatch(createNewMatchDto(sport, Modality.MASCULINE, entityManager));

        var existingId = match.getId();
        var originalModality = match.getModality();
        var matchDto = createNewMatchDto(sport, Modality.FEMININE, entityManager);

        var updatedMatch = matchService.replaceMatch(existingId, matchDto);

        assertNotEquals(originalModality, updatedMatch.getModality());
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing ID is passed to replace Match")
    void Should_ThrowNotFoundException_When_NonExistingIdIsPassedToReplaceMatch() {

        var id = getRandomLongId();
        var matchDto = createNewMatchDto(Sports.FUTSAL, entityManager);

        assertThrows(NotFoundException.class, () -> {
            matchService.replaceMatch(id, matchDto);
        });
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid player is passed")
    void Should_ThrowBadRequestException_When_InvalidPlayersArePassed() {

        var playerIds = createNewPlayerList(entityManager)
                .stream()
                .map(Participant::getId)
                .toList();

        var match = createNewMatch(Sports.VOLLEYBALL, entityManager);
        entityManager.merge(match);

        var matchDto = MatchDto.builder()
                .teamAId(createNewTeam(entityManager).getId())
                .teamBId(createNewTeam(entityManager).getId())
                .playerIds(playerIds)
                .sport(Sports.VOLLEYBALL)
                .build();

        assertThrows(BadRequestException.class, () -> {
            matchService.saveMatch(matchDto);
        });
        assertThrows(BadRequestException.class, () -> {
            matchService.replaceMatch(match.getId(), matchDto);
        });
    }

}