package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.HandballMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandballMatchRepository extends JpaRepository<HandballMatch, Long> {
}
