package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository<T extends Match> extends JpaRepository<T, Long> {

    /**
     * Busca pela modalidade esportiva de uma determinada partida com base no seu ID. O valor
     * retornado por este método não corresponde ao valor interno das opções do enum {@link Sports},
     * mas sim ao nome dessas opções.
     *
     * @param id Identificador único da partida.
     * @param entityManager Responsável por gerar a query spara fazer a busca no banco de dados.
     * @return A modalidade esportiva correspondente a partida.
     */
    default String findMatchTypeById(Long id, EntityManager entityManager) {

        var query = "SELECT type FROM MATCH WHERE id = :id";

        return entityManager.createNativeQuery(query)
                .setParameter("id", id)
                .getSingleResult()
                .toString();

    }

}
