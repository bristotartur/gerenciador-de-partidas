package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.PenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.enums.PenaltyCardColor;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.services.GeneralMatchSportService;
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
                .team(createNewTeam())
                .matchSport(generalMatchSportService.newMatchSport(sport))
                .build();
    }

    private PenaltyCardDto createNewPenaltyCardDto(Sports sport, PenaltyCardColor color) {

        return PenaltyCardDto.builder()
                .color(color)
                .penaltyCardTime(LocalTime.of(9, 27, 0))
                .teamId(getRandomLongId())
                .matchSportId(getRandomLongId())
                .sport(sport)
                .build();
    }

    private Team createNewTeam() {

        return Team.builder()
                .id(getRandomLongId())
                .points(300)
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
        var matchSport = generalMatchSportService.newMatchSport(sport);
        var team = createNewTeam();
        var penaltyCardDto = createNewPenaltyCardDto(sport, PenaltyCardColor.RED);

        var penaltyCard = penaltyCardMapper.toNewPenaltyCard(penaltyCardDto, matchSport, team);

        assertEquals(penaltyCard.getMatchSport(), matchSport);
        assertEquals(penaltyCard.getTeam(), team);
    }

    @Test
    @DisplayName("Should update PenaltyCard when new values are passed")
    void Should_UpdatePenaltyCards_When_NewValuesArePassed() {

        var sport = Sports.HANDBALL;
        var matchSport = generalMatchSportService.newMatchSport(sport);
        var team = createNewTeam();
        var penaltyCardDto = createNewPenaltyCardDto(sport, PenaltyCardColor.RED);

        var existingPenaltyCard = createNewPenaltyCard(Sports.FUTSAL, PenaltyCardColor.YELLOW);
        var existingId = existingPenaltyCard.getId();

        var updatedPenaltyCard = penaltyCardMapper.toExistingPenaltyCard(existingId, penaltyCardDto, matchSport, team);

        assertNotEquals(existingPenaltyCard, updatedPenaltyCard);
    }

}
