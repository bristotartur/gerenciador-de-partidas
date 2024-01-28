package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.actions.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    /**
     * Busca por todos os gols relacionados a uma determinada partida.
     *
     * @param matchId Identificador único da partida.
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Um {@link Page} contendo os gols relacionados a partida especificada.
     */
    @Query("SELECT g FROM Goal g WHERE g.match.id = :matchId")
    Page<Goal> findMatchGoals(@Param("matchId") Long matchId, Pageable pageable);

}
