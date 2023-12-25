package com.bristotartur.gerenciadordepartidas.utils;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.*;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import lombok.experimental.UtilityClass;

public class MatchSportFactory {

    public static MatchSport newMatchSport(Sports sports) {

        MatchSport matchSport;

        switch (sports) {

            case FOOTBALL -> matchSport = new FootballMatch();
            case HANDBALL -> matchSport = new HandballMatch();
            case VOLLEYBALL -> matchSport = new VolleyballMatch();
            case BASKETBALL -> matchSport = new BasketballMatch();
            case TABLE_TENNIS -> matchSport = new TableTennisMatch();
            case CHESS -> matchSport = new ChessMatch();

            default -> throw new IllegalArgumentException(ExceptionMessages.UNSUPPORTED_SPORT.message);
        }
        return matchSport;
    }

}
