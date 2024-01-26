package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    /**
     * Realiza uma query para retornar todas as entidades do tipo {@link Participant} que tenham um nome
     * semelhante ao fornecido na base de dados.
     *
     * @param name Nome utilizado na pesquisa.
     * @return Todos os participantes com o nome parecido ao informado.
     */
    @Query("SELECT p FROM Participant p WHERE p.name LIKE %:name%")
    List<Participant> findParticipantsByNameLike(@Param("name") String name);

}
