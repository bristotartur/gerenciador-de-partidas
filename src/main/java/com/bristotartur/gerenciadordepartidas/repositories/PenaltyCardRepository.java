package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.PenaltyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenaltyCardRepository extends JpaRepository<PenaltyCard, Long> {
}
