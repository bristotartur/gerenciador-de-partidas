package com.bristotartur.gerenciadordepartidas.utils;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;

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
     * @param teamA Constante de {@link TeamName} que representa a equipe A da partida.
     * @param teamB Constante de {@link TeamName} que representa a equipe B da partida.
     * @param players Os jogadores presentes na partida.
     * @return Uma nova instância de {@link Match} com os dados fornecidos.
     */
    public static Match createNewMatch(TeamName teamA, TeamName teamB, List<Participant> players) {

        return Match.builder()
                .teamA(teamA)
                .teamB(teamB)
                .players(players)
                .teamScoreA(0)
                .teamScoreB(0)
                .modality(Modality.MASCULINE)
                .matchStatus(Status.IN_PROGRESS)
                .matchStart(LocalDateTime.now())
                .matchEnd(LocalDateTime.now())
                .build();
    }

    /**
     * Cria uma nova instância de {@link Match} sem persistí-la no banco de dados.
     *
     * @param teamA Constante de {@link TeamName} que representa a equipe A da partida.
     * @param teamB Constante de {@link TeamName} que representa a equipe B da partida.
     * @param players Os jogadores presentes na partida.
     * @param modality A modalidade da partida, podendo ser feminina, masculina ou mista.
     * @return Uma nova instância de {@link Match} com os dados fornecidos.
     */
    public static Match createNewMatch(TeamName teamA, TeamName teamB, List<Participant> players, Modality modality) {

        return Match.builder()
                .teamA(teamA)
                .teamB(teamB)
                .players(players)
                .teamScoreA(0)
                .teamScoreB(0)
                .modality(modality)
                .matchStatus(Status.IN_PROGRESS)
                .matchStart(LocalDateTime.now())
                .matchEnd(LocalDateTime.now())
                .build();
    }

    /**
     * Cria uma nova instância de {@link Match} sem persistí-la no banco de dados.
     *
     * @param teamA Constante de {@link TeamName} que representa a equipe A da partida.
     * @param teamB Constante de {@link TeamName} que representa a equipe B da partida.
     * @param players Os jogadores presentes na partida.
     * @param status O status da partida, podendo ser agendada, em andamento ou encerrada.
     * @return Uma nova instância de {@link Match} com os dados fornecidos.
     */
    public static Match createNewMatch(TeamName teamA, TeamName teamB, List<Participant> players, Status status) {

        return Match.builder()
                .teamA(teamA)
                .teamB(teamB)
                .players(players)
                .teamScoreA(0)
                .teamScoreB(0)
                .modality(Modality.MASCULINE)
                .matchStatus(status)
                .matchStart(LocalDateTime.now())
                .matchEnd(LocalDateTime.now())
                .build();
    }

    /**
     * Cria uma nova instância de {@link MatchDto}.
     *
     * @param sport O tipo de esporte da partida.
     * @param teamA Constante de {@link TeamName} que representa a equipe A da partida.
     * @param teamB Constante de {@link TeamName} que representa a equipe B da partida.
     * @param playerIds Identificadores únicos dos jogadores presentes na partida.
     * @return Uma nova instância de {@link MatchDto} com os dados passados.
     */
    public static MatchDto createNewMatchDto(Sports sport, TeamName teamA, TeamName teamB, List<Long> playerIds) {

        return MatchDto.builder()
                .sport(sport)
                .teamA(teamA)
                .teamB(teamB)
                .playerIds(playerIds)
                .modality(Modality.MASCULINE)
                .matchStatus(Status.IN_PROGRESS)
                .matchStart(LocalDateTime.now())
                .matchEnd(LocalDateTime.now())
                .build();
    }

    /**
     * Cria uma nova instância de {@link MatchDto}.
     *
     * @param sport O tipo de esporte da partida.
     * @param teamA Constante de {@link TeamName} que representa a equipe A da partida.
     * @param teamB Constante de {@link TeamName} que representa a equipe B da partida.
     * @param playerIds Identificadores únicos dos jogadores presentes na partida.
     * @param modality A modalidade da partida, podendo ser feminina, masculina ou mista.
     * @return Uma nova instância de {@link MatchDto} com os dados passados.
     */
    public static MatchDto createNewMatchDto(Sports sport, TeamName teamA, TeamName teamB, List<Long> playerIds, Modality modality) {

        return MatchDto.builder()
                .sport(sport)
                .teamA(teamA)
                .teamB(teamB)
                .playerIds(playerIds)
                .modality(modality)
                .matchStatus(Status.IN_PROGRESS)
                .matchStart(LocalDateTime.now())
                .matchEnd(LocalDateTime.now())
                .build();
    }

    /**
     * Cria uma nova instância de {@link MatchDto}.
     *
     * @param sport O tipo de esporte da partida.
     * @param teamA Constante de {@link TeamName} que representa a equipe A da partida.
     * @param teamB Constante de {@link TeamName} que representa a equipe B da partida.
     * @param playerIds Identificadores únicos dos jogadores presentes na partida.
     * @param status O status da partida, podendo ser agendada, em andamento ou encerrada.
     * @return Uma nova instância de {@link MatchDto} com os dados passados.
     */
    public static MatchDto createNewMatchDto(Sports sport, TeamName teamA, TeamName teamB, List<Long> playerIds, Status status) {

        return MatchDto.builder()
                .sport(sport)
                .teamA(teamA)
                .teamB(teamB)
                .playerIds(playerIds)
                .modality(Modality.MASCULINE)
                .matchStatus(status)
                .matchStart(LocalDateTime.now())
                .matchEnd(LocalDateTime.now())
                .build();
    }

}
