package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import org.springframework.context.ApplicationContext;

/**
 * Interface que define uma fábrica para criar instâncias de classes que implementam a estratégia
 * MatchSportStrategy. Esta fábrica permite criar dinamicamente serviços especializados com base
 * no tipo de esporte fornecido.
 */
public interface MatchSportServiceFactory {

    /**
     * Cria uma nova instância de uma classe que implementa MatchSportStrategy especializada
     * no tipo de esporte fornecido.
     *
     * @param sport O tipo de esporte no qual a classe gerada irá lidar.
     * @param context O contexto do Spring responsável por gerenciar a criação e injeção de dependências.
     * @return Uma instância de MatchSportStrategy especializada no esporte fornecido.
     * @throws BadRequestException Se o tipo de esporte fornecida for nulo ou não suportado.
     */
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
