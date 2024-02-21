package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.services.events.SportEventValidator;
import com.bristotartur.gerenciadordepartidas.utils.EditionTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.MatchTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.SportEventTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
class SportEventValidatorTest {

    private SportEvent event;
    private Edition edition;
    private final List<Match> matches = new LinkedList<>();

    @BeforeEach
    void setUp() {
        edition = EditionTestUtil.createNewEdition(Status.SCHEDULED);
        edition.setId(1L);

        matches.addAll(List.of(
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Status.SCHEDULED),
                MatchTestUtil.createNewMatch(any(), any(), any(), any(), Status.SCHEDULED)));

        event = SportEventTestUtil.createNewSportEvent(Sports.HANDBALL, Modality.MASCULINE, Status.SCHEDULED, 6);
        event.setEdition(edition);
        event.setMatches(matches);
    }

    @Test
    @DisplayName("Should not throw anything when DTO with unique Type and Modality in Edition is passed")
    void Should_NotThrowAnything_When_DtoWithUniqueTypeAndModalityInEditionIsPassed() {

        var events = List.of(
                SportEventTestUtil.createNewSportEvent(Sports.CHESS, Modality.MIXED, edition),
                SportEventTestUtil.createNewSportEvent(Sports.VOLLEYBALL, Modality.MIXED, edition));

        var dto = SportEventTestUtil.createNewSportEventDto(Sports.TABLE_TENNIS, Modality.MASCULINE, 12, 1L);

        assertDoesNotThrow(() -> SportEventValidator.checkSportEventForEdition(events, dto));
    }

    @Test
    @DisplayName("Should throw BadRequestException when DTO with not unique Type and Modality in Edition is passed")
    void Should_ThrowBadRequestException_When_DtoWithNotUniqueTypeAndModalityInEditionIsPassed() {

        var events = List.of(
                SportEventTestUtil.createNewSportEvent(Sports.FUTSAL, Modality.MASCULINE, edition),
                SportEventTestUtil.createNewSportEvent(Sports.FUTSAL, Modality.FEMININE, edition));

        var dto = SportEventTestUtil.createNewSportEventDto(Sports.FUTSAL, Modality.MASCULINE, 12, 1L);

        assertThrows(BadRequestException.class, () -> SportEventValidator.checkSportEventForEdition(events, dto));
    }

    @Test
    @DisplayName("Should not throw anything when scheduled SportEvent is passed")
    void Should_NotThrowAnything_When_ScheduledSportEventIsPassed() {
        assertDoesNotThrow(() -> SportEventValidator.checkSportEventForUpdate(event));
    }

    @Test
    @DisplayName("Should throw BadRequestException when unscheduled SportEvent is passed")
    void Should_ThrowBadRequestException_When_UnscheduledSportEventIsPassed() {
        event.setEventStatus(Status.IN_PROGRESS);
        assertThrows(BadRequestException.class, () -> SportEventValidator.checkSportEventForUpdate(event));
    }

    @Test
    @DisplayName("Should not throw anything when registerd Matches does not exceeds the limit of new total matches value")
    void Should_NotThrowAnything_When_RegisteredMatchesDoesNotExceedsTheLimitOfNewTotalMatchesValue() {
        assertDoesNotThrow(() -> SportEventValidator.checkNewTotalMatchesForSportEvent(event, 7));
    }

    @Test
    @DisplayName("Should throw BadRequestException when registerd Matches exceeds the limit of new total matches value")
    void Should_ThrowBadRequestException_When_RegisteredMatchesExceedsTheLimitOfNewTotalMatchesValue() {
        assertThrows(BadRequestException.class, () -> SportEventValidator.checkNewTotalMatchesForSportEvent(event, 5));
    }

    @Test
    @DisplayName("Should not throw anything when trying to start SportEvent with sufficient matches")
    void Should_NotThrowAnything_When_TryingToStartSportEventWithSufficientMatches() {
        assertDoesNotThrow(() -> SportEventValidator.checkSportEventToUpdateStatus(event, Status.IN_PROGRESS, Status.IN_PROGRESS));
    }


    @Test
    @DisplayName("Should throw BadRequestException when trying to start SportEvent with no sufficient matches")
    void Should_ThrowBadRequestException_When_TryingToStartSportEventWithNoSufficientMatches() {

        event.setTotalMatches(7);
        event.setEventStatus(Status.IN_PROGRESS);

        assertThrows(BadRequestException.class, () -> SportEventValidator.checkSportEventToUpdateStatus(event, Status.ENDED, Status.IN_PROGRESS));
    }

    @Test
    @DisplayName("Should not throw anything when trying to finish SportEvent with sufficient matches")
    void Should_NotThrowanything_When_TryingToFinishSportEventWithSufficientMatches() {

        matches.forEach(match -> match.setMatchStatus(Status.ENDED));
        matches.addAll(List.of(
                MatchTestUtil.createNewMatch(any(), any(), any(), event, Status.ENDED),
                MatchTestUtil.createNewMatch(any(), any(), any(), event, Status.ENDED),
                MatchTestUtil.createNewMatch(any(), any(), any(), event, Status.ENDED),
                MatchTestUtil.createNewMatch(any(), any(), any(), event, Status.ENDED)));

        event.setMatches(matches);
        event.setTotalMatches(6);
        event.setEventStatus(Status.IN_PROGRESS);

        assertDoesNotThrow(() -> SportEventValidator.checkSportEventToUpdateStatus(event, Status.ENDED, Status.IN_PROGRESS));
    }

    @Test
    @DisplayName("Should throw BadRequestException when trying to finish SportEvent with no sufficient matches")
    void Should_ThrowBadRequestException_When_TryingToFinishSportEventWithNoSufficientMatches() {

        matches.remove(0);
        event.setMatches(matches);

        assertThrows(BadRequestException.class, () -> SportEventValidator.checkSportEventToUpdateStatus(event, Status.ENDED, Status.IN_PROGRESS));
    }

    @Test
    @DisplayName("Should throw BadRequestException when trying to finish SportEvent with unfinished matches")
    void Should_ThrowBadRequestException_When_TryingToFinishSportEventWithUnfinishedMatches() {

        matches.add(MatchTestUtil.createNewMatch(any(), any(), any(), event,Status.IN_PROGRESS));
        matches.forEach(match -> match.setMatchStatus(Status.IN_PROGRESS));
        event.setMatches(matches);

        assertThrows(BadRequestException.class, () -> SportEventValidator.checkSportEventToUpdateStatus(event, Status.ENDED, Status.IN_PROGRESS));
    }

}