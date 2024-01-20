package com.bristotartur.gerenciadordepartidas.utils;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.dtos.ParticipantDto;
import jakarta.persistence.EntityManager;

/**
 * Classe utilitária para a geração e gerenciamento de instâncias de {@link Participant} em
 * testes de integração.
 */
public final class ParticipantTestUtil {

    private ParticipantTestUtil() {
    }

    /**
     * Cria uma nova instância de {@link Participant} sem persistí-la no banco de dados.
     *
     * @param classNumber O número da turma do participante.
     * @param team A equipe associada ao participante.
     * @return Ua nova instância de {@link Participant}.
     */
    public static Participant createNewParticipant(String classNumber, Team team) {

        return Participant.builder()
                .name("sa")
                .classNumber(classNumber)
                .team(team)
                .build();
    }

    /**
     * Cria uma nova instância de {@link Participant} e a persiste no banco de dados.
     *
     * @param classNumber O número da turma do participante.
     * @param team A equipe associada ao participante.
     * @param entityManager Instância de {@link EntityManager} responsável por gerenciar o ciclo de vida de
     *                      entidades e persistí-las no banco de dados.
     * @return Ua nova instância de {@link Participant}.
     */
    public static Participant createNewParticipant(String classNumber, Team team, EntityManager entityManager) {

        var participant = Participant.builder()
                .name("sa")
                .classNumber(classNumber)
                .team(team)
                .build();

        entityManager.merge(participant);
        return participant;
    }

    /**
     * Gera uma nova instância de {@link ParticipantDto}.
     *
     * @param classNumber O número da turma do participante.
     * @param team A equipe associada ao participante.
     * @return uma nova instância de {@link ParticipantDto}.
     */
    public static ParticipantDto createNewParticipantDto(String classNumber, Long teamId) {

        return ParticipantDto.builder()
                .name("sa")
                .classNumber(classNumber)
                .teamId(teamId)
                .build();
    }

}
