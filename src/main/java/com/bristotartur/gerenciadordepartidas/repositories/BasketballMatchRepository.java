package com.bristotartur.gerenciadordepartidas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketballMatchRepository extends JpaRepository<BasketballMatchRepository, Long> {
}
