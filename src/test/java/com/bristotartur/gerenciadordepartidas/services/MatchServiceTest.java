package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingMatchDto;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.MatchRepository;
import com.bristotartur.gerenciadordepartidas.services.events.MatchServiceMediator;
import com.bristotartur.gerenciadordepartidas.services.events.MatchService;
import com.bristotartur.gerenciadordepartidas.utils.MatchTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.ParticipantTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.TeamTestUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    private MatchRepository<Match> matchRepository;
    @Autowired
    private MatchServiceMediator matchServiceMediator;

    private Team teamA;
    private Team teamB;
    private Team teamC;
    private final List<Participant> players = new LinkedList<>();
    private final List<Participant> invalidPlayers = new LinkedList<>();

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
    @DisplayName("Should retrieve all Matches in paged form when searching for all Matches")
    void Should_RetrieveAllMatchesInPagedForm_When_SearchingForAllMatches() {

        var pageable = PageRequest.of(0, 2);

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players);
        var matches = List.of(
                matchServiceMediator.saveMatch(match, Sports.FUTSAL),
                matchServiceMediator.saveMatch(match, Sports.HANDBALL));

        var matchPage = new PageImpl<>(matches, pageable, matches.size());
        var result = matchService.findAllMatches(pageable);

        assertEquals(result.getContent(), matchPage.getContent());
        assertEquals(result.getTotalPages(), matchPage.getTotalPages());
    }

    @Test
    @DisplayName("Should retrieve all Matches of a specific sport in paged form when specific sport is passed to search")
    void Should_RetrieveMatchesOfSpecificSportInPagedForm_When_SpecificSportIsPassedToSearch() {

        var pageable = PageRequest.of(0, 3);

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players);
        var futsalMatches = List.of(
                matchServiceMediator.saveMatch(match, Sports.FUTSAL),
                matchServiceMediator.saveMatch(match, Sports.FUTSAL));

        var genericMatchList = new LinkedList<>(List.of(matchServiceMediator.saveMatch(match, Sports.HANDBALL)));
        genericMatchList.addAll(futsalMatches);

        var genericMatchPage = new PageImpl<>(genericMatchList, pageable, genericMatchList.size());
        var result = matchService.findMatchesBySport(Sports.FUTSAL, pageable);

        assertEquals(result.getTotalPages(), genericMatchPage.getTotalPages());
        assertEquals(result.getContent(), futsalMatches);
        assertNotEquals(result.getContent(), genericMatchList);
    }

    @Test
    @DisplayName("Should find Match when existing Match ID is passed to search")
    void Should_FindMatch_When_ExistingMatchIdIsPassedToSearch() {

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players, Modality.MIXED);
        var volleyballMatch = matchServiceMediator.saveMatch(match, Sports.VOLLEYBALL);

        var result = matchService.findMatchById(volleyballMatch.getId());

        assertEquals(result, volleyballMatch);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing Match ID is passed to any method")
    void Should_ThrowNotFoundException_When_NonExistingMatchIdIsPassedToAnyMethod() {

        var id = getRandomLongId();
        var matchDto = MatchTestUtil.createNewMatchDto(any(), any(), any(), any());

        assertThrows(NotFoundException.class, () -> matchService.findMatchById(id));
        assertThrows(NotFoundException.class, () -> matchService.deleteMatchById(id));
        assertThrows(NotFoundException.class, () -> matchService.replaceMatch(id, matchDto));
    }

    @Test
    @DisplayName("Should retrieve all Match players when searching for Match players")
    void Should_RetrieveAllMatchPlayers_When_SearchingForMatchPlayers() {

        var pageable = PageRequest.of(0, 2);

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players, Modality.FEMININE);
        var handballMatch = matchServiceMediator.saveMatch(match, Sports.HANDBALL);

        var playerPage = new PageImpl<>(handballMatch.getPlayers(), pageable, handballMatch.getPlayers().size());
        var result = matchService.findAllMatchPlayers(handballMatch.getId(), pageable);

        assertEquals(result.getContent(), playerPage.getContent());
        assertEquals(result.getTotalPages(), playerPage.getTotalPages());
    }

    @Test
    @DisplayName("Should create new ExposingMatchDto when Match is passed to create ExposingMatchDto")
    void Should_CreateNewExposingMatchDto_When_MatchIsPassedToCreateExposingMatchDto() {

        var sport = Sports.HANDBALL;
        var match = MatchTestUtil.createNewMatch(teamA, teamB, players, Modality.MASCULINE);
        var handballMatch = matchServiceMediator.saveMatch(match, sport);

        var result = matchService.createExposingMatchDto(handballMatch);

        assertEquals(result.getSport(), sport);
        assertEquals(result.getTeamA(), handballMatch.getTeamA().getName());
        assertEquals(result.getTeamB(), handballMatch.getTeamB().getName());
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
    @DisplayName("Should throw BadRequestException when MatchDto with two equal teams is passed")
    void Should_ThrowBadRequestException_When_MatchDtoWithTwoEqualTeamsIsPassed() {

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players);
        var futsalMatch = matchServiceMediator.saveMatch(match, Sports.FUTSAL);

        var matchDto = MatchTestUtil.createNewMatchDto(any(), 1L, 1L, any());

        assertThrows(BadRequestException.class, () -> matchService.saveMatch(matchDto));
        assertThrows(BadRequestException.class, () -> matchService.replaceMatch(futsalMatch.getId(), matchDto));
    }

    @Test
    @DisplayName("Should delete Match from database when Match ID is passed to delete")
    void Should_DeleteMatchFromDatabase_When_MatchIdIsPassedToDelete() {

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players, Modality.MIXED);
        var chessMatch = matchServiceMediator.saveMatch(match, Sports.CHESS);

        matchService.deleteMatchById(chessMatch.getId());

        assertTrue(matchRepository.findById(chessMatch.getId()).isEmpty());
    }

    @Test
    @DisplayName("Should update Match when MatchDto with new values is passed")
    void Should_UpdateMatch_When_MatchDtoWithNewValuesIsPassed() {

        var sport = Sports.TABLE_TENNIS;
        var originalModality = Modality.MASCULINE;

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players, originalModality);
        var tableTennisMatch = matchServiceMediator.saveMatch(match, sport);

        var playerIds = players.stream()
                .map(Participant::getId)
                .toList();

        var newModality = Modality.FEMININE;
        var matchDto = MatchTestUtil.createNewMatchDto(sport, teamA.getId(), teamB.getId(), playerIds, newModality);
        var result = matchService.replaceMatch(tableTennisMatch.getId(), matchDto);

        assertNotNull(result);
        assertNotEquals(result.getModality(), originalModality);
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid players are passed to save or update Match")
    void Should_ThrowBadRequestException_When_InvalidPlayersArePassedToSaveOrUpdateMatch() {

        var playerIds = invalidPlayers.stream()
                .map(Participant::getId)
                .toList();

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players, Modality.MIXED);
        var volleyballMatch = matchServiceMediator.saveMatch(match, Sports.VOLLEYBALL);

        var matchDto = MatchTestUtil.createNewMatchDto(Sports.VOLLEYBALL, teamA.getId(), teamC.getId(), playerIds);

        assertThrows(BadRequestException.class, () -> matchService.saveMatch(matchDto));
        assertThrows(BadRequestException.class, () -> matchService.replaceMatch(volleyballMatch.getId(), matchDto));
    }

    @Test
    @DisplayName("Should convert Match to ExposingMatchDto when Match is passed to convert")
    void Should_ConvertMatchToExposingMatchDto_When_MatchIsPassedToConvert() {

        var match = MatchTestUtil.createNewMatch(teamA, teamB, players, Modality.MASCULINE);
        var tableTennisMatch = matchServiceMediator.saveMatch(match, Sports.TABLE_TENNIS);

        var result = matchService.createExposingMatchDto(tableTennisMatch);

        assertInstanceOf(ExposingMatchDto.class, result);
        assertEquals(result.getTeamA(), teamA.getName());
        assertEquals(result.getTeamB(), teamB.getName());
    }

}