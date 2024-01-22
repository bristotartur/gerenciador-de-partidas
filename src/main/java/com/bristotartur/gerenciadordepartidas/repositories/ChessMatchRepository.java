package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.events.ChessMatch;
import org.springframework.stereotype.Repository;

@Repository
public interface ChessMatchRepository extends MatchRepository<ChessMatch>{
}
