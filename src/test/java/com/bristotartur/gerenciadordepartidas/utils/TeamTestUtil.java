package com.bristotartur.gerenciadordepartidas.utils;

import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.dtos.TeamDto;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import jakarta.persistence.EntityManager;

/**
 * Classe utilitária para a geração e gerenciamento de instâncias de {@link Team} em
 * testes de integração.
 */
public final class TeamTestUtil {

    private TeamTestUtil() {
    }

    /**
     * Cria uma nova instância de {@link Team} sem persistí-la no banco de dados.
     *
     * @param teamName O nome da equipe.
     * @return Uma nova instância de {@link Team}.
     */
    public static Team createNewTeam(TeamName teamName) {

        return Team.builder()
                .name(teamName.value)
                .points(300)
                .build();
    }

    /**
     * Cria uma nova instância de {@link Team} e a persiste no banco de dados.
     *
     * @param teamName O nome da equipe.
     * @param entityManager Instância de {@link EntityManager} responsável por gerenciar o ciclo de vida de
     *                      entidades e persistí-las no banco de dados.
     * @return Uma nova instância de {@link Team}.
     */
    public static Team createNewTeam(TeamName teamName, EntityManager entityManager) {

        var team = Team.builder()
                .name(teamName.value)
                .points(300)
                .build();

        entityManager.persist(team);
        return team;
    }

    /**
     * Gera uma nova instância de {@link TeamDto}.
     *
     * @param teamName O nome da equipe.
     * @param points A pontuação da equipe
     * @return Uma nova instância de {@link TeamDto} com os dados passados.
     */
    public static TeamDto createNewTeamDto(TeamName teamName, Integer points) {
        return new TeamDto(teamName, points);
    }

}
