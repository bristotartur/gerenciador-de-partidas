package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.enums.PenaltyCardColor;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.utils.MatchTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.ParticipantTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.PenaltyCardTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class PenaltyCardMapperTest {

    @Autowired
    private PenaltyCardMapper penaltyCardMapper;

    @Test
    @DisplayName("Should map entities to their referent fields in PenaltyCard when they are passed")
    void Should_MapEntitiesToTheirReferentFieldsInPenaltyCard_When_TheyArePassed() {

        var player = ParticipantTestUtil.createNewParticipant("2-53", any());
        var match = MatchTestUtil.createNewMatch(any(), any(), List.of(player));

        var penaltyCardDto = PenaltyCardTestUtil
                .createNewPenaltyCardDto(Sports.HANDBALL, PenaltyCardColor.RED, any(), any());

        var result = penaltyCardMapper.toNewPenaltyCard(penaltyCardDto, player, match);

        assertEquals(result.getColor(), PenaltyCardColor.RED);
        assertEquals(result.getPlayer(), player);
        assertEquals(result.getMatch(), match);
    }

    @Test
    @DisplayName("Should update PenaltyCard when new values are passed")
    void Should_UpdatePenaltyCards_When_NewValuesArePassed() {

        var playerA = ParticipantTestUtil.createNewParticipant("2-53", any());
        var playerB = ParticipantTestUtil.createNewParticipant("2-53", any());
        var match = MatchTestUtil.createNewMatch(any(), any(), List.of(playerA));

        var penaltyCard = PenaltyCardTestUtil
                .createNewPenaltyCard(PenaltyCardColor.YELLOW, playerA, match);

        var penaltyCardDto = PenaltyCardTestUtil
                .createNewPenaltyCardDto(Sports.FUTSAL, PenaltyCardColor.RED, any(), any());

        var result = penaltyCardMapper.toExistingPenaltyCard(getRandomLongId(), penaltyCardDto, playerB, match);

        assertNotEquals(result, penaltyCard);
    }

}
