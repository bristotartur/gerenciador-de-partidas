package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.FootballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.HandballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GeneralMatchSportService {

    private final FootballMatchService footballMatchService;
    private final HandballMatchService handballMatchService;

    public MatchSport findMatchSportForGoal(Long id, Sports sport) {

        if (sport.equals(Sports.FOOTBALL))
            return footballMatchService.findFootballMatchById(id);

        if (sport.equals(Sports.HANDBALL))
            return handballMatchService.findHandballMatchById(id);

        throw new IllegalArgumentException(ExceptionMessages.UNSUPPORTED_FOR_GOALS.message);
    }

    public MatchSport findMatchSportForCard(Long id, Sports sport) {

        MatchSport matchSport;

        switch (sport) {

            case FOOTBALL -> matchSport = footballMatchService.findFootballMatchById(id);
            case HANDBALL -> matchSport = handballMatchService.findHandballMatchById(id);

            default -> throw new BadRequestException(ExceptionMessages.UNSUPPORTED_FOR_PENALTY_CARDS.message);
        }
        return matchSport;
    }
}
