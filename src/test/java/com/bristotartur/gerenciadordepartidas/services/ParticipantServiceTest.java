package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.ParticipantDto;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.ParticipantRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.EntityTestUtil.createNewTeam;
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

    private Participant createNewParticipant(String classNumber) {

        return Participant.builder()
                .name("sa")
                .classNumber(classNumber)
                .team(createNewTeam(entityManager))
                .build();
    }

    private ParticipantDto createNewParticipantDto(String classNumber) {

        var team = createNewTeam(entityManager);

        return ParticipantDto.builder()
                .name("foo")
                .classNumber(classNumber)
                .teamId(team.getId())
                .build();
    }

    @Test
    @DisplayName("Should retrieve all Participants from repository when searching for all Participants")
    void Should_RetrieveAllParticipantsFromRepository_When_SearchingForAllParticipants() {

        List<Participant> existingParticipants = List.of(
                createNewParticipant("1-53"),
                createNewParticipant("2-53"),
                createNewParticipant("3-53"));

        existingParticipants.forEach(participant -> entityManager.merge(participant));

        List<Participant> participantList = participantService.findAllParticipants();

        assertEquals(existingParticipants, participantList);
    }

    @Test
    @DisplayName("Should find Participant when existing Participant ID is passed to search")
    void Should_FindParticipant_When_ExistingParticipantIdIsPassedToSearch() {

        var existingParticipant = createNewParticipant("3-53");

        entityManager.merge(existingParticipant);

        var existingId = existingParticipant.getId();
        var participant = participantService.findParticipantById(existingId);

        assertEquals(participant, existingParticipant);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing Participant ID is passed to search")
    void Should_ThrowNotFoundException_When_NonExistingParticipantIdIsPassedToSearch() {

        var id = getRandomLongId();

        assertThrows(NotFoundException.class, () -> {
            participantService.findParticipantById(id);
        });
    }

    @Test
    @DisplayName("Should save Participant when valid ParticipantDto is passed to save")
    void Should_SaveParticipant_When_ValidParticipantDtoIsPassedToSave() {

        var participantDto = createNewParticipantDto("2-53");
        var savedId = participantService.saveParticipant(participantDto).getId();

        var savedParticipant = participantRepository.findById(savedId).get();

        assertNotNull(savedParticipant);
    }

    @Test
    @DisplayName("Should delete Participant from database when Participant ID is passed to delete")
    void Should_DeleteParticipantFromDatabase_When_ParticipantIdIsPassedToDelete() {

        var existingParticipant = createNewParticipant("3-54");

        entityManager.merge(existingParticipant);

        var existingId = existingParticipant.getId();
        participantService.deleteParticipantById(existingId);

        assertTrue(participantRepository.findById(existingId).isEmpty());
    }

    @Test
    @DisplayName("Should update Participant when ParticipantDto with new values is passed")
    void Should_UpdateParticipant_When_ParticipantDtoWithNewValuesIsPassed() {

        var existingParticipant = createNewParticipant("2-81");

        entityManager.merge(existingParticipant);

        var existingId = existingParticipant.getId();
        var participantDto = createNewParticipantDto("2-61");

        var updatedParticipant = participantService.replaceParticipant(existingId, participantDto);

        assertNotNull(updatedParticipant);
        assertNotEquals(existingParticipant, updatedParticipant);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing ID is passed to replace Participant")
    void Should_ThrowNotFoundException_When_NonExistingIdIsPassedToReplaceParticipant() {

        var id = getRandomLongId();
        var participantDto = createNewParticipantDto("2-14");

        assertThrows(NotFoundException.class, () -> {
            participantService.replaceParticipant(id, participantDto);
        });
    }

    @Test
    void Should_FormatClassNumber_When_ValidClassNumberIsPassed() {

        var expectClassNumber = "2-61";
        var participantDto = createNewParticipantDto("261");

        var participant = participantService.saveParticipant(participantDto);

        assertEquals(participant.getClassNumber(), expectClassNumber);
    }

    @Test
    void Should_ThrowBadRequestException_When_InvalidClassNumberIsPassed() {

        var participantDto = createNewParticipantDto("4-61");

        assertThrows(BadRequestException.class, () -> {
            participantService.saveParticipant(participantDto);
        });
    }

}