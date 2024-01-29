package com.bristotartur.gerenciadordepartidas.utils;

import com.bristotartur.gerenciadordepartidas.domain.actions.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.PenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.enums.PenaltyCardColor;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import jakarta.persistence.EntityManager;

import java.time.LocalTime;

/**
 * Classe utilitária para a geração e gerenciamento de instâncias de {@link PenaltyCard} em
 * testes de integração.
 */
public final class PenaltyCardTestUtil {

    private PenaltyCardTestUtil() {
    }

    /**
     * Cria uma nova instância de de {@link PenaltyCard} sem persistí-la no banco de dados.
     *
     * @param color A cor do cartão.
     * @param player Jogador que recebeu o cartão.
     * @param match A partida onde ocorreu a penalidade.
     * @return Uma nova instância de {@link PenaltyCard}.
     */
    public static PenaltyCard createNewPenaltyCard(PenaltyCardColor color, Participant player, Match match) {

        return PenaltyCard.builder()
                .color(color)
                .player(player)
                .team(player.getTeam())
                .match(match)
                .penaltyCardTime(LocalTime.now())
                .build();
    }

    /**
     * Cria uma nova instância de de {@link PenaltyCard} e a persiste no banco de dados.
     *
     * @param color A cor do cartão.
     * @param player Jogador que recebeu o cartão.
     * @param match A partida onde ocorreu a penalidade.
     * @param entityManager Instância de {@link EntityManager} responsável por gerenciar o ciclo de vida de
     *                      entidades e persistí-las no banco de dados.
     * @return Uma nova instância de {@link PenaltyCard}.
     */
    public static PenaltyCard createNewPenaltyCard(PenaltyCardColor color, Participant player, Match match, EntityManager entityManager) {

        var penaltyCard = PenaltyCard.builder()
                .color(color)
                .player(player)
                .team(player.getTeam())
                .match(match)
                .penaltyCardTime(LocalTime.now())
                .build();

        entityManager.merge(penaltyCard);
        return penaltyCard;
    }

    /**
     * Cria uma nova instância de {@link PenaltyCardDto}.
     *
     * @param sport Modalidade esportica da partida em a penalidade ocorreu.
     * @param color A cor do cartão.
     * @param playerId Identificador único do jogador que recebeu o cartão.
     * @param matchId Identificador único da partida em que a penalidade ocorreu.
     * @return Uma nova instância de {@link PenaltyCardDto}
     */
    public static PenaltyCardDto createNewPenaltyCardDto(Sports sport, PenaltyCardColor color, Long playerId, Long matchId) {

        return PenaltyCardDto.builder()
                .sport(sport)
                .color(color)
                .playerId(playerId)
                .matchId(matchId)
                .penaltyCardTime(LocalTime.now())
                .build();
    }

}
