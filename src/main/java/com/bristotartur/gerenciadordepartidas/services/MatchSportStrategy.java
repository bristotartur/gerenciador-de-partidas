package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;

public interface MatchSportStrategy<T extends MatchSport> {

    T findMatchSportById(Long id);

    T createNewMatchSport();

}
