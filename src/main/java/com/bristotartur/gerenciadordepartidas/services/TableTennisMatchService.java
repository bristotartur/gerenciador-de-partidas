package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.TableTennisMatch;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.TableTennisMatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TableTennisMatchService implements MatchSportStrategy<TableTennisMatch> {

    private final TableTennisMatchRepository tableTennisMatchRepository;

    @Override
    public MatchSport findMatchSportById(Long id) {

        return tableTennisMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.TABLE_TENNIS_MATCH_NOT_FOUND.message));

    }

    @Override
    public TableTennisMatch createNewMatchSport() {
        return tableTennisMatchRepository.save(new TableTennisMatch());
    }
}
