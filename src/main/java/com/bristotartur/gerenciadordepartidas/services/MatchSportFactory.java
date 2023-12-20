package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.*;
import com.bristotartur.gerenciadordepartidas.enums.Sports;

public class MatchSportFactory {

    private static final String UNSUPPORTED_SPORT_MESSAGE = "Modalidade esportiva não suportada";

    public static MatchSport newMatchSport(Sports sports) {

        MatchSport matchSport;

        switch (sports) {

            case FOOTBALL -> matchSport = new FootballMatch();
            case HANDBALL -> matchSport = new HandballMatch();
            case VOLLEYBALL -> matchSport = new VolleyballMatch();
            case BASKETBALL -> matchSport = new BasketballMatch();
            case TABLE_TENNIS -> matchSport = new TableTennisMatch();
            case CHESS -> matchSport = new ChessMatch();

            default -> throw new IllegalArgumentException(UNSUPPORTED_SPORT_MESSAGE);
        }
        return matchSport;
    }

}
