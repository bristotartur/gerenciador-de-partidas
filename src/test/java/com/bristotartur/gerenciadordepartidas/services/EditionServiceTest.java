package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.dtos.input.EditionDto;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.EditionRepository;
import com.bristotartur.gerenciadordepartidas.services.events.EditionService;
import com.bristotartur.gerenciadordepartidas.utils.EditionTestUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class EditionServiceTest {

    @Autowired
    private EditionService editionService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EditionRepository editionRepository;

    @Test
    @DisplayName("Should retrieve Editions in paged form when searching for all Editions")
    void Should_RetrieveEditionsInPagedForm_When_SearchingForAllEditions() {

        var pageable = PageRequest.of(0, 3);

        var editions = List.of(
                EditionTestUtil.createNewEdition(Status.SCHEDULED, entityManager),
                EditionTestUtil.createNewEdition(Status.ENDED, entityManager),
                EditionTestUtil.createNewEdition(Status.IN_PROGRESS, entityManager));

        var editionPage = new PageImpl<>(editions, pageable, editions.size());
        var result = editionService.findAllEditions(pageable);

        assertEquals(result.getContent(), editionPage.getContent());
        assertEquals(result.getTotalPages(), editionPage.getTotalPages());
    }

    @Test
    @DisplayName("Should find Edition when valid edition ID is passed")
    void Should_FindEdition_When_ValidEditionIdIsPassed() {

        var edition = EditionTestUtil.createNewEdition(Status.SCHEDULED, entityManager);
        var result = editionService.findEditionById(edition.getId());

        assertEquals(result, edition);
    }

    @Test
    @DisplayName("Should throw NotFoundException when invalid Edition ID is passed to any method")
    void Should_ThrowNotFoundException_When_InvalidEditionIdisPassedToAnyMethod() {

        var id = getRandomLongId();
        var dto = new EditionDto(any(), any());

        assertThrows(NotFoundException.class, () -> editionService.findEditionById(id));
        assertThrows(NotFoundException.class, () -> editionService.deleteEditionById(id));
        assertThrows(NotFoundException.class, () -> editionService.replaceEdition(id, dto));
        assertThrows(NotFoundException.class, () -> editionService.updateEditionStatus(id, any()));
    }

    @Test
    @DisplayName("Should save Edition when valid EditionDto is passed to save")
    void Should_SaveEdition_When_ValidEditionDtoIsPassedToSave() {

        var dto = new EditionDto(LocalDate.now(), LocalDate.now());
        var result = editionService.saveEdition(dto);

        assertTrue(editionRepository.findById(result.getId()).isPresent());
    }

    @Test
    @DisplayName("Should delete Edition from database when ID from Edition with accept status is passed to delete")
    void Should_DeleteeditionFromDatabase_When_IdFromEditionWithAcceptStatusIsPassedToDelete() {

        var edition = EditionTestUtil.createNewEdition(Status.SCHEDULED, entityManager);
        editionService.deleteEditionById(edition.getId());

        assertTrue(editionRepository.findById(edition.getId()).isEmpty());
    }

    @Test
    @DisplayName("Should throw BadRequestException when ID from Edition with non accept status is passed to delete")
    void Should_ThrowBadRequestException_When_IdFromEditionWithNonAcceptStatusIsPassedToDelete() {

        var editionA = EditionTestUtil.createNewEdition(Status.IN_PROGRESS, entityManager);
        var editionB = EditionTestUtil.createNewEdition(Status.ENDED, entityManager);
        var editionC = EditionTestUtil.createNewEdition(Status.OPEN_FOR_EDITS, entityManager);

        assertThrows(BadRequestException.class, () -> editionService.deleteEditionById(editionA.getId()));
        assertThrows(BadRequestException.class, () -> editionService.deleteEditionById(editionB.getId()));
        assertThrows(BadRequestException.class, () -> editionService.deleteEditionById(editionC.getId()));
    }

    @Test
    @DisplayName("Should update Edition when EditionDto with new values is passed")
    void Should_UpdateEdition_When_EditionDtoWithNewValuesIsPassed() {

        var edition = EditionTestUtil.createNewEdition(Status.SCHEDULED, entityManager);
        var dto = new EditionDto(LocalDate.of(2024, Month.FEBRUARY, 16), LocalDate.of(2024, Month.FEBRUARY, 17));

        var result = editionService.replaceEdition(edition.getId(), dto);

        assertNotEquals(result, edition);
    }

    @Test
    @DisplayName("Should not throw anything when valid Status is passed to update Edition Status")
    void Should_NotThrowAnything_When_ValidStatusIsPassedToUpdateEditionStatus() {

        var edition = EditionTestUtil.createNewEdition(Status.SCHEDULED, entityManager);

        assertDoesNotThrow(() -> editionService.updateEditionStatus(edition.getId(), Status.IN_PROGRESS));
        assertDoesNotThrow(() -> editionService.updateEditionStatus(edition.getId(), Status.ENDED));
        assertDoesNotThrow(() -> editionService.updateEditionStatus(edition.getId(), Status.OPEN_FOR_EDITS));
        assertDoesNotThrow(() -> editionService.updateEditionStatus(edition.getId(), Status.ENDED));
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid Status is passed to update Edition Status")
    void Should_ThrowBadRequestException_When_InvalidStatusIsPassedToUpdateEditionStatus() {

        var edition = EditionTestUtil.createNewEdition(Status.SCHEDULED, entityManager);

        assertThrows(BadRequestException.class, () -> editionService.updateEditionStatus(edition.getId(), Status.ENDED));
        assertThrows(BadRequestException.class, () -> editionService.updateEditionStatus(edition.getId(), Status.OPEN_FOR_EDITS));
    }

    @Test
    @DisplayName("Should throw BadRequestException when trying to leave two Editions in progress")
    void Should_ThrowBadRequestException_When_TryingToLeaveTwoEditionsInProgress() {

        EditionTestUtil.createNewEdition(Status.IN_PROGRESS, entityManager);
        var edition = EditionTestUtil.createNewEdition(Status.SCHEDULED, entityManager);

        assertThrows(BadRequestException.class, () -> editionService.updateEditionStatus(edition.getId(), Status.IN_PROGRESS));
    }

    @Test
    @DisplayName("Should not throw anything when trying to operate on unfinished Editions")
    void Should_NotThrowAnything_When_TryingToOperateOnUnfinishedEditions() {

        var editionA = EditionTestUtil.createNewEdition(Status.SCHEDULED, entityManager);
        var editionB = EditionTestUtil.createNewEdition(Status.IN_PROGRESS, entityManager);
        var editionC = EditionTestUtil.createNewEdition(Status.OPEN_FOR_EDITS, entityManager);

        assertDoesNotThrow(() -> editionService.checkEditionStatusById(editionA.getId()));
        assertDoesNotThrow(() -> editionService.checkEditionStatusById(editionB.getId()));
        assertDoesNotThrow(() -> editionService.checkEditionStatusById(editionC.getId()));
    }

    @Test
    @DisplayName("Should throw BadRequestException when trying to operate on unfinished Editions")
    void Should_ThrowBadRequestException_When_TryingToOperateOnFinishedEditions() {
        var edition = EditionTestUtil.createNewEdition(Status.ENDED, entityManager);
        assertThrows(BadRequestException.class, () -> editionService.checkEditionStatusById(edition.getId()));
    }

}