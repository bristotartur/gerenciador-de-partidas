package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.*;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GeneralMatchSportService {

    private final FootballMatchService footballMatchService;
    private final HandballMatchService handballMatchService;
    private final BasketballMatchService basketballMatchService;
    private final VolleyballMatchService volleyballMatchService;
    private final TableTennisMatchService tableTennisMatchService;
    private final ChessMatchService chessMatchService;

    public MatchSport newMatchSport(Sports sport) {

        MatchSport matchSport;

        switch (sport) {

            case FOOTBALL -> matchSport = footballMatchService.saveFootballMatch(new FootballMatch());
            case HANDBALL -> matchSport = handballMatchService.saveHandballMatch(new HandballMatch());
            case BASKETBALL -> matchSport = basketballMatchService.saveBasketballMatch(new BasketballMatch());
            case VOLLEYBALL -> matchSport = volleyballMatchService.saveVolleyballMatch(new VolleyballMatch());
            case TABLE_TENNIS -> matchSport = tableTennisMatchService.saveTableTennisMatch(new TableTennisMatch());
            case CHESS -> matchSport = chessMatchService.saveChessMatch(new ChessMatch());

            default -> throw new BadRequestException(ExceptionMessages.UNSUPPORTED_SPORT.message);
        }
        return matchSport;
    }

    public MatchSport findMatchSport(Sports sport, Long id) {

        MatchSport matchSport;

        switch (sport) {

            case FOOTBALL -> matchSport = footballMatchService.findFootballMatchById(id);
            case HANDBALL -> matchSport = handballMatchService.findHandballMatchById(id);
            case BASKETBALL -> matchSport = basketballMatchService.findBasketballMatchById(id);
            case VOLLEYBALL -> matchSport = volleyballMatchService.findVolleyballMatchById(id);
            case TABLE_TENNIS -> matchSport = tableTennisMatchService.findTableTennisMatchById(id);
            case CHESS -> matchSport = chessMatchService.findChessMatchById(id);

            default -> throw new BadRequestException(ExceptionMessages.UNSUPPORTED_SPORT.message);
        }
        return matchSport;
    }

    public MatchSport findMatchSportForGoal(Long id, Sports sport) {

        if (sport.equals(Sports.FOOTBALL))
            return footballMatchService.findFootballMatchById(id);

        if (sport.equals(Sports.HANDBALL))
            return handballMatchService.findHandballMatchById(id);

        throw new BadRequestException(ExceptionMessages.UNSUPPORTED_FOR_GOALS.message);
    }

    public MatchSport findMatchSportForCard(Long id, Sports sport) {

        MatchSport matchSport;

        switch (sport) {

            case FOOTBALL -> matchSport = footballMatchService.findFootballMatchById(id);
            case HANDBALL -> matchSport = handballMatchService.findHandballMatchById(id);
            case BASKETBALL -> matchSport = basketballMatchService.findBasketballMatchById(id);

            default -> throw new BadRequestException(ExceptionMessages.UNSUPPORTED_FOR_PENALTY_CARDS.message);
        }
        return matchSport;
    }

}
