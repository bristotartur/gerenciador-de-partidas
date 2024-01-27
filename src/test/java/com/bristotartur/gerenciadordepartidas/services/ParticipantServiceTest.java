package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.ParticipantRepository;
import com.bristotartur.gerenciadordepartidas.services.people.ParticipantService;
import com.bristotartur.gerenciadordepartidas.utils.ParticipantTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.TeamTestUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParticipantServiceTest {

    @Autowired
    private ParticipantService participantService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ParticipantRepository participantRepository;
    private Team team;

    @BeforeEach
    void setUp() {
        team = TeamTestUtil.createNewTeam(TeamName.PAPA_LEGUAS, entityManager);
    }

    @Test
    @DisplayName("Should retrieve all Participants from repository when searching for all Participants")
    void Should_RetrieveAllParticipantsFromRepository_When_SearchingForAllParticipants() {

        var participants = List.of(
                ParticipantTestUtil.createNewParticipant("1-53", team, entityManager),
                ParticipantTestUtil.createNewParticipant("2-53", team, entityManager),
                ParticipantTestUtil.createNewParticipant("3-53", team, entityManager));

        var result = participantService.findAllParticipants();

        assertEquals(result, participants);
    }

    @Test
    @DisplayName("Should retrieve all Participants from repository when their names are similar to the given name")
    void Should_RetrieveAllParticipantsFromRepository_When_TheirNamesAreSimilarToTheGivenName() {

        var participants = List.of(
                ParticipantTestUtil.createNewParticipant("Carlos Henrique","1-53", team, entityManager),
                ParticipantTestUtil.createNewParticipant("Carlos Eduardo","2-53", team, entityManager),
                ParticipantTestUtil.createNewParticipant("Carol","3-53", team, entityManager));

        var result = participantService.findParticipantsByNameLike("Carlos");

        assertFalse(participants.isEmpty());
        assertNotEquals(result, participants);
    }

    @Test
    @DisplayName("Should find Participant when existing Participant ID is passed to search")
    void Should_FindParticipant_When_ExistingParticipantIdIsPassedToSearch() {

        var participant = ParticipantTestUtil.createNewParticipant("3-53", team, entityManager);

        var existingId = participant.getId();
        var result = participantService.findParticipantById(existingId);

        assertEquals(result, participant);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing Participant ID is passed to any method")
    void Should_ThrowNotFoundException_When_NonExistingParticipantIdIsPassedToAnyMethod() {

        var id = getRandomLongId();
        var participantDto = ParticipantTestUtil.createNewParticipantDto("2-14", getRandomLongId());

        assertThrows(NotFoundException.class, () -> {
            participantService.findParticipantById(id);
        });
        assertThrows(NotFoundException.class, () -> {
           participantService.deleteParticipantById(id);
        });
        assertThrows(NotFoundException.class, () -> {
            participantService.replaceParticipant(id, participantDto);
        });
    }

    @Test
    @DisplayName("Should create new ExposingParticipantDto when Participant is passed to create ExposingParticipantDto")
    void Should_CreateNewExposingParticipantDto_When_ParticipantIsPassedToCreateExposingParticipantDto() {

        var participant = ParticipantTestUtil.createNewParticipant("3-53", team, entityManager);
        var result = participantService.createExposingParticipantDto(participant);

        assertEquals(result.getTeam(), participant.getTeam().getName());
    }

    @Test
    @DisplayName("Should save Participant when valid ParticipantDto is passed to save")
    void Should_SaveParticipant_When_ValidParticipantDtoIsPassedToSave() {

        var participantDto = ParticipantTestUtil.createNewParticipantDto("2-53", team.getId());
        var result = participantService.saveParticipant(participantDto);

        assertEquals(result, participantRepository.findById(result.getId()).get());
    }

    @Test
    @DisplayName("Should delete Participant from database when Participant ID is passed to delete")
    void Should_DeleteParticipantFromDatabase_When_ParticipantIdIsPassedToDelete() {

        var participant = ParticipantTestUtil.createNewParticipant("3-54", team, entityManager);
        participantService.deleteParticipantById(participant.getId());

        assertTrue(participantRepository.findById(participant.getId()).isEmpty());
    }

    @Test
    @DisplayName("Should update Participant when ParticipantDto with new values is passed")
    void Should_UpdateParticipant_When_ParticipantDtoWithNewValuesIsPassed() {

        var participant = ParticipantTestUtil.createNewParticipant("3-54", team, entityManager);

        var newTeam = TeamTestUtil.createNewTeam(TeamName.UNICONTTI, entityManager);
        var participantDto = ParticipantTestUtil.createNewParticipantDto("2-31", newTeam.getId());

        var result = participantService.replaceParticipant(participant.getId(), participantDto);

        assertNotEquals(result, participant);
    }

    @Test
    @DisplayName("Should reformat class number when valid class number is passed")
    void Should_ReformatClassNumber_When_ValidClassNumberIsPassed() {

        var expectClassNumber = "2-61";
        var participantDto = ParticipantTestUtil.createNewParticipantDto("261", team.getId());

        var result = participantService.saveParticipant(participantDto);

        assertEquals(result.getClassNumber(), expectClassNumber);
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid class number is passed")
    void Should_ThrowBadRequestException_When_InvalidClassNumberIsPassed() {

        var participantDto = ParticipantTestUtil.createNewParticipantDto("4-61", team.getId());

        assertThrows(BadRequestException.class, () -> {
            participantService.saveParticipant(participantDto);
        });
    }

}