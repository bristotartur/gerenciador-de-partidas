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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
class SportEventValidatorTest {

    private SportEvent event;
    private Edition edition;
    private List<Match> matches = new LinkedList<>();

    @BeforeEach
    void setUp() {
        edition = EditionTestUtil.createNewEdition(Status.SCHEDULED);
        edition.setId(1L);

        matches.addAll(List.of(
                MatchTestUtil.createNewMatch(any(), any(), any(), Status.SCHEDULED),
                MatchTestUtil.createNewMatch(any(), any(), any(), Status.SCHEDULED)));

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

        var dto = SportEventTestUtil.createNewSportEventDto(Sports.TABLE_TENNIS, Modality.MASCULINE, Status.SCHEDULED, 12, 1L);

        assertDoesNotThrow(() -> SportEventValidator.checkSportEventForEdition(events, dto));
    }

    @Test
    @DisplayName("Should throw BadRequestException when DTO with not unique Type and Modality in Edition is passed")
    void Should_ThrowBadRequestException_When_DtoWithNotUniqueTypeAndModalityInEditionIsPassed() {

        var events = List.of(
                SportEventTestUtil.createNewSportEvent(Sports.FUTSAL, Modality.MASCULINE, edition),
                SportEventTestUtil.createNewSportEvent(Sports.FUTSAL, Modality.FEMININE, edition));

        var dto = SportEventTestUtil.createNewSportEventDto(Sports.FUTSAL, Modality.MASCULINE, Status.SCHEDULED, 12, 1L);

        assertThrows(BadRequestException.class, () -> SportEventValidator.checkSportEventForEdition(events, dto));
    }

    @Test
    @DisplayName("Should not throw anything when DTO with no changes in SportEvent is passed")
    void Should_NotThrowAnything_When_DtoWithNoChangesInSportEventIsPassed() {

        var type = event.getType();
        var modality = event.getModality();
        var status = event.getEventStatus();
        var totalMatches = event.getTotalMatches();
        var editionId = event.getEdition().getId();

        var dtoSameStatus = SportEventTestUtil.createNewSportEventDto(type, modality, status, totalMatches, editionId);
        var dtoNewStatus = SportEventTestUtil.createNewSportEventDto(type, modality, Status.IN_PROGRESS, totalMatches, editionId);

        assertDoesNotThrow(() -> SportEventValidator.checkDtoForUpdate(event, dtoSameStatus));
        assertDoesNotThrow(() -> SportEventValidator.checkDtoForUpdate(event, dtoNewStatus));
    }

    @Test
    @DisplayName("Should not throw anything when DTO with changes in scheduled SportEvent is passed")
    void Should_NotThrowAnything_When_DtoWithChangesInScheduledSportEventIsPassed() {

        var type = event.getType();
        var modality = event.getModality();
        var editionId = event.getEdition().getId();

        var dto = SportEventTestUtil.createNewSportEventDto(type, modality, Status.IN_PROGRESS, 13, editionId);

        assertDoesNotThrow(() -> SportEventValidator.checkDtoForUpdate(event, dto));
    }

    @Test
    @DisplayName("Should throw BadRequestException when trying to update unscheduled SportEvent fields")
    void Should_ThrowBadRequestException_When_TryingToUpdateUnscheduledSportEventFields() {

        event.setEventStatus(Status.IN_PROGRESS);
        var dto = SportEventTestUtil.createNewSportEventDto(Sports.FUTSAL, Modality.MASCULINE, Status.IN_PROGRESS, 12, 1L);

        assertThrows(BadRequestException.class, () -> SportEventValidator.checkDtoForUpdate(event, dto));
    }

    @Test
    @DisplayName("Should throw BadRequestException whe registerd Matches exceeds the limit of new total matches value")
    void Should_ThrowBadRequestException_When_RegisteredMatchesExceedsTheLimitOfNewTotalMatchesValue() {

        var dto = SportEventTestUtil.createNewSportEventDto(any(), any(), Status.SCHEDULED, 5, any());

        assertThrows(BadRequestException.class, () -> SportEventValidator.checkMatchesToUpdateEvent(event, dto));
    }

    @Test
    @DisplayName("Should throw BadRequestException when trying to start SportEvent with no sufficient matches")
    void Should_ThrowBadRequestException_When_TryingToStartSportEventWithNoSufficientMatches() {

        var dto = SportEventTestUtil.createNewSportEventDto(any(), any(), Status.IN_PROGRESS, 7, any());

        assertThrows(BadRequestException.class, () -> SportEventValidator.checkMatchesToUpdateEvent(event, dto));
    }

    @Test
    @DisplayName("Should throw BadRequestException when trying to finish SportEvent with no sufficient matches")
    void Should_ThrowBadRequestException_When_TryingToFinishSportEventWithNoSufficientMatches() {

        matches.addAll(List.of(
                MatchTestUtil.createNewMatch(any(), any(), any(), Status.SCHEDULED),
                MatchTestUtil.createNewMatch(any(), any(), any(), Status.SCHEDULED),
                MatchTestUtil.createNewMatch(any(), any(), any(), Status.SCHEDULED)));

        event.setMatches(matches);
        var dto = SportEventTestUtil.createNewSportEventDto(any(), any(), Status.ENDED, 6, any());

        assertThrows(BadRequestException.class, () -> SportEventValidator.checkMatchesToUpdateEvent(event, dto));
    }

    @Test
    @DisplayName("Should throw BadRequestException when trying to finish SportEvent with unfinished matches")
    void Should_ThrowBadRequestException_When_TryingToFinishSportEventWithUnfinishedMatches() {

        matches.add(MatchTestUtil.createNewMatch(any(), any(), any(), Status.SCHEDULED));
        event.setMatches(matches);
        var dto = SportEventTestUtil.createNewSportEventDto(any(), any(), Status.ENDED, 6, any());

        assertThrows(BadRequestException.class, () -> SportEventValidator.checkMatchesToUpdateEvent(event, dto));

    }

}