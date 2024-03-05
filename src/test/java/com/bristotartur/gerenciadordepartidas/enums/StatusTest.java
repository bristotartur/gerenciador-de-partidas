package com.bristotartur.gerenciadordepartidas.enums;

import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.UnprocessableEntityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class StatusTest {

    @Test
    @DisplayName("Should find status when valid value is passed")
    void Should_FindStatus_When_ValidValueIsPassed() {

        var scheduled = "SCHEDULED";
        var inProgress = "in-progress";
        var openForEdits = "oPEN-FoR-EdItS";

        assertEquals(Status.SCHEDULED, Status.findStatusLike(scheduled));
        assertEquals(Status.IN_PROGRESS, Status.findStatusLike(inProgress));
        assertEquals(Status.OPEN_FOR_EDITS, Status.findStatusLike(openForEdits));
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid status is passed")
    void Should_ThrowbadRequestException_When_InvalidStatusIsPassed() {

        var inProgress = "IN PROGRESS";
        var finished = "FINISHED";

        assertThrows(BadRequestException.class, () -> Status.findStatusLike(inProgress));
        assertThrows(BadRequestException.class, () -> Status.findStatusLike(finished));
    }

    @Test
    @DisplayName("Should not throw anything when the same Status are passed")
    void Should_NotThrowAnything_When_SameStatusArePassed() {

        assertDoesNotThrow(() -> Status.checkStatus(Status.SCHEDULED, Status.SCHEDULED));
        assertDoesNotThrow(() -> Status.checkStatus(Status.IN_PROGRESS, Status.IN_PROGRESS));
        assertDoesNotThrow(() -> Status.checkStatus(Status.ENDED, Status.ENDED));
        assertDoesNotThrow(() -> Status.checkStatus(Status.OPEN_FOR_EDITS, Status.OPEN_FOR_EDITS));
    }

    @Test
    @DisplayName("Should not throw anything when valid Status are passed")
    void Should_NotThrowAnything_When_ValidStatusArePassed() {

        assertDoesNotThrow(() -> Status.checkStatus(Status.SCHEDULED, Status.IN_PROGRESS));
        assertDoesNotThrow(() -> Status.checkStatus(Status.IN_PROGRESS, Status.ENDED));
        assertDoesNotThrow(() -> Status.checkStatus(Status.ENDED, Status.OPEN_FOR_EDITS));
        assertDoesNotThrow(() -> Status.checkStatus(Status.OPEN_FOR_EDITS, Status.ENDED));
        assertDoesNotThrow(() -> Status.checkStatus(Status.ENDED, Status.ENDED));
        assertDoesNotThrow(() -> Status.checkStatus(Status.OPEN_FOR_EDITS, Status.OPEN_FOR_EDITS));
    }

    @Test
    @DisplayName("Should throw UnprocessableEntityException when invalid Status are passed")
    void Should_TheowUnprocessableEntityException_When_InvalidStatusArePassed() {

        assertThrows(UnprocessableEntityException.class, () -> Status.checkStatus(Status.SCHEDULED, Status.ENDED));
        assertThrows(UnprocessableEntityException.class, () -> Status.checkStatus(Status.SCHEDULED, Status.OPEN_FOR_EDITS));

        assertThrows(UnprocessableEntityException.class, () -> Status.checkStatus(Status.IN_PROGRESS, Status.SCHEDULED));
        assertThrows(UnprocessableEntityException.class, () -> Status.checkStatus(Status.IN_PROGRESS, Status.OPEN_FOR_EDITS));

        assertThrows(UnprocessableEntityException.class, () -> Status.checkStatus(Status.ENDED, Status.SCHEDULED));
        assertThrows(UnprocessableEntityException.class, () -> Status.checkStatus(Status.ENDED, Status.IN_PROGRESS));

        assertThrows(UnprocessableEntityException.class, () -> Status.checkStatus(Status.OPEN_FOR_EDITS, Status.SCHEDULED));
        assertThrows(UnprocessableEntityException.class, () -> Status.checkStatus(Status.OPEN_FOR_EDITS, Status.IN_PROGRESS));
    }

}