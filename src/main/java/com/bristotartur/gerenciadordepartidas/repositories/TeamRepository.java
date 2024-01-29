package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByName(String name);

    /**
     * Procura por todos os membros de uma equipe pelo seu ID.
     *
     * @param teamId Identificador único da equipe.
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Uma {@link Page} contendo todos os participantes relacionados a equipe
     */
    @Query("SELECT p FROM Participant p WHERE p.team.id = :teamId")
    Page<Participant> findTeamMembers(@Param("teamId") Long teamId, Pageable pageable);

}
