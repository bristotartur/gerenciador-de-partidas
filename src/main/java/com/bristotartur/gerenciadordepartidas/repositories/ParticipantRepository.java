package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    /**
     * Realiza uma query para retornar uma lista paginada contendo entidades do tipo {@link Participant}
     * que tenham o nome semelhante ao fornecido na base de dados.
     *
     * @param name Nome utilizado na pesquisa.
     * @return Uma {@link Page} contendo os participantes com o nome parecido ao informado.
     */
    @Query("SELECT p FROM Participant p WHERE p.name LIKE %:name%")
    Page<Participant> findParticipantsByNameLike(@Param("name") String name, Pageable pageable);

    /**
     * Busca por todas as partidas associadas a uma entidade do tipo {@link Participant}.
     *
     * @param participantId Identificador único do participante.
     * @return Uma lista com todas as partidas associadas a um participante.
     */
    @Query("SELECT m FROM Match m JOIN m.players p WHERE p.id = :participantId")
    List<Match> findMatchesByParticipantId(@Param("participantId") Long participantId);

    /**
     * Procura por todos os membros de uma determinada equipe.
     *
     * @param team Equipe utilizada na pesquisa.
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Uma {@link Page} contendo todos os participantes relacionados a equipe
     */
    @Query("SELECT p FROM Participant p WHERE p.team = :team")
    Page<Participant> findTeamMembers(@Param("team") Team team, Pageable pageable);

    /**
     * Retorna uma lista paginada de todos as partidas associados a uma determinada instância
     * de {@link Participant}.
     *
     * @param id Identificador único do participante.
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Um {@link Page} contendo todas as partidas associados ao participante.
     */
    @Query("SELECT m FROM Match m JOIN m.players p WHERE p.id = :id")
    Page<Match> findParticipantMatches(@Param("id") Long id, Pageable pageable);

}
