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
        var dto = new EditionDto(any(), any(), any());

        assertThrows(NotFoundException.class, () -> editionService.findEditionById(id));
        assertThrows(NotFoundException.class, () -> editionService.deleteEditionById(id));
        assertThrows(NotFoundException.class, () -> editionService.replaceEdition(id, dto));
    }

    @Test
    @DisplayName("Should save Edition when valid EditionDto is passed to save")
    void Should_SaveEdition_When_ValidEditionDtoIsPassedToSave() {

        var dto = new EditionDto(Status.SCHEDULED, LocalDate.now(), LocalDate.now());
        var result = editionService.saveEdition(dto);

        assertTrue(editionRepository.findById(result.getId()).isPresent());
    }

    @Test
    @DisplayName("Should throw BadRequestException when trying to create an Edition with accept status")
    void Should_ThrowBadRequestException_When_TriyngToCreateEditionWithNonAccesptStatus() {

        var dtoA = new EditionDto(Status.IN_PROGRESS, LocalDate.now(), LocalDate.now());
        var dtoB = new EditionDto(Status.ENDED, LocalDate.now(), LocalDate.now());
        var dtoC = new EditionDto(Status.OPEN_FOR_EDITS, LocalDate.now(), LocalDate.now());

        assertThrows(BadRequestException.class, () -> editionService.saveEdition(dtoA));
        assertThrows(BadRequestException.class, () -> editionService.saveEdition(dtoB));
        assertThrows(BadRequestException.class, () -> editionService.saveEdition(dtoC));
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
        var dto = new EditionDto(Status.IN_PROGRESS, LocalDate.now(), LocalDate.now());

        var result = editionService.replaceEdition(edition.getId(), dto);

        assertNotEquals(result, edition);
    }

    @Test
    @DisplayName("Should throw BadRequestException when trying to leave two Editions in progress")
    void Should_ThrowBadRequestException_When_TryingToLeaveTwoEditionsInProgress() {

        var editionA = EditionTestUtil.createNewEdition(Status.IN_PROGRESS, entityManager);
        var editionB = EditionTestUtil.createNewEdition(Status.SCHEDULED, entityManager);
        var dto = new EditionDto(Status.IN_PROGRESS, LocalDate.now(), LocalDate.now());

        assertThrows(BadRequestException.class, () -> editionService.replaceEdition(editionB.getId(), dto));
    }

}