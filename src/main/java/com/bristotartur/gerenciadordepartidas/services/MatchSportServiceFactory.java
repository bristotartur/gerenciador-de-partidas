package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import org.springframework.context.ApplicationContext;

public interface MatchSportServiceFactory {

    public static MatchSportStrategy newMatchSportService(Sports sport, ApplicationContext context) {

        MatchSportStrategy matchSportStrategy;

        switch (sport) {

            case FOOTBALL -> matchSportStrategy = context.getBean(FootballMatchService.class);
            case HANDBALL -> matchSportStrategy = context.getBean(HandballMatchService.class);
            case BASKETBALL -> matchSportStrategy = context.getBean(BasketballMatchService.class);
            case VOLLEYBALL -> matchSportStrategy = context.getBean(VolleyballMatchService.class);
            case TABLE_TENNIS -> matchSportStrategy = context.getBean(TableTennisMatchService.class);
            case CHESS -> matchSportStrategy = context.getBean(ChessMatchService.class);

            default -> throw new BadRequestException(ExceptionMessages.UNSUPPORTED_SPORT.message);
        }
        return matchSportStrategy;
    }
}
