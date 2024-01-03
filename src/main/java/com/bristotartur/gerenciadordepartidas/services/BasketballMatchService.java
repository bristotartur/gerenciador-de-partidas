package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.BasketballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.BasketballMatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BasketballMatchService implements MatchSportStrategy<BasketballMatch> {

    private final BasketballMatchRepository basketballMatchRepository;

//    public BasketballMatch findBasketballMatchById(Long id) {
//
//        return basketballMatchRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException(ExceptionMessages.BASKETBALL_MATCH_NOT_FOUND.message));
//    }
//
//    public BasketballMatch saveBasketballMatch(BasketballMatch basketballMatch) {
//        return basketballMatchRepository.save(basketballMatch);
//    }

    @Override
    public MatchSport findMatchSportById(Long id) {

        return basketballMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.BASKETBALL_MATCH_NOT_FOUND.message));
    }

    @Override
    public MatchSport saveMatchSport(BasketballMatch matchSport) {
        return basketballMatchRepository.save(matchSport);
    }
}
