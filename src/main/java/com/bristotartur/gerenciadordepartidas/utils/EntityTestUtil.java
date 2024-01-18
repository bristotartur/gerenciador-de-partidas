package com.bristotartur.gerenciadordepartidas.utils;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;
import com.bristotartur.gerenciadordepartidas.domain.match.specifications.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.domain.participant.Participant;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.GoalDto;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.dtos.PenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.enums.MatchStatus;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.PenaltyCardColor;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;

public final class EntityTestUtil {

    private EntityTestUtil() {
    }

    public static Match createNewMatch(Sports sport, EntityManager entityManager) {

        var teamA = createNewTeam(entityManager);
        var teamB = createNewTeam(entityManager);

        return Match.builder()
                .teamA(teamA)
                .teamB(teamB)
                .players(createNewPlayerList(teamA, teamB, entityManager))
                .teamScoreA(3)
                .teamScoreB(2)
                .modality(Modality.MASCULINE.name)
                .matchStatus(MatchStatus.ENDED.name)
                .matchStart(LocalDateTime.of(2024, 1, 10, 13, 57, 0))
                .matchEnd(LocalDateTime.of(2024, 1, 10, 14, 30, 0))
                .build();
    }

    public static MatchDto createNewMatchDto(Sports sport, EntityManager entityManager) {

        var teamA = createNewTeam(entityManager);
        var teamB = createNewTeam(entityManager);
        var playerIds = createNewPlayerList(teamA, teamB, entityManager).stream()
                .map(player -> player.getId())
                .toList();

        return MatchDto.builder()
                .teamAId(teamA.getId())
                .teamBId(teamB.getId())
                .playerIds(playerIds)
                .sport(sport)
                .teamScoreA(3)
                .teamScoreB(2)
                .modality(Modality.MASCULINE)
                .matchStatus(MatchStatus.ENDED)
                .matchStart(LocalDateTime.of(2024, 1, 10, 13, 57, 00))
                .matchEnd(LocalDateTime.of(2024, 1, 10, 14, 30, 00))
                .build();
    }

    public static Goal createNewGoal(Sports sport, Match match) {

        var player = match.getPlayers().get(0);

        return Goal.builder()
                .goalTime(LocalTime.of( 8, 27, 0))
                .player(player)
                .match(match)
                .build();
    }

    public static GoalDto createNewGoalDto(Sports sport, Match match) {

        var player = match.getPlayers().get(3);

        return GoalDto.builder()
                .goalTime(LocalTime.of( 9, 27, 0))
                .playerId(player.getId())
                .matchId(match.getId())
                .sport(sport)
                .build();
    }

    public static PenaltyCard createNewPenaltyCard(Sports sport, PenaltyCardColor color, Match match) {

        var player = match.getPlayers().get(0);

        return PenaltyCard.builder()
                .color(color.name)
                .penaltyCardTime(LocalTime.of( 9, 27, 0))
                .player(player)
                .match(match)
                .build();
    }

    public static PenaltyCardDto createNewPenaltyCardDto(Sports sport, PenaltyCardColor color, Match match) {

        var player = match.getPlayers().get(2);

        return PenaltyCardDto.builder()
                .color(color)
                .penaltyCardTime(LocalTime.of( 9, 27, 0))
                .playerId(player.getId())
                .matchId(match.getId())
                .sport(sport)
                .build();
    }

    public static List<Participant> createNewPlayerList(EntityManager entityManager) {

        List<Participant> players = new LinkedList<>();
        var teamA = createNewTeam(entityManager);
        var teamB = createNewTeam(entityManager);
        var rand = new Random();

        for (int i = 0; i < 10; i++) {

            var team = rand.nextBoolean() ? teamA : teamB;

            var player = Participant.builder()
                    .name("sa")
                    .classNumber("2-53")
                    .team(team)
                    .build();

            entityManager.persist(player);
            players.add(player);
        }
        return players;
    }

    public static List<Participant> createNewPlayerList(Team teamA, Team teamB, EntityManager entityManager) {

        List<Participant> players = new LinkedList<>();
        var rand = new Random();

        for (int i = 0; i < 10; i++) {

            var team = rand.nextBoolean() ? teamA : teamB;

            var player = Participant.builder()
                    .name("sa")
                    .classNumber("2-53")
                    .team(team)
                    .build();

            entityManager.persist(player);
            players.add(player);
        }
        return players;
    }

    private Participant createNewPlayer(Team team) {

        var participant = Participant.builder()
                .name("foo")
                .classNumber("2-53")
                .team(team)
                .build();

        return participant;
    }

    public static Team createNewTeam(EntityManager entityManager) {

        var team = Team.builder()
                .points(300)
                .build();

        entityManager.persist(team);

        return team;
    }

}
