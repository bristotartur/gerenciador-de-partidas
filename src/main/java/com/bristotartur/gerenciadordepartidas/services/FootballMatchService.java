package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.FootballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.FootballMatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FootballMatchService implements MatchSportStrategy<FootballMatch> {

    private final FootballMatchRepository footballMatchRepository;

    @Override
    public MatchSport findMatchSportById(Long id) {

        return footballMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.FOOTBALL_MATCH_NOT_FOUND.message));
    }

    @Override
    public FootballMatch createNewMatchSport() {
        return footballMatchRepository.save(new FootballMatch());
    }

}
