package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.events.HandballMatch;
import org.springframework.stereotype.Repository;

@Repository
public interface HandballMatchRepository extends MatchRepository<HandballMatch>{
}
