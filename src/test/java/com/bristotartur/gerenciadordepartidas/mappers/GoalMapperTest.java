package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.FootballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.GoalDto;
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

import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class GoalMapperTest {

    @Autowired
    private EntityManager entityManager;

    @Mock
    private TeamService teamService;
    @Mock
    private GeneralMatchSportService generalMatchSportService;
    @InjectMocks
    private GoalMapper goalMapper;

    private GoalDto goalDtoA;
    private GoalDto goalDtoB;
    private Goal existingGoal;
    private LocalTime goalTimeA;
    private LocalTime goalTimeB;
    private Team existingTeamA;
    private Team existingTeamB;
    private FootballMatch existingMatchSportA;
    private FootballMatch existingMatchSportB;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        goalTimeA = LocalTime.of(13, 35, 00);
        goalTimeB = LocalTime.of(13, 36, 00);

        existingTeamA = createTeam();
        existingTeamB = createTeam();
        existingMatchSportA = createFootballMatch();
        existingMatchSportB = createFootballMatch();
        existingGoal = createGoal(existingTeamA, existingMatchSportA);

        goalDtoA = new GoalDto(goalTimeA, existingTeamA.getId(), existingMatchSportA.getId(), Sports.FOOTBALL);
        goalDtoB = new GoalDto(goalTimeB, existingTeamB.getId(), existingMatchSportB.getId(), Sports.FOOTBALL);
    }

    private Team createTeam() {
        return Team.builder().id(RandomIdUtil.getRandomLongId()).build();
    }

    private FootballMatch createFootballMatch() {
        return FootballMatch.builder().id(RandomIdUtil.getRandomLongId()).build();
    }

    private Goal createGoal(Team team, MatchSport matchSport) {

        return Goal.builder()
                .id(RandomIdUtil.getRandomLongId())
                .goalTime(any())
                .team(team)
                .matchSport(matchSport).build();
    }

    @Test
    @DisplayName("Should map entities to their referent fields in Goal when they exist in the database")
    void Should_MapEntitiesToTheirReferentFields_When_TheyExistInTheDatabase() {

        entityManager.merge(existingTeamA);
        entityManager.merge(existingMatchSportA);

        var goal = goalMapper.toNewGoal(goalDtoA);

        assertThat(goal.getTeam()).isEqualTo(teamService.findTeamById(existingTeamA.getId()));
        assertThat(goal.getMatchSport())
                .isEqualTo(generalMatchSportService.findMatchSportForGoal(existingMatchSportA.getId(), Sports.FOOTBALL));
    }

    @Test
    @DisplayName("Should update Goal fields when new values are passed")
    void Should_UpdateGoalFields_When_NewValuesArePassed() {

        when(teamService.findTeamById(existingTeamB.getId())).thenReturn(existingTeamB);
        when(generalMatchSportService.findMatchSportForGoal(existingTeamB.getId(), Sports.FOOTBALL))
                .thenReturn(existingMatchSportB);

        var goal = goalMapper.toExistingGoal(existingGoal.getId(), goalDtoB);

        assertThat(goal.getGoalTime()).isNotEqualTo(existingGoal.getGoalTime());
        assertThat(goal.getTeam()).isNotEqualTo(existingGoal.getTeam());
        assertThat(goal.getMatchSport()).isNotEqualTo(existingGoal.getMatchSport());
    }

}