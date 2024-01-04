package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.VolleyballMatch;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.VolleyballMatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VolleyballMatchService implements MatchSportStrategy<VolleyballMatch> {

    private final VolleyballMatchRepository volleyballMatchRepository;

    @Override
    public VolleyballMatch findMatchSportById(Long id) {

        return volleyballMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.VOLLEYBALL_MATCH_NOT_FOUND.message));
    }

    @Override
    public VolleyballMatch createNewMatchSport() {
        return volleyballMatchRepository.save(new VolleyballMatch());
    }
}
