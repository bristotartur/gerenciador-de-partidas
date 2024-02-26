package com.bristotartur.gerenciadordepartidas.enums;

import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TeamTest {

    @Test
    @DisplayName("Should find Team when valid value is passed")
    void Should_FindStatus_When_ValidValueIsPassed() {

        var atomica = "ATOMICA";
        var papaLeguas = "papa-leguas";
        var mestresDeObras = "MeSTRes-DE_OBraS";
        var mestres = "mestres";
        var papa = "PAPA";

        assertEquals(Team.ATOMICA, Team.findTeamLike(atomica));
        assertEquals(Team.PAPA_LEGUAS, Team.findTeamLike(papaLeguas));
        assertEquals(Team.MESTRES_DE_OBRAS, Team.findTeamLike(mestresDeObras));
        assertEquals(Team.MESTRES_DE_OBRAS, Team.findTeamLike(mestres));
        assertEquals(Team.PAPA_LEGUAS, Team.findTeamLike(papa));
    }

    @Test
    @DisplayName("Should throw BadRequestException when invalid Team is passed")
    void Should_ThrowbadRequestException_When_InvalidStatusIsPassed() {

        var papaLeguas = "PAPA LEGUAS";
        var twitter = "twitter";
        var uniconti = "uniconti";

        assertThrows(BadRequestException.class, () -> Team.findTeamLike(papaLeguas));
        assertThrows(BadRequestException.class, () -> Team.findTeamLike(twitter));
        assertThrows(BadRequestException.class, () -> Team.findTeamLike(uniconti));
    }

}