package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.domain.participant.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.GoalDto;
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
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class GoalMapperTest {

    @Autowired
    private GoalMapper goalMapper;
    @Autowired
    private GeneralMatchSportService generalMatchSportService;

    private Goal createNewGoal(Sports sport) {

        return Goal.builder()
                .goalTime(LocalTime.of(9, 27, 0))
                .player(createNewPlayer())
                .match(createNewMatch())
                .build();
    }

    private GoalDto createNewGoalDto(Sports sport) {

        var match = Match.builder().id(getRandomLongId()).build();

        return GoalDto.builder()
                .goalTime(LocalTime.of(9, 27, 0))
                .playerId(getRandomLongId())
                .matchId(createNewMatch().getId())
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
    @DisplayName("Should map entities to their referent fields in Goal when they are passed")
    void Should_MapEntitiesToTheirReferentFieldsInGoal_When_TheyArePassed() {

        var sport = Sports.FUTSAL;
        var match = createNewMatch();
        var player = createNewPlayer();
        var goalDto = createNewGoalDto(sport);

        var goal = goalMapper.toNewGoal(goalDto, player, match);

        assertEquals(goal.getMatch(), match);
        assertEquals(goal.getPlayer(), player);
    }

    @Test
    @DisplayName("Should update Goal when new values are passed")
    void Should_UpdateGoal_When_NewValuesArePassed() {

        var sport = Sports.FUTSAL;
        var match = createNewMatch();
        var player = createNewPlayer();
        var goalDto = createNewGoalDto(sport);

        var existingGoal = createNewGoal(Sports.FUTSAL);
        var existingId = existingGoal.getId();

        var updatedGoal = goalMapper.toExistingGoal(existingId, goalDto, player, match);

        assertNotEquals(existingGoal, updatedGoal);
    }

}