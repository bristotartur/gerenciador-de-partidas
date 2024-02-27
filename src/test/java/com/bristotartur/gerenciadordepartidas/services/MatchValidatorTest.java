package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.dtos.input.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.*;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.services.matches.MatchValidator;
import com.bristotartur.gerenciadordepartidas.utils.MatchTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.ParticipantTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.SportEventTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
class MatchValidatorTest {

    private Team teamA;
    private Team teamB;
    private Team teamC;
    private SportEvent event;

    @BeforeEach
    void setUp() {

        teamA = Team.ATOMICA;
        teamB = Team.MESTRES_DE_OBRAS;
        teamC = Team.PAPA_LEGUAS;

        event = SportEventTestUtil.createNewSportEvent(Sports.FUTSAL, Modality.FEMININE, Status.SCHEDULED, 6);
    }

    @Test
    @DisplayName("Should not throw anything when trying to create a Match with two different Teams")
    void Should_NotThrowAnything_When_TryingToCreateMatchWithTwoDifferentTeams() {

        var dto = MatchTestUtil.createNewMatchDto(any(), teamA, teamB, any(), any());
        assertDoesNotThrow(() -> MatchValidator.checkTeamsForMatch(dto));
    }

    @Test
    @DisplayName("Should throw BadRequestException when trying to create a Match with two equal Teams")
    void Should_ThrowBadRequestException_When_TryingToCreateMatchWithTwoEqualTeams() {

        var dto = MatchTestUtil.createNewMatchDto(any(), teamA, teamA, any(), any());
        assertThrows(BadRequestException.class, () -> MatchValidator.checkTeamsForMatch(dto));
    }

    @Test
    @DisplayName("Should not throw anything when Match Sport and Modality are the same as those of its Event")
    void Should_NotThrowAnything_When_MatchSportAndModalityAreTheSameAsThoseOfItsEvent() {

        var dto = MatchTestUtil.createNewMatchDto(event.getType(), teamA, teamA, any(), any(), event.getModality());
        assertDoesNotThrow(() -> MatchValidator.checkMatchForSportEvent(event, dto));
    }

    @Test
    @DisplayName("Should throw BadRequestException when Match Sport and Modality are the same as those of its Event")
    void Should_ThrowBadRequestException_When_MatchSportAndModalityAreNotTheSameAsThoseOfItsEvent() {

        var dto = MatchTestUtil.createNewMatchDto(Sports.BASKETBALL, teamA, teamA, any(), any(), Modality.MIXED);
        assertThrows(BadRequestException.class, () -> MatchValidator.checkMatchForSportEvent(event, dto));
    }

    @Test
    @DisplayName("Should not throw anything when Matches with other importances are finished")
    void Should_NotThrowAnything_When_MatchesWithOtherImportancesAreFinished() {

        var matches = List.of(
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.NORMAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.SEMIFINAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.THIRD_PLACE_PLAYOFF)
        );
        matches.forEach(match -> match.setMatchStatus(Status.ENDED));

