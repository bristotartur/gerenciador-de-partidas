package com.bristotartur.gerenciadordepartidas.enums;

import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class SportsTest {

    @Test
    @DisplayName("Should find sport when valid value is passed")
    void Should_FindSport_When_ValidValueIsPassed() {

        var chess = "CHESS";
        var basketball = "basketball";
        var tableTennis = "TABLE-TENNIS";

        assertEquals(Sports.CHESS, Sports.findSportLike(chess));
        assertEquals(Sports.BASKETBALL, Sports.findSportLike(basketball));
        assertEquals(Sports.TABLE_TENNIS, Sports.findSportLike(tableTennis));
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid value is passed")
    void Should_Throw_BadRequestException_When_InvalidValueIsPassed() {

        var football = "FOOTBALL";
        var tableTennis = "table tennis";

        assertThrows(BadRequestException.class, () -> Sports.findSportLike(football));
        assertThrows(BadRequestException.class, () -> Sports.findSportLike(tableTennis));
    }

}