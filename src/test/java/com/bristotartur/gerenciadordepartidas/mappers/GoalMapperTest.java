package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.FootballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.GoalDto;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GoalMapperTest {

    GoalDto goalDto;
    Goal existingGoal;
    LocalTime goalTimeA;
    LocalTime goalTimeB;
    Team team;
    MatchSport matchSport;

    @BeforeEach
    void setUp() {
        goalTimeA = LocalTime.of(13, 35, 00);
        goalTimeB = LocalTime.of(13, 40, 00);

        team = Team.builder()
                .id(1L)
                .name(TeamName.MESTRES_DE_OBRAS.name)
                .build();

        matchSport = FootballMatch.builder()
                .id(1L)
                .build();

        existingGoal = Goal.builder()
                .id(1L)
                .goalTime(goalTimeA).build();

        goalDto = new GoalDto(goalTimeB, team, Sports.FOOTBALL);
    }

    @Test
    void Should_ConvertSportFieldToMatchSport_When_MappedToGoal() {

        var goal = GoalMapper.INSTANCE.toNewGoal(goalDto);

        assertThat(goal.getMatchSport()).isInstanceOf(MatchSport.class);
    }

    @Test
    @DisplayName("Should update Goal fields when new values are passed")
    void Should_UpdateGoalFields_When_NewValuesArePassed() {

        var goal = GoalMapper.INSTANCE.toExistingGoal(1L, goalDto);

        assertThat(goal.getGoalTime()).isNotEqualTo(existingGoal.getGoalTime());
    }
}