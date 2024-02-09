package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.ParticipantRepository;
import com.bristotartur.gerenciadordepartidas.services.people.ParticipantService;
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

import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ParticipantServiceTest {

    @Autowired
    private ParticipantService participantService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ParticipantRepository participantRepository;

    private Edition edition;

    @BeforeEach
    void setUp() {
        edition = EditionTestUtil.createNewEdition(Status.IN_PROGRESS, entityManager);
    }

    @Test
    @DisplayName("Should retrieve all Participants in paged form when searching for all Participants")
    void Should_RetrieveAllParticipantsInPagedForm_When_SearchingForAllParticipants() {

        var pageable = PageRequest.of(0, 3);
        var team = Team.PAPA_LEGUAS;

        var participants = List.of(
                ParticipantTestUtil.createNewParticipant("1-53", team, edition, entityManager),
                ParticipantTestUtil.createNewParticipant("2-53", team, edition, entityManager),
                ParticipantTestUtil.createNewParticipant("3-53", team, edition, entityManager));

        var participantPage = new PageImpl<>(participants, pageable, participants.size());
        var result = participantService.findAllParticipants(pageable);

        assertEquals(result.getContent(), participantPage.getContent());
        assertEquals(result.getTotalPages(), participantPage.getTotalPages());
    }

    @Test
    @DisplayName("Should retrieve Participants in paged form when their names are similar to the given name")
    void Should_RetrieveParticipantsInPagedForm_When_TheirNamesAreSimilarToTheGivenName() {

        var pageable = PageRequest.of(0, 3);
        var team = Team.PAPA_LEGUAS;

        var participants = List.of(
                ParticipantTestUtil.createNewParticipant("Carlos Henrique","1-53", team, edition, entityManager),
                ParticipantTestUtil.createNewParticipant("Carlos Eduardo","2-53", team, edition, entityManager),
                ParticipantTestUtil.createNewParticipant("Carol","3-53", team, edition, entityManager));

        var participantPage = new PageImpl<>(participants, pageable, participants.size());
        var result = participantService.findParticipantsByNameLike("Carlos", pageable);

        assertFalse(result.getContent().isEmpty());
        assertNotEquals(result.getContent(), participantPage.getContent());
    }

    @Test
    @DisplayName("Should find Participant when existing Participant ID is passed to search")
    void Should_FindParticipant_When_ExistingParticipantIdIsPassedToSearch() {

        var team = Team.PAPA_LEGUAS;
        var participant = ParticipantTestUtil.createNewParticipant("3-53", team, edition, entityManager);

        var existingId = participant.getId();
        var result = participantService.findParticipantById(existingId);

        assertEquals(result, participant);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing Participant ID is passed to any method")
    void Should_ThrowNotFoundException_When_NonExistingParticipantIdIsPassedToAnyMethod() {

        var id = getRandomLongId();
        var participantDto = ParticipantTestUtil.createNewParticipantDto("2-14", any(), any());

        assertThrows(NotFoundException.class, () -> participantService.findParticipantById(id));
        assertThrows(NotFoundException.class, () -> participantService.deleteParticipantById(id));
        assertThrows(NotFoundException.class, () -> participantService.replaceParticipant(id, participantDto));
    }

    @Test
    @DisplayName("Should create new ExposingParticipantDto when Participant is passed to create ExposingParticipantDto")
    void Should_CreateNewExposingParticipantDto_When_ParticipantIsPassedToCreateExposingParticipantDto() {

        var team = Team.PAPA_LEGUAS;
        var participant = ParticipantTestUtil.createNewParticipant("3-53", team, edition, entityManager);
        var result = participantService.createExposingParticipantDto(participant);

        assertEquals(result.getTeam(), participant.getTeam());
    }

    @Test
    @DisplayName("Should save Participant when valid ParticipantDto is passed to save")
    void Should_SaveParticipant_When_ValidParticipantDtoIsPassedToSave() {

        var team = Team.PAPA_LEGUAS;
        var participantDto = ParticipantTestUtil.createNewParticipantDto("2-53", team, edition.getId());
        var result = participantService.saveParticipant(participantDto);

        assertEquals(result, participantRepository.findById(result.getId()).get());
    }

    @Test
    @DisplayName("Should delete Participant from database when Participant ID is passed to delete")
    void Should_DeleteParticipantFromDatabase_When_ParticipantIdIsPassedToDelete() {

        var team = Team.PAPA_LEGUAS;
        var participant = ParticipantTestUtil.createNewParticipant("3-54", team, edition, entityManager);
        participantService.deleteParticipantById(participant.getId());

        assertTrue(participantRepository.findById(participant.getId()).isEmpty());
    }

    @Test
    @DisplayName("Should throw BadRequestException when trying to remove a Participant already associated to any event")
    void Should_ThrowBadRequestException_When_TrtingToRemoveParticipantAlreadyAssociatedToAnyEvent() {

        var team = Team.PAPA_LEGUAS;
        var teamB = Team.ATOMICA;
        var sportEvent = SportEventTestUtil.createNewSportEvent(
                Sports.FUTSAL, Modality.MASCULINE, Status.SCHEDULED, edition, entityManager
        );
        var participant = ParticipantTestUtil.createNewParticipant("3-53", team, edition, entityManager);
        var match = MatchTestUtil.createNewMatch(team, teamB, List.of(participant), sportEvent);

        entityManager.merge(match);
        assertThrows(BadRequestException.class, () -> participantService.deleteParticipantById(participant.getId()));
    }

    @Test
    @DisplayName("Should update Participant when ParticipantDto with new values is passed")
    void Should_UpdateParticipant_When_ParticipantDtoWithNewValuesIsPassed() {

        var team = Team.PAPA_LEGUAS;
        var participant = ParticipantTestUtil.createNewParticipant("3-54", team, edition, entityManager);

        var newTeam = Team.UNICONTTI;
        var participantDto = ParticipantTestUtil.createNewParticipantDto("2-31", newTeam, edition.getId());

        var result = participantService.replaceParticipant(participant.getId(), participantDto);

        assertNotEquals(result, participant);
    }

    @Test
    @DisplayName("Should reformat class number when valid class number is passed")
    void Should_ReformatClassNumber_When_ValidClassNumberIsPassed() {

        var team = Team.PAPA_LEGUAS;
        var expectClassNumber = "2-61";
        var participantDto = ParticipantTestUtil.createNewParticipantDto("261", team, edition.getId());

        var result = participantService.saveParticipant(participantDto);

        assertEquals(result.getClassNumber(), expectClassNumber);
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid class number is passed")
    void Should_ThrowBadRequestException_When_InvalidClassNumberIsPassed() {

        var team = Team.PAPA_LEGUAS;
        var participantDto = ParticipantTestUtil.createNewParticipantDto("4-61", team, edition.getId());

        assertThrows(BadRequestException.class, () -> participantService.saveParticipant(participantDto));
    }

}