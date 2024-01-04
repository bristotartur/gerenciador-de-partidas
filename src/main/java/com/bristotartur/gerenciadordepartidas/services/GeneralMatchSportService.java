package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GeneralMatchSportService {

    private final ApplicationContext context;

    public MatchSport newMatchSport(Sports sport) {
        
        MatchSportStrategy service = MatchSportServiceFactory.newMatchSportService(sport, context);
        return service.createNewMatchSport();
    }

    public MatchSport findMatchSport(Long id, Sports sport) {

        MatchSportStrategy service = MatchSportServiceFactory.newMatchSportService(sport, context);
        return service.findMatchSportById(id);
    }

    public MatchSport findMatchSportForGoal(Long id, Sports sport) {

        MatchSportStrategy service = MatchSportServiceFactory.newMatchSportService(sport, context);

        if (sport.equals(Sports.FOOTBALL) || sport.equals(Sports.HANDBALL))
            return service.findMatchSportById(id);

        throw new BadRequestException(ExceptionMessages.UNSUPPORTED_FOR_GOALS.message);
    }

    public MatchSport findMatchSportForCard(Long id, Sports sport) {

        MatchSportStrategy service = MatchSportServiceFactory.newMatchSportService(sport, context);
        MatchSport matchSport;

        switch (sport) {
            case FOOTBALL, HANDBALL, BASKETBALL -> matchSport = service.findMatchSportById(id);
            default -> throw new BadRequestException(ExceptionMessages.UNSUPPORTED_FOR_PENALTY_CARDS.message);
        }
        return matchSport;
    }

}
