package com.bristotartur.gerenciadordepartidas.utils;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.input.ParticipantDto;
import com.bristotartur.gerenciadordepartidas.enums.Team;
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
     * @param edition A ediçõa em que o participante está inscrito.
     * @return Ua nova instância de {@link Participant}.
     */
    public static Participant createNewParticipant(String classNumber, Team team, Edition edition) {

        return Participant.builder()
                .name("sa")
                .classNumber(classNumber)
                .team(team)
                .edition(edition)
                .build();
    }

    /**
     * Cria uma nova instância de {@link Participant} e a persiste no banco de dados.
     *
     * @param classNumber O número da turma do participante.
     * @param team A equipe associada ao participante.
     * @param edition A ediçõa em que o participante está inscrito.
     * @param entityManager Instância de {@link EntityManager} responsável por gerenciar o ciclo de vida de
     *                      entidades e persistí-las no banco de dados.
     * @return Ua nova instância de {@link Participant}.
     */
    public static Participant createNewParticipant(String classNumber, Team team, Edition edition, EntityManager entityManager) {

        var participant = Participant.builder()
                .name("sa")
                .classNumber(classNumber)
                .team(team)
                .edition(edition)
                .build();

        entityManager.merge(participant);
        return participant;
    }

    /**
     * Cria uma nova instância de {@link Participant} e a persiste no banco de dados.
     *
     * @param name O nome do participante.
     * @param classNumber O número da turma do participante.
     * @param team A equipe associada ao participante.
     * @param edition A ediçõa em que o participante está inscrito.
     * @param entityManager Instância de {@link EntityManager} responsável por gerenciar o ciclo de vida de
     *                      entidades e persistí-las no banco de dados.
     * @return Ua nova instância de {@link Participant}.
     */
    public static Participant createNewParticipant(String name, String classNumber, Team team, Edition edition, EntityManager entityManager) {

        var participant = Participant.builder()
                .name(name)
                .classNumber(classNumber)
                .team(team)
                .edition(edition)
                .build();

        entityManager.merge(participant);
        return participant;
    }

    /**
     * Gera uma nova instância de {@link ParticipantDto}.
     *
     * @param classNumber O número da turma do participante.
     * @param team A equipe associada ao participante.
     * @param editionId Identificador único da edição na qual o participante está associado.
     * @return uma nova instância de {@link ParticipantDto}.
     */
    public static ParticipantDto createNewParticipantDto(String classNumber, Team team, Long editionId) {

        return ParticipantDto.builder()
                .name("sa")
                .classNumber(classNumber)
                .team(team)
                .editionId(editionId)
                .build();
    }

}
