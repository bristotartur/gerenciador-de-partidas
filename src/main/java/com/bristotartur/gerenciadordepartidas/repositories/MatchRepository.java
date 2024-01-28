package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

        var query = "SELECT type FROM Match WHERE id = :id";

        return entityManager.createNativeQuery(query)
                .setParameter("id", id)
                .getSingleResult()
                .toString();
    }

    /**
     * Retorna uma lista paginada de todos os jogadores associados a uma determinada instância de {@link Match}.
     *
     * @param id Identificador único da partida.
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Um {@link Page} contendo todos os jogadores associados a partida.
     */
    @Query("SELECT p FROM Participant p JOIN p.matches m WHERE m.id = :id")
    Page<Participant> findMatchPlayers(@Param("id") Long id, Pageable pageable);

}
