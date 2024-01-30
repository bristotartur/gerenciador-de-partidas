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

        var sportType = "futsal";
        var result = Sports.findSportByValue(sportType);

        assertEquals(result.value, sportType);
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid value is passed")
    void Should_Throw_BadRequestException_When_InvalidValueIsPassed() {
        var sportType = "football";
        assertThrows(BadRequestException.class, () -> Sports.findSportByValue(sportType));
    }

}