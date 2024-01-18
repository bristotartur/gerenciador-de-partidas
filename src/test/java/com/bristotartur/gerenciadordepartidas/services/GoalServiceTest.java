package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.actions.Goal;
import com.bristotartur.gerenciadordepartidas.dtos.GoalDto;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.GoalRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.EntityTestUtil.*;
import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GoalServiceTest {

    @Autowired
    private GoalService goalService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private FutsalMatchService futsalMatchService;
    @Autowired
    private HandballMatchService handballMatchService;

    @Test
    @DisplayName("Should retrieve all Goals from repository when searching for all Goals")
    void Should_RetrieveAllGoalsFromRepository_When_SearchingForAllGoals() {

        var sport = Sports.FUTSAL;
        var match = futsalMatchService.saveMatch(createNewMatch(sport, entityManager));

        List<Goal> existingGoals = List.of(
                createNewGoal(sport, match),
                createNewGoal(sport, match));

        existingGoals.forEach(goal -> entityManager.merge(goal));

        List<Goal> goalList = goalService.findAllGoals();

        assertEquals(existingGoals, goalList);
    }

    @Test
    @DisplayName("Should find Goal when existing Goal ID is passed to search")
    void Should_FindGoal_When_ExistingGoalIdIsPassedToSearch() {

        var sport = Sports.FUTSAL;
        var match = futsalMatchService.saveMatch(createNewMatch(sport, entityManager));
        var existingGoal = createNewGoal(sport, match);

        entityManager.merge(existingGoal);

        var existingId = existingGoal.getId();
        var goal = goalService.findGoalById(existingId);

        assertEquals(goal, existingGoal);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing Goal ID is passed to search")
    void Should_ThrowNotFoundException_When_NonExistingGoalIdIsPassedToSearch() {

        var id = getRandomLongId();

        assertThrows(NotFoundException.class, () -> {
            goalService.findGoalById(id);
        });
    }

    @Test
    @DisplayName("Should save Goal when valid GoalDto is passed to save")
    void Should_SaveGoal_When_ValidGoalDtoIsPassedToSave() {

        var sport = Sports.HANDBALL;
        var match = handballMatchService.saveMatch(createNewMatch(sport, entityManager));

        var goalDto = createNewGoalDto(sport, match);
        var savedId = goalService.saveGoal(goalDto).getId();

        var savedGoal = goalRepository.findById(savedId).get();

        assertNotNull(savedGoal);
    }

    @Test
    @DisplayName("Should delete Goal from database when Goal ID is passed to delete")
    void Should_DeleteGoalFromDatabase_When_GoalIdIsPassedToDelete() {

        var sport = Sports.FUTSAL;
        var match = futsalMatchService.saveMatch(createNewMatch(sport, entityManager));
        var existingGoal = createNewGoal(sport, match);

        entityManager.merge(existingGoal);

        var existingId = existingGoal.getId();
        goalService.deleteGoalById(existingId);

        assertTrue(goalRepository.findById(existingId).isEmpty());
    }

    @Test
    @DisplayName("Should update Goal when GoalDto with new values is passed")
    void Should_UpdateGoal_When_GoalDtoWithNewValuesIsPassed() {

        var sport = Sports.FUTSAL;
        var match = futsalMatchService.saveMatch(createNewMatch(sport, entityManager));
        var existingGoal = createNewGoal(sport, match);

        entityManager.merge(existingGoal);

        var existingId = existingGoal.getId();
        var newSport = Sports.HANDBALL;
        var newMatch = handballMatchService.saveMatch(createNewMatch(sport, entityManager));
        var goalDto = createNewGoalDto(newSport, newMatch);

        var updatedGoal = goalService.replaceGoal(existingId, goalDto);

        assertNotNull(updatedGoal);
        assertNotEquals(existingGoal, updatedGoal);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing ID is passed to replace Goal")
    void Should_ThrowNotFoundException_When_NonExistingIdIsPassedToReplaceGoal() {

        var id = getRandomLongId();

        var goalDto = GoalDto.builder().build();

        assertThrows(NotFoundException.class, () -> {
            goalService.replaceGoal(id, goalDto);
        });
    }

}