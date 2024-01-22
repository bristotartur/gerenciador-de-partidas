package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.GoalRepository;
import com.bristotartur.gerenciadordepartidas.services.actions.GoalService;
import com.bristotartur.gerenciadordepartidas.services.events.GeneralMatchSportService;
import com.bristotartur.gerenciadordepartidas.utils.GoalTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.MatchTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.ParticipantTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.TeamTestUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

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
    private GeneralMatchSportService generalMatchSportService;

    private Participant playerA;
    private Participant playerB;
    private Match match;

    @BeforeEach
    void setUp() {

        var teamA = TeamTestUtil.createNewTeam(TeamName.MESTRES_DE_OBRAS, entityManager);
        var teamB = TeamTestUtil.createNewTeam(TeamName.PAPA_LEGUAS, entityManager);

        playerA = ParticipantTestUtil.createNewParticipant("1-42", teamA, entityManager);
        playerB = ParticipantTestUtil.createNewParticipant("1-51", teamB, entityManager);

        match = MatchTestUtil.createNewMatch(teamA, teamB, List.of(playerA, playerB));
    }

    @Test
    @DisplayName("Should retrieve all Goals from repository when searching for all Goals")
    void Should_RetrieveAllGoalsFromRepository_When_SearchingForAllGoals() {

        var futsalMatch = generalMatchSportService.saveMatch(match, Sports.FUTSAL);
        var goals = List.of(
                GoalTestUtil.createNewGoal(playerA, futsalMatch, entityManager),
                GoalTestUtil.createNewGoal(playerB, futsalMatch, entityManager));

        var result = goalService.findAllGoals();

        assertEquals(result, goals);
    }

    @Test
    @DisplayName("Should find Goal when existing Goal ID is passed to search")
    void Should_FindGoal_When_ExistingGoalIdIsPassedToSearch() {

        var handballMatch = generalMatchSportService.saveMatch(match, Sports.HANDBALL);
        var goal = GoalTestUtil.createNewGoal(playerA, handballMatch, entityManager);

        var result = goalService.findGoalById(goal.getId());

        assertEquals(result, goal);
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing Goal ID is passed to any method")
    void Should_ThrowNotFoundException_When_NonExistingGoalIdIsPassedToAnyMethod() {

        var id = getRandomLongId();
        var goalDto = GoalTestUtil.createNewGoalDto(any(), any(), any());

        assertThrows(NotFoundException.class, () -> {
            goalService.findGoalById(id);
        });
        assertThrows(NotFoundException.class, () -> {
            goalService.deleteGoalById(id);
        });
        assertThrows(NotFoundException.class, () -> {
            goalService.replaceGoal(id, goalDto);
        });
    }

    @Test
    @DisplayName("Should save Goal when valid GoalDto is passed to save")
    void Should_SaveGoal_When_ValidGoalDtoIsPassedToSave() {

        var sport = Sports.HANDBALL;
        var handballMatch = generalMatchSportService.saveMatch(match, sport);
        var goalDto = GoalTestUtil.createNewGoalDto(playerB.getId(), handballMatch.getId(), sport);

        var result = goalService.saveGoal(goalDto);

        assertEquals(result, goalRepository.findById(result.getId()).get());
    }

    @Test
    @DisplayName("Should delete Goal from database when Goal ID is passed to delete")
    void Should_DeleteGoalFromDatabase_When_GoalIdIsPassedToDelete() {

        var futsalMatch = generalMatchSportService.saveMatch(match, Sports.FUTSAL);
        var goal = GoalTestUtil.createNewGoal(playerA, futsalMatch, entityManager);

        goalService.deleteGoalById(goal.getId());

        assertTrue(goalRepository.findById(goal.getId()).isEmpty());
    }

    @Test
    @DisplayName("Should decrease team score in match when a goal is deleted")
    void Should_DecreaseTeamScoreInMatch_When_GoalIsDeleted() {

        var futsalMatch = generalMatchSportService.saveMatch(match, Sports.FUTSAL);
        futsalMatch.setTeamScoreB(1);

        var goal = GoalTestUtil.createNewGoal(playerB, futsalMatch, entityManager);
        goalService.deleteGoalById(goal.getId());

        assertEquals(futsalMatch.getTeamScoreB(), 0);
    }

    @Test
    @DisplayName("Should update Goal when GoalDto with new values is passed")
    void Should_UpdateGoal_When_GoalDtoWithNewValuesIsPassed() {

        var sport = Sports.FUTSAL;
        var futsalMatch = generalMatchSportService.saveMatch(match, Sports.FUTSAL);

        var goal = GoalTestUtil.createNewGoal(playerA, futsalMatch, entityManager);

        var goalDto = GoalTestUtil.createNewGoalDto(playerB.getId(), futsalMatch.getId(), sport);
        var result = goalService.replaceGoal(goal.getId(), goalDto);

        assertNotEquals(result, goal);
    }

    @Test
    @DisplayName("Should update Team score when Goal player is changed to a player from the opposing Team")
    void Should_UpdateTeamScore_When_GoalPlayerIsChangedToPlayerFromOpposingTeam() {

        var sport = Sports.HANDBALL;
        var handballMatch = generalMatchSportService.saveMatch(match, sport);

        var originalGoalDto = GoalTestUtil.createNewGoalDto(playerA.getId(), handballMatch.getId(), sport);
        var goal = goalService.saveGoal(originalGoalDto);

        var originalTeamScoreA = handballMatch.getTeamScoreA();
        var originalTeamScoreB = handballMatch.getTeamScoreB();

        var newGoalDto = GoalTestUtil.createNewGoalDto(playerB.getId(), handballMatch.getId(), sport);
        goalService.replaceGoal(goal.getId(), newGoalDto);

        assertNotEquals(originalTeamScoreA, handballMatch.getTeamScoreA());
        assertNotEquals(originalTeamScoreB, handballMatch.getTeamScoreB());
        assertEquals(handballMatch.getTeamScoreA(), 0);
        assertEquals(handballMatch.getTeamScoreB(), 1);
    }

    @Test
    @DisplayName("Should not update Team score when Goal player is changed to a player from the same Team")
    void Should_NotUpdateTeamScore_When_GoalPlayerIsChangedToPlayerFromSameTeam() {

        var sport = Sports.FUTSAL;
        var futsalMatch = generalMatchSportService.saveMatch(match, sport);

        var team = match.getTeamB();
        var playerC = ParticipantTestUtil.createNewParticipant("1-52", team, entityManager);
        futsalMatch.setPlayers(List.of(playerA, playerB, playerC));

        var originalGoalDto = GoalTestUtil.createNewGoalDto(playerB.getId(), futsalMatch.getId(), sport);
        var goal = goalService.saveGoal(originalGoalDto);

        var originalTeamScoreA = futsalMatch.getTeamScoreA();
        var originalTeamScoreB = futsalMatch.getTeamScoreB();

        var newGoalDto = GoalTestUtil.createNewGoalDto(playerC.getId(), futsalMatch.getId(), sport);
        goalService.replaceGoal(goal.getId(), newGoalDto);

        assertEquals(originalTeamScoreA, futsalMatch.getTeamScoreA());
        assertEquals(originalTeamScoreB, futsalMatch.getTeamScoreB());
    }

    @Test
    @DisplayName("Should decrease Team score in match when Goal Match is changed")
    void Should_DecreaseTeamScoreInMatch_When_GoalMatchIsChanged() {

        var originalSport = Sports.HANDBALL;
        var handballMatch = generalMatchSportService.saveMatch(match, originalSport);

        var originalGoalDto = GoalTestUtil.createNewGoalDto(playerA.getId(), handballMatch.getId(), originalSport);
        var goal = goalService.saveGoal(originalGoalDto);

        var originalTeamScoreA = handballMatch.getTeamScoreA();

        var newSport = Sports.FUTSAL;
        var futsalMatch = generalMatchSportService.saveMatch(match, newSport);

        var newGoalDto = GoalTestUtil.createNewGoalDto(playerB.getId(), futsalMatch.getId(), newSport);
        goalService.replaceGoal(goal.getId(), newGoalDto);

        assertNotEquals(originalTeamScoreA, handballMatch.getTeamScoreA());
        assertEquals(futsalMatch.getTeamScoreB(), 1);
    }

}