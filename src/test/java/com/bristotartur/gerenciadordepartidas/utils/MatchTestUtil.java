package com.bristotartur.gerenciadordepartidas.utils;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.dtos.input.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Team;

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
     * @param teamA Constante de {@link Team} que representa a equipe A da partida.
     * @param teamB Constante de {@link Team} que representa a equipe B da partida.
     * @param players Os jogadores presentes na partida.
     * @return Uma nova instância de {@link Match} com os dados fornecidos.
     */
    public static Match createNewMatch(Team teamA, Team teamB, List<Participant> players, SportEvent event) {

        return Match.builder()
                .teamA(teamA)
                .teamB(teamB)
                .players(players)
                .event(event)
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
     * @param teamA Constante de {@link Team} que representa a equipe A da partida.
     * @param teamB Constante de {@link Team} que representa a equipe B da partida.
     * @param players Os jogadores presentes na partida.
     * @param modality A modalidade da partida, podendo ser feminina, masculina ou mista.
     * @return Uma nova instância de {@link Match} com os dados fornecidos.
     */
    public static Match createNewMatch(Team teamA, Team teamB, List<Participant> players, SportEvent event, Modality modality) {

        return Match.builder()
                .teamA(teamA)
                .teamB(teamB)
                .players(players)
                .event(event)
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
     * @param teamA Constante de {@link Team} que representa a equipe A da partida.
     * @param teamB Constante de {@link Team} que representa a equipe B da partida.
     * @param players Os jogadores presentes na partida.
     * @param status O status da partida, podendo ser agendada, em andamento ou encerrada.
     * @return Uma nova instância de {@link Match} com os dados fornecidos.
     */
    public static Match createNewMatch(Team teamA, Team teamB, List<Participant> players, SportEvent event, Status status) {

        return Match.builder()
                .teamA(teamA)
                .teamB(teamB)
                .players(players)
                .event(event)
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
     * @param teamA Constante de {@link Team} que representa a equipe A da partida.
     * @param teamB Constante de {@link Team} que representa a equipe B da partida.
     * @param playerIds Identificadores únicos dos jogadores presentes na partida.
     * @return Uma nova instância de {@link MatchDto} com os dados passados.
     */
    public static MatchDto createNewMatchDto(Sports sport, Team teamA, Team teamB, List<Long> playerIds, Long eventId) {

        return MatchDto.builder()
                .sport(sport)
                .teamA(teamA)
                .teamB(teamB)
                .eventId(eventId)
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
     * @param teamA Constante de {@link Team} que representa a equipe A da partida.
     * @param teamB Constante de {@link Team} que representa a equipe B da partida.
     * @param playerIds Identificadores únicos dos jogadores presentes na partida.
     * @param modality A modalidade da partida, podendo ser feminina, masculina ou mista.
     * @return Uma nova instância de {@link MatchDto} com os dados passados.
     */
    public static MatchDto createNewMatchDto(Sports sport, Team teamA, Team teamB, List<Long> playerIds, Long eventId, Modality modality) {

        return MatchDto.builder()
                .sport(sport)
                .teamA(teamA)
                .teamB(teamB)
                .eventId(eventId)
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
     * @param teamA Constante de {@link Team} que representa a equipe A da partida.
     * @param teamB Constante de {@link Team} que representa a equipe B da partida.
     * @param playerIds Identificadores únicos dos jogadores presentes na partida.
     * @param status O status da partida, podendo ser agendada, em andamento ou encerrada.
     * @return Uma nova instância de {@link MatchDto} com os dados passados.
     */
    public static MatchDto createNewMatchDto(Sports sport, Team teamA, Team teamB, List<Long> playerIds, Long eventId, Status status) {

        return MatchDto.builder()
                .sport(sport)
                .teamA(teamA)
                .teamB(teamB)
                .eventId(eventId)
                .playerIds(playerIds)
                .modality(Modality.MASCULINE)
                .matchStatus(status)
                .matchStart(LocalDateTime.now())
                .matchEnd(LocalDateTime.now())
                .build();
    }

}
