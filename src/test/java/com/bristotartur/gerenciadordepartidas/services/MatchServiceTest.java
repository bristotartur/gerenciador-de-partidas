package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.domain.participant.Participant;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.MatchStatus;
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

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

    private Match createNewMatch(Sports sport) {

        var teamA = createNewTeam();
        var teamB = createNewTeam();

        return Match.builder()
                .teamA(teamA)
                .teamB(teamB)
                .players(createNewPlayerList(teamA, teamB))
                .matchSport(generalMatchSportService.newMatchSport(sport))
                .teamScoreA(3)
                .teamScoreB(2)
                .modality(Modality.MASCULINE.name)
                .matchStatus(MatchStatus.ENDED.name)
                .matchStart(LocalDateTime.of(2024, 1, 10, 13, 57, 0))
                .matchEnd(LocalDateTime.of(2024, 1, 10, 14, 30, 0))
                .build();
    }

    private MatchDto createNewMatchDto(Sports sport) {

        var teamA = createNewTeam();
        var teamB = createNewTeam();
        var playerIds = createNewPlayerList(teamA, teamB).stream()
                .map(player -> player.getId())
                .toList();

        return MatchDto.builder()
                .teamAId(teamA.getId())
                .teamBId(teamB.getId())
                .playerIds(playerIds)
                .sport(sport)
                .teamScoreA(3)
                .teamScoreB(2)
                .modality(Modality.MASCULINE)
                .matchStatus(MatchStatus.ENDED)
                .matchStart(LocalDateTime.of(2024, 1, 10, 13, 57, 00))
                .matchEnd(LocalDateTime.of(2024, 1, 10, 14, 30, 00))
                .build();
    }

    private List<Participant> createNewPlayerList(Team teamA, Team teamB) {

        List<Participant> players = new LinkedList<>();
        var rand = new Random();

        for (int i = 0; i < 10; i++) {

            var team = rand.nextBoolean() ? teamA : teamB;

            var player = Participant.builder()
                    .name("sa")
                    .classNumber("2-53")
                    .team(team)
                    .build();

            entityManager.merge(player);
            players.add(player);
        }
        return players;
    }

    private Team createNewTeam() {

        var team = Team.builder()
                .points(300)
                .build();

        entityManager.merge(team);

        return team;
    }

    @Test
    @DisplayName("Should retrieve all Matches from repository when searching for all Matches")
    void Should_RetrieveAllMatchesFromRepository_When_SearchingForAllMatches() {

        List<Match> existingMatches = List.of(createNewMatch(Sports.FUTSAL), createNewMatch(Sports.HANDBALL));
        existingMatches.forEach(match -> entityManager.merge(match));

        List<Match> matchList = matchService.findAllMatches();

        assertEquals(existingMatches, matchList);
    }

    @Test
    @DisplayName("Should find Match when existing Match ID is passed to search")
    void Should_FindMatch_When_ExistingMatchIdIsPassedToSearch() {

        var existingMatch = createNewMatch(Sports.VOLLEYBALL);

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
    @DisplayName("Should save Match when valid MatchDto is passed to save")
    void Should_SaveMatch_When_ValidMatchDtoIsPassedToSave() {

        var matchDto = createNewMatchDto(Sports.TABLE_TENNIS);
        var savedId = matchService.saveMatch(matchDto).getId();

        var savedMatch = matchRepository.findById(savedId).get();

        assertNotNull(savedMatch);
    }

    @Test
    @DisplayName("Should delete Match from database when Match ID is passed to delete")
    void Should_DeleteMatchFromDatabase_When_MatchIdIsPassedToDelete() {

        var existingMatch = createNewMatch(Sports.CHESS);

        entityManager.merge(existingMatch);

        var existingId = existingMatch.getId();
        matchService.deleteMatchById(existingId);

        assertTrue(matchRepository.findById(existingId).isEmpty());
    }

    @Test
    @DisplayName("Should update Match when MatchDto with new values is passed")
    void Should_UpdateMatch_When_MatchDtoWithNewValuesIsPassed() {

        var existingMatch = createNewMatch(Sports.TABLE_TENNIS);

        entityManager.merge(existingMatch);

        var existingId = existingMatch.getId();
        var matchDto = createNewMatchDto(Sports.VOLLEYBALL);

        var updatedMatch = matchService.replaceMatch(existingId, matchDto);

        assertNotNull(updatedMatch);
        assertNotEquals(existingMatch, updatedMatch);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing ID is passed to replace Match")
    void Should_ThrowNotFoundException_When_NonExistingIdIsPassedToReplaceMatch() {

        var id = getRandomLongId();
        var matchDto = createNewMatchDto(Sports.FUTSAL);

        assertThrows(NotFoundException.class, () -> {
            matchService.replaceMatch(id, matchDto);
        });
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid player is passed")
    void Should_ThrowBadRequestException_When_InvalidPlayersArePassed() {

        var playerIds = createNewPlayerList(createNewTeam(), createNewTeam()).stream()
                .map(Participant::getId)
                .toList();

        var match = createNewMatch(Sports.VOLLEYBALL);
        entityManager.merge(match);

        var matchDto = MatchDto.builder()
                .teamAId(createNewTeam().getId())
                .teamBId(createNewTeam().getId())
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