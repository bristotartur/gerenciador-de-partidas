package com.bristotartur.gerenciadordepartidas.utils;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.MatchStatus;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe utilitária para a geração e gerenciamento de instâncias de {@link Match} em
 * testes de integração.
 */
public final class MatchTestUtil {

    private MatchTestUtil() {
    }

    /**
     * Cria uma nova instância de {@link Match} sem persistí-la no banco de dados.
     *
     * @param teamA Instância de {@link Team} que representa a equipe A da partida.
     * @param teamB Instância de {@link Team} que representa a equipe B da partida.
     * @param players Os jogadores presentes na partida.
     * @return Uma nova instância de {@link Match} com os dados fornecidos.
     */
    public static Match createNewMatch(Team teamA, Team teamB, List<Participant> players) {

        return Match.builder()
                .teamA(teamA)
                .teamB(teamB)
                .players(players)
                .teamScoreA(0)
                .teamScoreB(0)
                .modality(Modality.MASCULINE.name)
                .matchStatus(MatchStatus.IN_PROGRESS.name)
                .matchStart(LocalDateTime.now())
                .matchEnd(LocalDateTime.now())
                .build();
    }

    /**
     * Cria uma nova instância de {@link Match} sem persistí-la no banco de dados.
     *
     * @param teamA Instância de {@link Team} que representa a equipe A da partida.
     * @param teamB Instância de {@link Team} que representa a equipe B da partida.
     * @param players Os jogadores presentes na partida.
     * @param modality A modalidade da partida, podendo ser feminina, masculina ou mista.
     * @return Uma nova instância de {@link Match} com os dados fornecidos.
     */
    public static Match createNewMatch(Team teamA, Team teamB, List<Participant> players, Modality modality) {

        return Match.builder()
                .teamA(teamA)
                .teamB(teamB)
                .players(players)
                .teamScoreA(0)
                .teamScoreB(0)
                .modality(modality.name)
                .matchStatus(MatchStatus.IN_PROGRESS.name)
                .matchStart(LocalDateTime.now())
                .matchEnd(LocalDateTime.now())
                .build();
    }

    /**
     * Cria uma nova instância de {@link Match} sem persistí-la no banco de dados.
     *
     * @param teamA Instância de {@link Team} que representa a equipe A da partida.
     * @param teamB Instância de {@link Team} que representa a equipe B da partida.
     * @param players Os jogadores presentes na partida.
     * @param status O status da partida, podendo ser agendada, em andamento ou encerrada.
     * @return Uma nova instância de {@link Match} com os dados fornecidos.
     */
    public static Match createNewMatch(Team teamA, Team teamB, List<Participant> players, MatchStatus status) {

        return Match.builder()
                .teamA(teamA)
                .teamB(teamB)
                .players(players)
                .teamScoreA(0)
                .teamScoreB(0)
                .modality(Modality.MASCULINE.name)
                .matchStatus(status.name)
                .matchStart(LocalDateTime.now())
                .matchEnd(LocalDateTime.now())
                .build();
    }

    /**
     * Cria uma nova instância de {@link MatchDto}.
     *
     * @param sport O tipo de esporte da partida.
     * @param teamAId Identificador único da equipe que representa a equipe A na partida.
     * @param teamBId Identificador único da equipe que representa a equipe B na partida.
     * @param playerIds Identificadores únicos dos jogadores presentes na partida.
     * @return Uma nova instância de {@link MatchDto} com os dados passados.
     */
    public static MatchDto createNewMatchDto(Sports sport, Long teamAId, Long teamBId, List<Long> playerIds) {

        return MatchDto.builder()
                .sport(sport)
                .teamAId(teamAId)
                .teamBId(teamBId)
                .playerIds(playerIds)
                .modality(Modality.MASCULINE)
                .matchStatus(MatchStatus.IN_PROGRESS)
                .matchStart(LocalDateTime.now())
                .matchEnd(LocalDateTime.now())
                .build();
    }

    /**
     * Cria uma nova instância de {@link MatchDto}.
     *
     * @param sport O tipo de esporte da partida.
     * @param teamAId Identificador único da equipe que representa a equipe A na partida.
     * @param teamBId Identificador único da equipe que representa a equipe B na partida.
     * @param playerIds Identificadores únicos dos jogadores presentes na partida.
     * @param modality A modalidade da partida, podendo ser feminina, masculina ou mista.
     * @return Uma nova instância de {@link MatchDto} com os dados passados.
     */
    public static MatchDto createNewMatchDto(Sports sport, Long teamAId, Long teamBId, List<Long> playerIds, Modality modality) {

        return MatchDto.builder()
                .sport(sport)
                .teamAId(teamAId)
                .teamBId(teamBId)
                .playerIds(playerIds)
                .modality(modality)
                .matchStatus(MatchStatus.IN_PROGRESS)
                .matchStart(LocalDateTime.now())
                .matchEnd(LocalDateTime.now())
                .build();
    }

    /**
     * Cria uma nova instância de {@link MatchDto}.
     *
     * @param sport O tipo de esporte da partida.
     * @param teamAId Identificador único da equipe que representa a equipe A na partida.
     * @param teamBId Identificador único da equipe que representa a equipe B na partida.
     * @param playerIds Identificadores únicos dos jogadores presentes na partida.
     * @param status O status da partida, podendo ser agendada, em andamento ou encerrada.
     * @return Uma nova instância de {@link MatchDto} com os dados passados.
     */
    public static MatchDto createNewMatchDto(Sports sport, Long teamAId, Long teamBId, List<Long> playerIds, MatchStatus status) {

        return MatchDto.builder()
                .sport(sport)
                .teamAId(teamAId)
                .teamBId(teamBId)
                .playerIds(playerIds)
                .modality(Modality.MASCULINE)
                .matchStatus(status)
                .matchStart(LocalDateTime.now())
                .matchEnd(LocalDateTime.now())
                .build();
    }

}
