package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.events.VolleyballMatch;
import org.springframework.stereotype.Repository;

@Repository
public interface VolleyballMatchRepository extends MatchRepository<VolleyballMatch>{
}
