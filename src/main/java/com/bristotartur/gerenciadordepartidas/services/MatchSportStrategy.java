package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;

public interface MatchSportStrategy<T extends MatchSport> {

    MatchSport findMatchSportById(Long id);

    MatchSport saveMatchSport(T matchSport);
}
