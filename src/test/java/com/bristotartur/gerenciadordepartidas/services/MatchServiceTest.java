package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.response.ResponseMatchDto;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.exceptions.UnprocessableEntityException;
import com.bristotartur.gerenciadordepartidas.repositories.MatchRepository;
import com.bristotartur.gerenciadordepartidas.services.matches.MatchService;
import com.bristotartur.gerenciadordepartidas.utils.EditionTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.MatchTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.ParticipantTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.SportEventTestUtil;
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

import java.util.LinkedList;
import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MatchServiceTest {

    @Autowired
    private MatchService matchService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MatchRepository<Match> matchRepository;

    private Edition edition;
    private Team teamA;
    private Team teamB;
    private Team teamC;
    private SportEvent futsalEvent;
    private SportEvent handballEvent;
    private final List<Participant> players = new LinkedList<>();
    private final List<Long> playersIds = new LinkedList<>();

    @BeforeEach
    void setUp() {

        edition = EditionTestUtil.createNewEdition(Status.IN_PROGRESS, entityManager);

        teamA = Team.PAPA_LEGUAS;
        teamB = Team.TWISTER;
        teamC = Team.UNICONTTI;

        players.addAll(List.of(
                ParticipantTestUtil.createNewParticipant("3-53", teamA, edition, entityManager),
                ParticipantTestUtil.createNewParticipant("3-13", teamB, edition, entityManager)
        ));
        playersIds.addAll(players.stream().map(Participant::getId).toList());

        futsalEvent = SportEventTestUtil.createNewSportEvent(
                Sports.FUTSAL, Modality.FEMININE, Status.SCHEDULED, edition, entityManager
        );
        handballEvent = SportEventTestUtil.createNewSportEvent(
                Sports.HANDBALL, Modality.MASCULINE, Status.SCHEDULED, edition, entityManager
        );
        futsalEvent.setMatches(List.of(MatchTestUtil.createNewMatch(teamA, teamB, players, futsalEvent)));
        handballEvent.setMatches(List.of(MatchTestUtil.createNewMatch(teamA, teamB, players, handballEvent)));
        entityManager.merge(futsalEvent);
        entityManager.merge(handballEvent);
    }

    @Test
    @DisplayName("Should retrieve all Matches in paged form when searching for all Matches")
    void Should_RetrieveAllMatchesInPagedForm_When_SearchingForAllMatches() {

        var pageable = PageRequest.of(0, 2);

        var matches = List.of(
                futsalEvent.getMatches().get(0),
                handballEvent.getMatches().get(0));

        var matchPage = new PageImpl<>(matches, pageable, matches.size());
        var result = matchService.findAllMatches(pageable);
        
        assertEquals(result.getContent(), matchPage.getContent());
        assertEquals(result.getTotalPages(), matchPage.getTotalPages());
    }

    @Test
    @DisplayName("Should retrieve all Matches of a specific sport in paged form when specific sport is passed to search")
    void Should_RetrieveMatchesOfSpecificSportInPagedForm_When_SpecificSportIsPassedToSearch() {

        var pageable = PageRequest.of(0, 3);

        var futsalDto = MatchTestUtil.createNewMatchDto(
                Sports.FUTSAL, teamA, teamB, playersIds, futsalEvent.getId(), futsalEvent.getModality()
        );
        var handballDto = MatchTestUtil.createNewMatchDto(
                Sports.HANDBALL, teamA, teamB, playersIds, handballEvent.getId(), handballEvent.getModality()
        );
        var futsalMatches = List.of(
                matchService.saveMatch(futsalDto),
                matchService.saveMatch(futsalDto));

        var genericMatchList = new LinkedList<>(List.of(matchService.saveMatch(handballDto)));
        genericMatchList.addAll(futsalMatches);

        var genericMatchPage = new PageImpl<>(genericMatchList, pageable, genericMatchList.size());
        var result = matchService.findMatchesBySport(Sports.FUTSAL, pageable);

        assertEquals(result.getTotalPages(), genericMatchPage.getTotalPages());
        assertEquals(result.getContent(), futsalMatches);
        assertNotEquals(result.getContent(), genericMatchList);
    }

    @Test
    @DisplayName("Should retrieve all Matches from a specific SportEvent when existing SportEvent ID is passed")
    void Should_RetrieveAllMatchesFromSportEvent_When_ExistingSportEventIdIsPassed() {

        var futsalDto = MatchTestUtil.createNewMatchDto(
                Sports.FUTSAL, teamA, teamB, playersIds, futsalEvent.getId(), futsalEvent.getModality()
        );
        matchService.saveMatch(futsalDto);
        matchService.saveMatch(futsalDto);
        matchService.saveMatch(futsalDto);

        var sportEventMatches = futsalEvent.getMatches();
        var pageable = PageRequest.of(0, sportEventMatches.size());
        var result = matchService.findMatchesBySportEvent(futsalEvent.getId(), pageable);

        assertEquals(result.getContent(), sportEventMatches);
        assertEquals(result.getPageable(), pageable);
    }

    @Test
    @DisplayName("Should find Match when existing Match ID is passed to search")
    void Should_FindMatch_When_ExistingMatchIdIsPassedToSearch() {

        var sportEvent = SportEventTestUtil.createNewSportEvent(
                Sports.VOLLEYBALL, Modality.MIXED, Status.SCHEDULED, edition, entityManager
        );
        sportEvent.setMatches(List.of(MatchTestUtil.createNewMatch(teamA, teamB, players, sportEvent)));
        entityManager.merge(sportEvent);

        var dto = MatchTestUtil.createNewMatchDto(
                Sports.VOLLEYBALL, teamA, teamB, playersIds, sportEvent.getId(), sportEvent.getModality()
        );
        var volleyballMatch = matchService.saveMatch(dto);

        var result = matchService.findMatchById(volleyballMatch.getId());

        assertEquals(result, volleyballMatch);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing Match ID is passed to any method")
    void Should_ThrowNotFoundException_When_NonExistingMatchIdIsPassedToAnyMethod() {

        var id = getRandomLongId();
        var matchDto = MatchTestUtil.createNewMatchDto(any(), any(), any(), any(), any());

        assertThrows(NotFoundException.class, () -> matchService.findMatchById(id));
        assertThrows(NotFoundException.class, () -> matchService.deleteMatchById(id));
        assertThrows(NotFoundException.class, () -> matchService.replaceMatch(id, matchDto));
    }

    @Test
    @DisplayName("Should retrieve all Match players when searching for Match players")
    void Should_RetrieveAllMatchPlayers_When_SearchingForMatchPlayers() {

        var pageable = PageRequest.of(0, 2);

        var handballDto = MatchTestUtil.createNewMatchDto(Sports.HANDBALL, teamA, teamB, playersIds, handballEvent.getId());
        var handballMatch = matchService.saveMatch(handballDto);

        var playerPage = new PageImpl<>(handballMatch.getPlayers(), pageable, handballMatch.getPlayers().size());
        var result = matchService.findAllMatchPlayers(handballMatch.getId(), pageable);

        assertEquals(result.getContent(), playerPage.getContent());
        assertEquals(result.getTotalPages(), playerPage.getTotalPages());
    }

    @Test
    @DisplayName("Should create new ExposingMatchDto when Match is passed to create ExposingMatchDto")
    void Should_CreateNewExposingMatchDto_When_MatchIsPassedToCreateExposingMatchDto() {

        var sport = Sports.HANDBALL;
        var handballDto = MatchTestUtil.createNewMatchDto(Sports.HANDBALL, teamA, teamB, playersIds, handballEvent.getId());
        var handballMatch = matchService.saveMatch(handballDto);

        var result = matchService.createExposingMatchDto(handballMatch);

        assertEquals(result.getSport(), sport);
        assertEquals(result.getTeamA(), handballMatch.getTeamA());
        assertEquals(result.getTeamB(), handballMatch.getTeamB());
    }

    @Test
    @DisplayName("Should save Match when valid MatchDto is passed to save")
    void Should_SaveMatch_When_ValidMatchDtoIsPassedToSave() {

        var sportEvent = SportEventTestUtil.createNewSportEvent(
                Sports.TABLE_TENNIS, Modality.MASCULINE, Status.SCHEDULED, edition, entityManager
        );
        sportEvent.setMatches(List.of(MatchTestUtil.createNewMatch(teamA, teamB, players, sportEvent)));
        entityManager.merge(sportEvent);

        var matchDto = MatchTestUtil.createNewMatchDto(Sports.TABLE_TENNIS, teamA, teamB, playersIds, sportEvent.getId());
        var result = matchService.saveMatch(matchDto);

        assertEquals(result, matchRepository.findById(result.getId()).get());
    }

    @Test
    @DisplayName("Should throw BadRequestException when MatchDto with two equal teams is passed")
    void Should_ThrowBadRequestException_When_MatchDtoWithTwoEqualTeamsIsPassed() {

        var futsalDto = MatchTestUtil.createNewMatchDto(
                Sports.FUTSAL, teamA, teamB, playersIds, futsalEvent.getId(), futsalEvent.getModality()
        );
        var futsalMatch = matchService.saveMatch(futsalDto);

        var dto = MatchTestUtil.createNewMatchDto(Sports.FUTSAL, teamA, teamA, playersIds, futsalEvent.getId());

        assertThrows(BadRequestException.class, () -> matchService.saveMatch(dto));
        assertThrows(BadRequestException.class, () -> matchService.replaceMatch(futsalMatch.getId(), dto));
    }

    @Test
    @DisplayName("Should throw UnprocessableEntityException when trying to create or update Match on finished event")
    void Should_ThrowbadRequestException_When_TryingToCreateOrUpdateMatchOnFinishedEvent() {

        var sportEvent = SportEventTestUtil.createNewSportEvent(
                Sports.TABLE_TENNIS, Modality.MASCULINE, Status.SCHEDULED, edition, entityManager
        );
        sportEvent.setMatches(List.of(MatchTestUtil.createNewMatch(teamA, teamB, players, sportEvent)));
        entityManager.merge(sportEvent);

        var dto = MatchTestUtil.createNewMatchDto(Sports.TABLE_TENNIS, teamA, teamB, playersIds, sportEvent.getId());
        var match = matchService.saveMatch(dto);

        sportEvent.setEventStatus(Status.ENDED);
        entityManager.merge(sportEvent);

        assertThrows(UnprocessableEntityException.class, () -> matchService.saveMatch(dto));
        assertThrows(UnprocessableEntityException.class, () -> matchService.replaceMatch(match.getId(), dto));
    }

    @Test
    @DisplayName("Should delete Match from database when Match ID is passed to delete")
    void Should_DeleteMatchFromDatabase_When_MatchIdIsPassedToDelete() {

        var sportEvent = SportEventTestUtil.createNewSportEvent(
                Sports.CHESS, Modality.MIXED, Status.SCHEDULED, edition, entityManager
        );
        sportEvent.setMatches(List.of(MatchTestUtil.createNewMatch(teamA, teamB, players, sportEvent)));
        entityManager.merge(sportEvent);

        var chessDto = MatchTestUtil.createNewMatchDto(
                Sports.CHESS, teamA, teamB, playersIds, sportEvent.getId(), sportEvent.getModality()
        );
        var chessMatch = matchService.saveMatch(chessDto);

        matchService.deleteMatchById(chessMatch.getId());

        assertTrue(matchRepository.findById(chessMatch.getId()).isEmpty());
    }

    @Test
    @DisplayName("Should update Match when MatchDto with new values is passed")
    void Should_UpdateMatch_When_MatchDtoWithNewValuesIsPassed() {

        var sport = Sports.HANDBALL;
        var originalModality = Modality.MASCULINE;

        var dtoA = MatchTestUtil.createNewMatchDto(
                sport, teamA, teamB, playersIds, handballEvent.getId(), originalModality
        );
        var handballMatch = matchService.saveMatch(dtoA);

        var dtoB = MatchTestUtil.createNewMatchDto(
                futsalEvent.getType(), teamA, teamB, playersIds, futsalEvent.getId(), futsalEvent.getModality()
        );
        var result = matchService.replaceMatch(handballMatch.getId(), dtoB);

        assertNotNull(result);
        assertNotEquals(result.getEvent(), handballMatch.getEvent());
        assertNotEquals(result.getModality(), originalModality);
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid players are passed to save or update Match")
    void Should_ThrowBadRequestException_When_InvalidPlayersArePassedToSaveOrUpdateMatch() {

        var dto = MatchTestUtil.createNewMatchDto(
                Sports.HANDBALL, teamA, teamB, playersIds, handballEvent.getId()
        );
        var handballMatch = matchService.saveMatch(dto);

        var matchDto = MatchTestUtil.createNewMatchDto(Sports.VOLLEYBALL, teamA, teamC, playersIds, handballEvent.getId());

        assertThrows(BadRequestException.class, () -> matchService.saveMatch(matchDto));
        assertThrows(BadRequestException.class, () -> matchService.replaceMatch(handballMatch.getId(), matchDto));
    }

    @Test
    @DisplayName("Should convert Match to ExposingMatchDto when Match is passed to convert")
    void Should_ConvertMatchToExposingMatchDto_When_MatchIsPassedToConvert() {

        var dto = MatchTestUtil.createNewMatchDto(
                Sports.HANDBALL, teamA, teamB, playersIds, handballEvent.getId()
        );
        var handballMatch = matchService.saveMatch(dto);

        var result = matchService.createExposingMatchDto(handballMatch);

        assertInstanceOf(ResponseMatchDto.class, result);
        assertEquals(result.getTeamA(), teamA);
        assertEquals(result.getTeamB(), teamB);
    }

    @Test
    @DisplayName("Should update MatchStatus when valid Status is passed")
    void Should_UpdateMatchStatus_When_ValidStatusIsPassed() {

        var dto = MatchTestUtil.createNewMatchDto(
                Sports.HANDBALL, teamA, teamB, playersIds, handballEvent.getId()
        );
        var handballMatch = matchService.saveMatch(dto);

        handballMatch.setMatchStatus(Status.ENDED);
        handballEvent.setEventStatus(Status.IN_PROGRESS);
        entityManager.merge(handballEvent);

        var originalStatus = handballMatch.getMatchStatus();
        var result = matchService.updateMatchStatus(handballMatch.getId(), Status.OPEN_FOR_EDITS);

        assertNotEquals(result.getMatchStatus(), originalStatus);
        assertEquals(result.getMatchStatus(), Status.OPEN_FOR_EDITS);
    }

    @Test
    @DisplayName("Should throw UnprocessableEntityException when invalid Match Status is passed")
    void Should_ThrowUnprocessableEntityException_When_InvalidMatchStatusIsPassed() {

        var dto = MatchTestUtil.createNewMatchDto(
                Sports.FUTSAL, teamA, teamB, playersIds, futsalEvent.getId(), futsalEvent.getModality()
        );
        var futsalMatch = matchService.saveMatch(dto);
        var id = futsalMatch.getId();

        assertThrows(UnprocessableEntityException.class, () -> matchService.updateMatchStatus(id, Status.ENDED));
        assertThrows(UnprocessableEntityException.class, () -> matchService.updateMatchStatus(id, Status.OPEN_FOR_EDITS));
    }

    @Test
    @DisplayName("Should throw BadRequestException when trying to update Match Status on not in progress SportEvent")
    void Should_ThrowBadRequestException_When_TryingToUpdateMatchStatusOnNotInProgressSportEvent() {

        futsalEvent.setEventStatus(Status.SCHEDULED);
        entityManager.merge(futsalEvent);

        var dto = MatchTestUtil.createNewMatchDto(
                Sports.FUTSAL, teamA, teamB, playersIds, futsalEvent.getId(), futsalEvent.getModality()
        );
        var futsalMatch = matchService.saveMatch(dto);
        var id = futsalMatch.getId();

        assertThrows(BadRequestException.class, () -> matchService.updateMatchStatus(id, Status.IN_PROGRESS));
    }

}