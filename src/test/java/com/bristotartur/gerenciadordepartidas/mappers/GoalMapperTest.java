package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.utils.GoalTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.MatchTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.ParticipantTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
class GoalMapperTest {

    @Autowired
    private GoalMapper goalMapper;

    @Test
    @DisplayName("Should map entities to their referent fields in Goal when they are passed")
    void Should_MapEntitiesToTheirReferentFieldsInGoal_When_TheyArePassed() {

        var player = ParticipantTestUtil.createNewParticipant("2-53", any(), any());
        var match = MatchTestUtil.createNewMatch(any(), any(), List.of(player));

        var goalDto = GoalTestUtil.createNewGoalDto(any(), any(), Sports.HANDBALL);
        var result = goalMapper.toNewGoal(goalDto, player, match);

        assertEquals(result.getGoalTime(), goalDto.goalTime());
        assertEquals(result.getPlayer(), player);
        assertEquals(result.getMatch(), match);
    }

    @Test
    @DisplayName("Should update Goal when new values are passed")
    void Should_UpdateGoal_When_NewValuesArePassed() {

        var playerA = ParticipantTestUtil.createNewParticipant("2-53", any(), any());
        var playerB = ParticipantTestUtil.createNewParticipant("2-53", any(), any());
        var match = MatchTestUtil.createNewMatch(any(), any(), List.of(playerA));

        var goal = GoalTestUtil.createNewGoal(playerA, match);
        var goalDto = GoalTestUtil.createNewGoalDto(any(), any(), Sports.HANDBALL);

        var result = goalMapper.toExistingGoal(getRandomLongId(), goalDto, playerB, match);

        assertNotEquals(result, goal);
    }

}