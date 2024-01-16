package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;
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

import static com.bristotartur.gerenciadordepartidas.utils.EntityTestUtil.createNewGoal;
import static com.bristotartur.gerenciadordepartidas.utils.EntityTestUtil.createNewGoalDto;
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

    @Test
    @DisplayName("Should retrieve all Goals from repository when searching for all Goals")
    void Should_RetrieveAllGoalsFromRepository_When_SearchingForAllGoals() {

        List<Goal> existingGoals = List.of(createNewGoal(Sports.FUTSAL, entityManager), createNewGoal(Sports.HANDBALL, entityManager));
        existingGoals.forEach(goal -> entityManager.merge(goal));

        List<Goal> goalList = goalService.findAllGoals();

        assertEquals(existingGoals, goalList);
    }

    @Test
    @DisplayName("Should find Goal when existing Goal ID is passed to search")
    void Should_FindGoal_When_ExistingGoalIdIsPassedToSearch() {

        var existingGoal = createNewGoal(Sports.FUTSAL, entityManager);

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

        var goalDto = createNewGoalDto(Sports.HANDBALL, entityManager);
        var savedId = goalService.saveGoal(goalDto).getId();

        var savedGoal = goalRepository.findById(savedId).get();

        assertNotNull(savedGoal);
    }

    @Test
    @DisplayName("Should delete Goal from database when Goal ID is passed to delete")
    void Should_DeleteGoalFromDatabase_When_GoalIdIsPassedToDelete() {

        var existingGoal = createNewGoal(Sports.FUTSAL, entityManager);

        entityManager.merge(existingGoal);

        var existingId = existingGoal.getId();
        goalService.deleteGoalById(existingId);

        assertTrue(goalRepository.findById(existingId).isEmpty());
    }

    @Test
    @DisplayName("Should update Goal when GoalDto with new values is passed")
    void Should_UpdateGoal_When_GoalDtoWithNewValuesIsPassed() {

        var existingGoal = createNewGoal(Sports.FUTSAL, entityManager);

        entityManager.merge(existingGoal);

        var existingId = existingGoal.getId();
        var goalDto = createNewGoalDto(Sports.HANDBALL, entityManager);

        var updatedGoal = goalService.replaceGoal(existingId, goalDto);

        assertNotNull(updatedGoal);
        assertNotEquals(existingGoal, updatedGoal);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing ID is passed to replace Goal")
    void Should_ThrowNotFoundException_When_NonExistingIdIsPassedToReplaceGoal() {

        var id = getRandomLongId();
        var goalDto = createNewGoalDto(Sports.FUTSAL, entityManager);

        assertThrows(NotFoundException.class, () -> {
            goalService.replaceGoal(id, goalDto);
        });
    }

}