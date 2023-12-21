package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.PenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.enums.PenaltyCardColor;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PenaltyCardMapperTest {

    PenaltyCardDto penaltyCardDto;
    PenaltyCard existingPenaltyCard;
    Team teamA;
    Team teamB;

    @BeforeEach
    void setUp() {

        teamA = Team.builder()
                .id(1L)
                .name(TeamName.PAPA_LEGUAS.name).build();

        teamB = Team.builder()
                .id(2L)
                .name(TeamName.UNICONTTI.name).build();

        existingPenaltyCard = PenaltyCard.builder()
                .id(1L)
                .color(PenaltyCardColor.YELLOW.name())
                .team(teamA).build();

        penaltyCardDto = new PenaltyCardDto(PenaltyCardColor.RED, teamB);
    }

    @Test
    @DisplayName("Should convert color field from PenaltyCardDto to its String value when mapped to PenaltyCard")
    void Should_ConvertCardColorFiledToString_When_MappedToPenaltyCardIs() {

        var penaltyCard = PenaltyCardMapper.INSTANCE.toNewPenaltyCard(penaltyCardDto);

        assertThat(penaltyCard.getColor()).isInstanceOf(String.class);
    }

    @Test
    @DisplayName("Should update PenaltyCard field when new values are passed")
    void Should_UpdatePenaltyCardFields_When_NewValuesArePassed() {

        var team = PenaltyCardMapper.INSTANCE.toExistingPenaltyCard(1L, penaltyCardDto);

        assertThat(team.getColor()).isNotEqualTo(existingPenaltyCard.getColor());
        assertThat(team.getTeam()).isNotEqualTo(existingPenaltyCard.getTeam());
    }
}