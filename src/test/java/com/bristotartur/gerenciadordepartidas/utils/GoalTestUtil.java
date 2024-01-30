package com.bristotartur.gerenciadordepartidas.utils;

import com.bristotartur.gerenciadordepartidas.domain.actions.Goal;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.dtos.GoalDto;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import jakarta.persistence.EntityManager;

import java.time.LocalTime;

/**
 * Classe utilitária para a geração e gerenciamento de instâncias de {@link Goal} em
 * testes de integração.
 */
public final class GoalTestUtil {

    private GoalTestUtil() {
    }

    /**
     * Cria uma nova instância de {@link Goal} sem persistí-la no banco de dados.
     *
     * @param player O jogador que marcou o gol.
     * @param match A partida em que o gol ocorreu.
     * @return Uma nova instância de {@link Goal}.
     */
    public static Goal createNewGoal(Participant player, Match match) {

        return Goal.builder()
                .goalTime(LocalTime.now())
                .player(player)
                .team(player.getTeam())
                .match(match)
                .build();
    }

    /**
     * Cria uma nova instância de {@link Goal} e a persiste no banco de dados.
     *
     * @param player O jogador que marcou o gol.
     * @param match A partida em que o gol ocorreu.
     * @param entityManager Instância de {@link EntityManager} responsável por gerenciar o ciclo de vida de
     *                      entidades e persistí-las no banco de dados.
     * @return Uma nova instância de {@link Goal}.
     */
    public static Goal createNewGoal(Participant player, Match match, EntityManager entityManager) {

        var goal = Goal.builder()
                .goalTime(LocalTime.now())
                .player(player)
                .team(player.getTeam())
                .match(match)
                .build();

        entityManager.merge(goal);
        return goal;
    }

    /**
     * Cria uma nova instância de {@link GoalDto}.
     *
     * @param playerId Identificador único do jogador que marcou o gol.
     * @param matchId Identificador único da partida em que o gol ocorreu.
     * @return Uma nova instância de {@link GoalDto} com os dados passados.
     */
    public static GoalDto createNewGoalDto(Long playerId, Long matchId, Sports sport) {

        return GoalDto.builder()
                .goalTime(LocalTime.now())
                .playerId(playerId)
                .matchId(matchId)
                .sport(sport)
                .build();
    }

}