        assertDoesNotThrow(() -> MatchValidator.checkMatchesForImportance(matches, Importance.NORMAL));
        assertDoesNotThrow(() -> MatchValidator.checkMatchesForImportance(matches, Importance.SEMIFINAL));
        assertDoesNotThrow(() -> MatchValidator.checkMatchesForImportance(matches, Importance.THIRD_PLACE_PLAYOFF));
        assertDoesNotThrow(() -> MatchValidator.checkMatchesForImportance(matches, Importance.FINAL));
    }

    @Test
    @DisplayName("Should throw BadRequestException when Matches with other importances are unfinished")
    void Should_ThrowBadRequestException_When_MatchesWithOtherImportancesAreUnfinished() {

        var matches = List.of(
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.NORMAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.SEMIFINAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.THIRD_PLACE_PLAYOFF)
        );

        assertThrows(BadRequestException.class, () -> MatchValidator.checkMatchesForImportance(matches, Importance.NORMAL));
        assertThrows(BadRequestException.class, () -> MatchValidator.checkMatchesForImportance(matches, Importance.SEMIFINAL));
        assertThrows(BadRequestException.class, () -> MatchValidator.checkMatchesForImportance(matches, Importance.THIRD_PLACE_PLAYOFF));
        assertThrows(BadRequestException.class, () -> MatchValidator.checkMatchesForImportance(matches, Importance.FINAL));
    }

    @Test
    @DisplayName("Should throw BadRequestException when cannot create more noraml Matches")
    void Should_ThrowBadRequestException_When_CannotCreateMoreNormalMatches() {

        var matches = List.of(
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.NORMAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.NORMAL)
        );
        matches.forEach(match -> match.setMatchStatus(Status.ENDED));
        event.setMatches(matches);

        var importance = Importance.NORMAL;
        var dto = MatchDto.builder().matchImportance(importance).build();

        assertThatThrownBy(() -> MatchValidator.checKMatchImportance(event, dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessages.INVALID_MATCH_IMPORTANCE.message.formatted(importance));
    }

    @Test
    @DisplayName("Should throw BadRequestException when cannot create more semifinal Matches")
    void Should_ThrowBadRequestException_When_CannotCreateMoreSemifinalMatches() {

        var event2 = event;
        var matchesA = List.of(
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.NORMAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.NORMAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.SEMIFINAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.SEMIFINAL)
        );
        var matchesB = List.of(
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.NORMAL)
        );
        matchesA.forEach(match -> match.setMatchStatus(Status.ENDED));
        matchesB.forEach(match -> match.setMatchStatus(Status.ENDED));

        event.setMatches(matchesA);
        event2.setMatches(matchesB);
        var importance = Importance.SEMIFINAL;
        var dto = MatchDto.builder().matchImportance(importance).build();

        assertThatThrownBy(() -> MatchValidator.checKMatchImportance(event, dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessages.INVALID_MATCH_IMPORTANCE.message.formatted(importance));

        assertThatThrownBy(() -> MatchValidator.checKMatchImportance(event2, dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessages.INVALID_MATCH_IMPORTANCE.message.formatted(importance));
    }

    @Test
    @DisplayName("Should throw BadRequestException when cannot create more third place playoff Matches")
    void Should_ThrowBadRequestException_When_CannotCreateMoreThirdPlacePlayoffMatches() {

        var matches = List.of(
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.NORMAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.NORMAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.SEMIFINAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.SEMIFINAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.THIRD_PLACE_PLAYOFF)
        );
        matches.forEach(match -> match.setMatchStatus(Status.ENDED));
        event.setMatches(matches);

        var importance = Importance.THIRD_PLACE_PLAYOFF;
        var dto = MatchDto.builder().matchImportance(importance).build();

        assertThatThrownBy(() -> MatchValidator.checKMatchImportance(event, dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessages.INVALID_MATCH_IMPORTANCE.message.formatted(importance));
    }

    @Test
    @DisplayName("Should throw BadRequestException when cannot create more final Matches")
    void Should_ThrowBadRequestException_When_CannotCreateMoreFinalMatches() {

        var matches = List.of(
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.NORMAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.NORMAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.SEMIFINAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.SEMIFINAL),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.THIRD_PLACE_PLAYOFF),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Importance.FINAL)
        );
        matches.forEach(match -> match.setMatchStatus(Status.ENDED));
        event.setMatches(matches);

        var importance = Importance.FINAL;
        var dto = MatchDto.builder().matchImportance(importance).build();

        assertThatThrownBy(() -> MatchValidator.checKMatchImportance(event, dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessages.INVALID_MATCH_IMPORTANCE.message.formatted(importance));
    }

    @Test
    @DisplayName("Should not thow anything when trying to update Match status on SportEvent in progress")
    void Should_NotThrowAnything_When_TryingToUpdateMatchStatusOnSportEventInProgress() {

        event.setEventStatus(Status.IN_PROGRESS);
        var matches = List.of(
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Status.ENDED)
        );
        event.setMatches(matches);

        assertDoesNotThrow(() -> MatchValidator.checkMatchStatus(event, Status.IN_PROGRESS));
    }

    @Test
    @DisplayName("Should throw BadRequestException when trying to update Match status on SportEvent not in progress")
    void Should_ThrowBadRequestException_When_TryingToUpdateMatchStatusOnSportEventnotinProgress() {

        event.setEventStatus(Status.SCHEDULED);
        assertThrows(BadRequestException.class, () -> MatchValidator.checkMatchStatus(event, Status.IN_PROGRESS));
    }

    @Test
    @DisplayName("Should not throw anything when there are no Matches in progress")
    void Should_NotThrowAnything_When_ThereAreNoMatchesInProgress() {

        event.setEventStatus(Status.IN_PROGRESS);
        var matches = List.of(
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Status.ENDED)
        );
        event.setMatches(matches);

        assertDoesNotThrow(() -> MatchValidator.checkMatchStatus(event, Status.IN_PROGRESS));
    }

    @Test
    @DisplayName("Should throw BadRequestException when thre is already a Match in progress")
    void Should_ThrowBadRequestException_When_ThereIsAlreadyMatchinProgress() {

        event.setEventStatus(Status.IN_PROGRESS);
        var matches = List.of(
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Status.IN_PROGRESS)
        );
        event.setMatches(matches);

        assertThrows(BadRequestException.class, () -> MatchValidator.checkMatchStatus(event, Status.IN_PROGRESS));
    }

    @Test
    @DisplayName("Should not throw anything when players are from one of the Teams in in the Match")
    void Should_NotThrowAnything_When_PlayersInMatchAreFromOneOfTheTeamsInTheMatch() {

        var players = List.of(
                ParticipantTestUtil.createNewParticipant(any(), teamA, any()),
                ParticipantTestUtil.createNewParticipant(any(), teamB, any())
        );
        var dto = MatchDto.builder()
                .teamA(teamA)
                .teamB(teamB).build();

        assertDoesNotThrow(() -> MatchValidator.checkPlayersForMatch(players, dto));
    }

    @Test
    @DisplayName("Should throw BadRequest exception when a player in Match is from a Team that is not in the Match")
    void Should_ThrowBadRequestException_When_PlayerInMatchIsFromTeamThatIsNotInMatch() {

        var players = List.of(
                ParticipantTestUtil.createNewParticipant(any(), teamA, any()),
                ParticipantTestUtil.createNewParticipant(any(), teamC, any())
        );
        players.forEach(player -> player.setId(getRandomLongId()));

        var invalidPlayerId = players.get(1).getId();
        var dto = MatchDto.builder()
                .teamA(teamA)
                .teamB(teamB).build();

        assertThatThrownBy(() -> MatchValidator.checkPlayersForMatch(players, dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessages.PARTICIPANT_INVALID_FOR_MATCH.message.formatted(invalidPlayerId));
    }

    @Test
    @DisplayName("Should throw BadRequestException when all Match players are from the same Team")
    void Should_ThrowBadRequestException_When_AllMatchPlayersAreFromTheSameTeam() {

        var playersFromTeamA = List.of(
                ParticipantTestUtil.createNewParticipant(any(), teamA, any()),
                ParticipantTestUtil.createNewParticipant(any(), teamA, any())
        );
        var playersFromTeamB = List.of(
                ParticipantTestUtil.createNewParticipant(any(), teamB, any()),
                ParticipantTestUtil.createNewParticipant(any(), teamB, any())
        );
        playersFromTeamA.forEach(player -> player.setId(getRandomLongId()));
        playersFromTeamB.forEach(player -> player.setId(getRandomLongId()));

        var dto = MatchDto.builder()
                .teamA(teamA)
                .teamB(teamB).build();

        assertThatThrownBy(() -> MatchValidator.checkPlayersForMatch(playersFromTeamA, dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessages.PLAYERS_FROM_SINGLE_TEAM_IN_MATCH.message.formatted(teamA));

        assertThatThrownBy(() -> MatchValidator.checkPlayersForMatch(playersFromTeamB, dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessages.PLAYERS_FROM_SINGLE_TEAM_IN_MATCH.message.formatted(teamB));
    }

}