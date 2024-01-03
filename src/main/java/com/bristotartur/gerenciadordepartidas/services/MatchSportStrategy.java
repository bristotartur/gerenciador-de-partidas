package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;

public interface MatchSportStrategy<T extends MatchSport> {

    MatchSport findMatchSportById(Long id);

    T createNewMatchSport();

}
