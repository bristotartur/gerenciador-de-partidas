package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.GoalDto;
import com.bristotartur.gerenciadordepartidas.enums.PenaltyCardColor;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.services.GeneralMatchSportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class GoalMapperTest {

    @Autowired
    private GoalMapper goalMapper;
    @Autowired
    private GeneralMatchSportService generalMatchSportService;

    private Goal createNewGoal(Sports sport) {

        return Goal.builder()
                .goalTime(LocalTime.of( 9, 27, 0))
                .team(createNewTeam())
                .matchSport(generalMatchSportService.newMatchSport(sport))
                .build();
    }

    private GoalDto createNewGoalDto(Sports sport) {

        return GoalDto.builder()
                .goalTime(LocalTime.of( 9, 27, 0))
                .teamId(createNewTeam().getId())
                .matchSportId(generalMatchSportService.newMatchSport(sport).getId())
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
    @DisplayName("Should map entities to their referent fields in Goal when they are passed")
    void Should_MapEntitiesToTheirReferentFieldsInGoal_When_TheyArePassed() {

        var sport = Sports.FOOTBALL;
        var matchSport = generalMatchSportService.newMatchSport(sport);
        var team = createNewTeam();
        var goalDto = createNewGoalDto(sport);

        var goal = goalMapper.toNewGoal(goalDto, matchSport, team);

        assertEquals(goal.getMatchSport(), matchSport);
        assertEquals(goal.getTeam(), team);
    }

    @Test
    @DisplayName("Should update Goal when new values are passed")
    void Should_UpdateGoal_When_NewValuesArePassed() {

        var sport = Sports.FOOTBALL;
        var matchSport = generalMatchSportService.newMatchSport(sport);
        var team = createNewTeam();
        var goalDto = createNewGoalDto(sport);

        var existingGoal = createNewGoal(Sports.FOOTBALL);
        var existingId = existingGoal.getId();

        var updatedGoal = goalMapper.toExistingGoal(existingId, goalDto, matchSport, team);

        assertNotEquals(existingGoal, updatedGoal);
    }

}