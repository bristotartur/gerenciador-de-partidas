package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.FootballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.PenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.enums.PenaltyCardColor;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.services.GeneralMatchSportService;
import com.bristotartur.gerenciadordepartidas.services.TeamService;
import com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class PenaltyCardMapperTest {

    @Autowired
    private EntityManager entityManager;

    @Mock
    private GeneralMatchSportService generalMatchSportService;
    @Mock
    private TeamService teamService;
    @InjectMocks
    private PenaltyCardMapper penaltyCardMapper;

    private PenaltyCardDto penaltyCardDto;
    private PenaltyCard existingPenaltyCard;
    private Team existingTeam;
    private MatchSport existingMatchSport;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        existingTeam = createTeam();
        existingMatchSport = createFootballMatch();
        existingPenaltyCard = createPenaltyCard(PenaltyCardColor.YELLOW, existingTeam, existingMatchSport);
        penaltyCardDto = createPenaltyCardDto(PenaltyCardColor.RED, Sports.FOOTBALL);
    }

    private Team createTeam() {
        return Team.builder().id(RandomIdUtil.getRandomLongId()).build();
    }

    private FootballMatch createFootballMatch() {
        return FootballMatch.builder().id(RandomIdUtil.getRandomLongId()).build();
    }

    private PenaltyCard createPenaltyCard(PenaltyCardColor color, Team team, MatchSport matchSport) {

        return PenaltyCard.builder()
                .id(1L)
                .color(color.name)
                .matchSport(existingMatchSport)
                .team(existingTeam).build();
    }

    private PenaltyCardDto createPenaltyCardDto(PenaltyCardColor color, Sports sport) {

        return PenaltyCardDto.builder()
                .color(color)
                .penaltyCardTime(any())
                .teamId(RandomIdUtil.getRandomLongId())
                .matchSportId(RandomIdUtil.getRandomLongId())
                .sport(sport).build();
    }

    @Test
    @DisplayName("Should convert color field from PenaltyCardDto to its String value when mapped to PenaltyCard")
    void Should_ConvertCardColorFiledToString_When_MappedToPenaltyCardIs() {

        var penaltyCard = penaltyCardMapper.toNewPenaltyCard(penaltyCardDto);

        assertThat(penaltyCard.getColor()).isInstanceOf(String.class);
    }

    @Test
    @DisplayName("Should map entities to their referent fields in PenaltyCard when they exist in the database")
    void Should_MapEntitiesToTheirReferentFields_When_TheyExistInTheDatabase() {

        entityManager.merge(existingTeam);
        entityManager.merge(existingMatchSport);

        var penaltyCard = penaltyCardMapper.toNewPenaltyCard(penaltyCardDto);

        assertThat(penaltyCard.getTeam()).isEqualTo(teamService.findTeamById(existingTeam.getId()));
        assertThat(penaltyCard.getMatchSport())
                .isEqualTo(generalMatchSportService.findMatchSportForGoal(existingMatchSport.getId(), Sports.FOOTBALL));
    }

    @Test
    @DisplayName("Should update PenaltyCard fields when new values are passed")
    void Should_UpdatePenaltyCardFields_When_NewValuesArePassed() {

        var penaltyCard = penaltyCardMapper.toExistingPenaltyCard(1L, penaltyCardDto);

        assertThat(penaltyCard.getColor()).isNotEqualTo(existingPenaltyCard.getColor());
        assertThat(penaltyCard.getTeam()).isNotEqualTo(existingPenaltyCard.getTeam());
        assertThat(penaltyCard.getMatchSport()).isNotEqualTo(existingPenaltyCard.getMatchSport());
    }
}