package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.events.TableTennisMatch;
import org.springframework.stereotype.Repository;

@Repository
public interface TableTennisMatchRepository extends MatchRepository<TableTennisMatch> {
}
