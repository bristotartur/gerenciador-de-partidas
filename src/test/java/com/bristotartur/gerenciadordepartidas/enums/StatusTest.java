package com.bristotartur.gerenciadordepartidas.enums;

import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class StatusTest {

    @Test
    @DisplayName("Should not throw anything when the same arguments are passed")
    void Should_NotThrowAnything_When_SameArgumentsArePassed() {

        assertDoesNotThrow(() -> Status.checkStatus(Status.SCHEDULED, Status.SCHEDULED));
        assertDoesNotThrow(() -> Status.checkStatus(Status.IN_PROGRESS, Status.IN_PROGRESS));
        assertDoesNotThrow(() -> Status.checkStatus(Status.ENDED, Status.ENDED));
        assertDoesNotThrow(() -> Status.checkStatus(Status.OPEN_FOR_EDITS, Status.OPEN_FOR_EDITS));
    }

    @Test
    @DisplayName("Should not throw anything when valid arguments are passed")
    void Should_NotThrowAnything_When_ValidArgumentsArePassed() {

        assertDoesNotThrow(() -> Status.checkStatus(Status.SCHEDULED, Status.IN_PROGRESS));
        assertDoesNotThrow(() -> Status.checkStatus(Status.IN_PROGRESS, Status.ENDED));
        assertDoesNotThrow(() -> Status.checkStatus(Status.ENDED, Status.OPEN_FOR_EDITS));
        assertDoesNotThrow(() -> Status.checkStatus(Status.OPEN_FOR_EDITS, Status.ENDED));
        assertDoesNotThrow(() -> Status.checkStatus(Status.ENDED, Status.ENDED));
        assertDoesNotThrow(() -> Status.checkStatus(Status.OPEN_FOR_EDITS, Status.OPEN_FOR_EDITS));
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid arguments are passed")
    void Should_TheowBadRequestException_When_InvalidArgumentsArePassed() {

        assertThrows(BadRequestException.class, () -> Status.checkStatus(Status.SCHEDULED, Status.ENDED));
        assertThrows(BadRequestException.class, () -> Status.checkStatus(Status.SCHEDULED, Status.OPEN_FOR_EDITS));

        assertThrows(BadRequestException.class, () -> Status.checkStatus(Status.IN_PROGRESS, Status.SCHEDULED));
        assertThrows(BadRequestException.class, () -> Status.checkStatus(Status.IN_PROGRESS, Status.OPEN_FOR_EDITS));

        assertThrows(BadRequestException.class, () -> Status.checkStatus(Status.ENDED, Status.SCHEDULED));
        assertThrows(BadRequestException.class, () -> Status.checkStatus(Status.ENDED, Status.IN_PROGRESS));

        assertThrows(BadRequestException.class, () -> Status.checkStatus(Status.OPEN_FOR_EDITS, Status.SCHEDULED));
        assertThrows(BadRequestException.class, () -> Status.checkStatus(Status.OPEN_FOR_EDITS, Status.IN_PROGRESS));
    }

}