package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository<T extends Match> extends JpaRepository<T, Long> {

    default String findMatchTypeById(Long id, EntityManager entityManager) {

        var query = "SELECT type FROM MATCH WHERE id = :id";

        return entityManager.createNativeQuery(query)
                .setParameter("id", id)
                .getSingleResult()
                .toString();

    }

}
