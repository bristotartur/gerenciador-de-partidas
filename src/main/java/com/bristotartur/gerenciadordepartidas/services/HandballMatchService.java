package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.HandballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.HandballMatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HandballMatchService implements MatchSportStrategy<HandballMatch> {

    private final HandballMatchRepository handballMatchRepository;

//    public HandballMatch findHandballMatchById(Long id) {
//
//        return handballMatchRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException(ExceptionMessages.HANDBALL_MATCH_NOT_FOUND.message));
//    }
//
//    public HandballMatch saveHandballMatch(HandballMatch handballMatch) {
//        return handballMatchRepository.save(handballMatch);
//    }

    @Override
    public MatchSport findMatchSportById(Long id) {

        return handballMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.HANDBALL_MATCH_NOT_FOUND.message));
    }

    @Override
    public MatchSport saveMatchSport(HandballMatch matchSport) {
        return handballMatchRepository.save(matchSport);
    }
}
