package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.events.FutsalMatch;
import org.springframework.stereotype.Repository;

@Repository
public interface FutsalMatchRepository extends MatchRepository<FutsalMatch> {
}
