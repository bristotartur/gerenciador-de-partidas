package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.actions.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.PenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.enums.PenaltyCardColor;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.services.events.GeneralMatchSportService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class PenaltyCardMapperTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PenaltyCardMapper penaltyCardMapper;
    @Autowired
    private GeneralMatchSportService generalMatchSportService;

    private PenaltyCard createNewPenaltyCard(Sports sport, PenaltyCardColor color) {

        return PenaltyCard.builder()
                .id(getRandomLongId())
                .color(color.name)
                .penaltyCardTime(LocalTime.of(9, 27, 0))
                .player(createNewPlayer())
                .match(createNewMatch())
                .build();
    }

    private PenaltyCardDto createNewPenaltyCardDto(Sports sport, PenaltyCardColor color) {

        return PenaltyCardDto.builder()
                .color(color)
                .penaltyCardTime(LocalTime.of(9, 27, 0))
                .playerId(getRandomLongId())
                .matchId(getRandomLongId())
                .sport(sport)
                .build();
    }

    private Match createNewMatch() {
        return Match.builder().id(getRandomLongId()).build();
    }

    private Participant createNewPlayer() {

        return Participant.builder()
                .name("sa")
                .classNumber("2-53")
                .team(any())
                .build();
    }

    @Test
    @DisplayName("Should convert color field from PenaltyCardDto to its String value when mapped to PenaltyCard")
    void Should_ConvertCardColorFiledToString_When_MappedToPenaltyCardIs() {

        var penaltyCardDto = createNewPenaltyCardDto(Sports.HANDBALL, PenaltyCardColor.YELLOW);
        var penaltyCard = penaltyCardMapper.toNewPenaltyCard(penaltyCardDto, any(), any());

        assertInstanceOf(String.class, penaltyCard.getColor());
    }

    @Test
    @DisplayName("Should map entities to their referent fields in PenaltyCard when they are passed")
    void Should_MapEntitiesToTheirReferentFieldsInPenaltyCard_When_TheyArePassed() {

        var sport = Sports.FUTSAL;
        var matchSport = createNewMatch();
        var player = createNewPlayer();
        var penaltyCardDto = createNewPenaltyCardDto(sport, PenaltyCardColor.RED);

        var penaltyCard = penaltyCardMapper.toNewPenaltyCard(penaltyCardDto, player, matchSport);

        assertEquals(penaltyCard.getMatch(), matchSport);
        assertEquals(penaltyCard.getPlayer(), player);
    }

    @Test
    @DisplayName("Should update PenaltyCard when new values are passed")
    void Should_UpdatePenaltyCards_When_NewValuesArePassed() {

        var sport = Sports.HANDBALL;
        var matchSport = createNewMatch();
        var player = createNewPlayer();
        var penaltyCardDto = createNewPenaltyCardDto(sport, PenaltyCardColor.RED);

        var existingPenaltyCard = createNewPenaltyCard(Sports.FUTSAL, PenaltyCardColor.YELLOW);
        var existingId = existingPenaltyCard.getId();

        var updatedPenaltyCard = penaltyCardMapper.toExistingPenaltyCard(existingId, penaltyCardDto, player, matchSport);

        assertNotEquals(existingPenaltyCard, updatedPenaltyCard);
    }

}
