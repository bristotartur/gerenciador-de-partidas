package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.events.BasketballMatch;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketballMatchRepository extends MatchRepository<BasketballMatch> {
}
