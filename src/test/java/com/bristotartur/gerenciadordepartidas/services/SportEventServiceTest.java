package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.enums.*;
import com.bristotartur.gerenciadordepartidas.exceptions.ConflictException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.exceptions.UnprocessableEntityException;
import com.bristotartur.gerenciadordepartidas.repositories.SportEventRepository;
import com.bristotartur.gerenciadordepartidas.services.events.SportEventService;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SportEventServiceTest {

    @Autowired
    private SportEventService sportEventService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private SportEventRepository sportEventRepository;

    private SportEvent sportEventA;
    private SportEvent sportEventB;
    private Edition edition;
    private final List<Participant> participants = new LinkedList<>();

    @BeforeEach
    void setUp() {

        edition = EditionTestUtil.createNewEdition(Status.SCHEDULED, entityManager);
        sportEventA = SportEventTestUtil.createNewSportEvent(Sports.TABLE_TENNIS, Modality.MASCULINE, Status.SCHEDULED, 6, edition);
        sportEventB = SportEventTestUtil.createNewSportEvent(Sports.TABLE_TENNIS, Modality.FEMININE, Status.SCHEDULED, 6, edition);

        participants.addAll(List.of(
                ParticipantTestUtil.createNewParticipant("2-53", Team.PAPA_LEGUAS, edition, entityManager),
                ParticipantTestUtil.createNewParticipant("2-13", Team.TWISTER, edition, entityManager)
        ));
    }

    @Test
    @DisplayName("Should retrieve all SportEvents in paged form when searching for all SportEvents")
    void Should_RetrieveAllSportEventsInPagedForm_When_SearchingForAllSportEvents() {

        var pageable = PageRequest.of(0, 2);

        var events = List.of(sportEventA, sportEventB);
        events.forEach(entityManager::merge);

        var eventPage = new PageImpl<>(events, pageable, events.size());
        var result = sportEventService.findAllEvents(pageable);

        assertEquals(result.getContent(), eventPage.getContent());
        assertEquals(result.getTotalPages(), eventPage.getTotalPages());
        assertEquals(result.getSize(), eventPage.getSize());
    }

    @Test
    @DisplayName("Should retrieve all SportEvents of Edition in paged form when searching for SportEvents of specific Edition")
    void Should_RetrieveAllEventsOfSpecificEditionInPagedForm_When_SearchingSportEventsOfSpecifEdition() {

        var pageable = PageRequest.of(0, 3);

        var otherEdition = EditionTestUtil.createNewEdition(Status.SCHEDULED, entityManager);
        var otherEvent = SportEventTestUtil.createNewSportEvent(Sports.TABLE_TENNIS, Modality.MASCULINE, Status.SCHEDULED, 6, otherEdition);

        var events = List.of(sportEventA, sportEventB, otherEvent);
        events.forEach(entityManager::merge);

        var eventPage = new PageImpl<>(events, pageable, events.size());
        var result = sportEventService.findAllEventsFromEdition(edition.getId(), pageable);

        assertFalse(result.getContent().isEmpty());
        assertNotEquals(result.getContent(), eventPage.getContent());
        assertEquals(result.getTotalPages(), eventPage.getTotalPages());
        assertEquals(result.getSize(), eventPage.getSize());
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing Edition ID is passed to any method")
    void Should_ThrownotFoundException_When_NonExistingEditionIdisPassed() {

        var pageable = PageRequest.of(0, 3);
        var id = getRandomLongId();
        var dto = SportEventTestUtil.createNewSportEventDto(any(), any(), any(), any());

        assertThrows(NotFoundException.class, () -> sportEventService.findAllEventsFromEdition(id, pageable));
        assertThrows(NotFoundException.class, () -> sportEventService.findEventById(id));
        assertThrows(NotFoundException.class, () -> sportEventService.findEventAndCheckStatus(id));
        assertThrows(NotFoundException.class, () -> sportEventService.deleteEventById(id));
        assertThrows(NotFoundException.class, () -> sportEventService.replaceEvent(id, dto));
    }

    @Test
    @DisplayName("Should retrieve all SportEvents of a type in paged form when searching for SportEvents of specific type")
    void Should_RetrieveAllSportEventsOfTypeInPagedForm_When_SearchingForSportEventsOfSpecificType() {

        var pageable = PageRequest.of(0, 3);

        var otherEvent = SportEventTestUtil.createNewSportEvent(Sports.CHESS, Modality.MIXED, Status.SCHEDULED, 6, edition);
        var events = List.of(sportEventA, sportEventB, otherEvent);
        events.forEach(entityManager::merge);

        var eventPage = new PageImpl<>(events, pageable, events.size());
        var result = sportEventService.findAllEventsOfType(Sports.TABLE_TENNIS, pageable);

        assertFalse(result.getContent().isEmpty());
        assertNotEquals(result.getContent(), eventPage.getContent());
        assertEquals(result.getTotalPages(), eventPage.getTotalPages());
        assertEquals(result.getSize(), eventPage.getSize());
    }

    @Test
    @DisplayName("Should retrieve all Participants of SportEvent when searching for all Participants of SportEvent")
    void Should_RetrieveAllParticipantsOfSportEvent_When_SearchingForAllParticipantsOfSportEvent() {

        var pageable = PageRequest.of(0, 2);
        sportEventA.setParticipants(participants);
        entityManager.merge(sportEventA);

        var participantPage = new PageImpl<>(participants, pageable, participants.size());
        var result = sportEventService.findParticipantsFromEvent(sportEventA.getId(), pageable);

        assertEquals(result.getContent(), participants);
        assertEquals(result.getTotalPages(), participantPage.getTotalPages());
        assertEquals(result.getSize(), participantPage.getSize());
    }

    @Test
    @DisplayName("Should find SportEvent when existing SportEvent ID is passed to search")
    void Should_FindSportEvent_When_ExistingSportEventIdIsPassedToSearch() {

        entityManager.merge(sportEventA);
        var result = sportEventService.findEventById(sportEventA.getId());

        assertEquals(result, sportEventA);
    }

    @Test
    @DisplayName("Should not throw anything when SportEvent with valid Status for Match is find")
    void Should_NotThrowAnything_When_SportEventWithValidStatusForMatchIsFind() {

        entityManager.merge(sportEventA);

        assertDoesNotThrow(() -> sportEventService.findEventAndCheckStatus(sportEventA.getId()));
    }

    @Test
    @DisplayName("Should throw UnprocessableEntityException when SportEvent with invalid Status for Match is find")
    void Should_ThrowUnprocessableEntityException_When_SportEventWithInvalidStatusForMatchIsFind() {

        sportEventA.setEventStatus(Status.ENDED);
        entityManager.merge(sportEventA);

        assertThrows(UnprocessableEntityException.class, () -> sportEventService.findEventAndCheckStatus(sportEventA.getId()));
    }

    @Test
    @DisplayName("Should save SportEvent when valid DTO is passed")
    void Should_SaveSportEvent_When_ValidDtoIsPassed() {

        var total = 12;
        var editionId = edition.getId();
        var dto = SportEventTestUtil.createNewSportEventDto(Sports.VOLLEYBALL, Modality.MIXED, total, editionId);

        var result = sportEventService.saveEvent(dto);

        assertTrue(sportEventRepository.findById(result.getId()).isPresent());
    }

    @Test
    @DisplayName("Should throw UnprocessableEntityException when trying to create or update SportEvent in finished Edition")
    void Should_ThrowUnprocessableEntityException_When_TryingToCreateOrUpdateSportEventInFinishedEdition() {

        var finishedEdition = EditionTestUtil.createNewEdition(Status.ENDED, entityManager);
        entityManager.merge(sportEventA);

        var total = 12;
        var editionId = finishedEdition.getId();
        var dto = SportEventTestUtil.createNewSportEventDto(Sports.VOLLEYBALL, Modality.MIXED, total, editionId);

        assertThrows(UnprocessableEntityException.class, () -> sportEventService.saveEvent(dto));
        assertThrows(UnprocessableEntityException.class, () -> sportEventService.replaceEvent(sportEventA.getId(), dto));
    }

    @Test
    @DisplayName("Should throw ConflictException when DTO with not unique Type and Modality in Edition is passed")
    void Should_ThrowConflictException_When_DtoWithNotUniqueTypeAndModalityInEditionIsPassed() {

        entityManager.merge(sportEventA);
        entityManager.merge(sportEventB);

        var total = 12;
        var editionId = edition.getId();
        var type = sportEventA.getType();
        var modality = sportEventA.getModality();

        var dto = SportEventTestUtil.createNewSportEventDto(type, modality, total, editionId);

        assertThrows(ConflictException.class, () -> sportEventService.saveEvent(dto));
        assertThrows(ConflictException.class, () -> sportEventService.replaceEvent(sportEventB.getId(), dto));
    }

    @Test
    @DisplayName("Should delete SportEvent when valid SportEvent ID is passed")
    void Should_DeleteSportEvent_When_ValidSportEventIdIsPassed() {

        entityManager.merge(sportEventB);
        var id = sportEventB.getId();

        sportEventService.deleteEventById(id);

        assertTrue(sportEventRepository.findById(id).isEmpty());
    }

    @Test
    @DisplayName("Should throw UnprocessableEntityException when trying to modify not scheduled SportEvent")
    void Should_ThrowUnprocessableEntityException_When_TryingToModifyNotScheduledSportEvent() {

        sportEventB.setEventStatus(Status.IN_PROGRESS);
        entityManager.merge(sportEventB);

        var id = sportEventB.getId();
        var total = 12;
        var editionId = edition.getId();

        var dto = SportEventTestUtil.createNewSportEventDto(Sports.VOLLEYBALL, Modality.MIXED, total, editionId);

        assertThrows(UnprocessableEntityException.class, () -> sportEventService.deleteEventById(id));
        assertThrows(UnprocessableEntityException.class, () -> sportEventService.replaceEvent(id, dto));
    }

    @Test
    @DisplayName("Should update SportEvent when valid DTO is passed")
    void Should_UpdateSportEvent_When_ValidDtoIsPassed() {

        var matches = List.of(
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventA, Status.SCHEDULED),
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventA, Status.SCHEDULED));

        sportEventA.setMatches(matches);
        entityManager.merge(sportEventA);

        var id = sportEventA.getId();
        var total = 12;
        var editionId = edition.getId();

        var dto = SportEventTestUtil.createNewSportEventDto(Sports.VOLLEYBALL, Modality.MIXED, total, editionId);
        var result = sportEventService.replaceEvent(id, dto);

        assertNotEquals(result, sportEventA);
    }

    @Test
    @DisplayName("Should not throw anything when valid Status is passed")
    void Should_NotThrowAnything_When_ValidStatusIsPassed() {

        var editionMock = EditionTestUtil.createNewEdition(Status.IN_PROGRESS, entityManager);
        var sportEventC = SportEventTestUtil.createNewSportEvent(
                Sports.HANDBALL, Modality.FEMININE, Status.SCHEDULED, editionMock, entityManager
        );
        var matches = List.of(
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventC, Status.SCHEDULED),
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventC, Status.SCHEDULED),
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventC, Status.SCHEDULED),
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventC, Status.SCHEDULED));

        matches.forEach(entityManager::merge);
        sportEventC.setMatches(matches);
        sportEventC.setTotalMatches(6);
        entityManager.merge(sportEventC);

        assertDoesNotThrow(() -> sportEventService.updateEventStatus(sportEventC.getId(), Status.IN_PROGRESS));
    }

    @Test
    @DisplayName("Should throw UnprocessableEntityException when invalid Status is passed")
    void Should_ThrowUnprocessableEntityException_When_InvalidStatusIsPassed() {

        var editionMock = EditionTestUtil.createNewEdition(Status.IN_PROGRESS, entityManager);
        var sportEventC = SportEventTestUtil.createNewSportEvent(
                Sports.HANDBALL, Modality.MASCULINE, Status.SCHEDULED, editionMock, entityManager
        );
        var message = ExceptionMessages.CANNOT_UPDATE_STATUS.message;

        assertThatThrownBy(() -> sportEventService.updateEventStatus(sportEventC.getId(), Status.ENDED))
                .isInstanceOf(UnprocessableEntityException.class)
                .hasMessage(message.formatted(Status.SCHEDULED, Status.IN_PROGRESS));

        assertThatThrownBy(() -> sportEventService.updateEventStatus(sportEventC.getId(), Status.OPEN_FOR_EDITS))
                .isInstanceOf(UnprocessableEntityException.class)
                .hasMessage(message.formatted(Status.SCHEDULED, Status.IN_PROGRESS));
    }

    @Test
    @DisplayName("Should throw UnprocessableEntityException when trying to start SportEvent with no sufficient matches")
    void Should_ThrowUnprocessableEntityException_When_TryingToStartSportEventWithNoSufficientMatches() {

        var matches = List.of(
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventA, Status.SCHEDULED));

        sportEventA.setMatches(matches);
        entityManager.merge(sportEventA);

        var id = sportEventA.getId();

        assertThrows(UnprocessableEntityException.class, () -> sportEventService.updateEventStatus(id, Status.IN_PROGRESS));
    }

    @Test
    @DisplayName("Should throw UnprocessableEntityException when trying to finish SportEvent with no sufficient matches")
    void Should_ThrowUnprocessableEntityException_When_TryingToFinishSportEventWithNoSufficientMatches() {

        var matches = List.of(
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventA, Status.ENDED),
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventA, Status.IN_PROGRESS),
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventA, Status.ENDED),
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventA, Status.ENDED));

        sportEventA.setMatches(matches);
        sportEventA.setEventStatus(Status.IN_PROGRESS);
        entityManager.merge(sportEventA);

        var id = sportEventA.getId();
        assertThrows(UnprocessableEntityException.class, () -> sportEventService.updateEventStatus(id, Status.ENDED));
    }

    @Test
    @DisplayName("Should throw UnprocessableEntityException when trying to finish SportEvent with unfinished matches")
    void Should_ThrowUnprocessableEntityException_When_TryingToFinishSportEventWithUnfinishedMatches() {

        var matches = List.of(
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventA, Status.ENDED),
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventA, Status.IN_PROGRESS),
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventA, Status.ENDED),
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventA, Status.ENDED),
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventA, Status.ENDED),
                MatchTestUtil.createNewMatch(Team.PAPA_LEGUAS, Team.TWISTER, participants, sportEventA, Status.ENDED));

        sportEventA.setMatches(matches);
        sportEventA.setEventStatus(Status.IN_PROGRESS);
        entityManager.merge(sportEventA);

        var id = sportEventA.getId();
        assertThrows(UnprocessableEntityException.class, () -> sportEventService.updateEventStatus(id, Status.ENDED));
    }

}