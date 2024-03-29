package com.bristotartur.gerenciadordepartidas.services.matches;

import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import org.springframework.context.ApplicationContext;

/**
 * Interface que define uma fábrica para criar instâncias de classes que implementam a estratégia
 * {@link MatchStrategy}. Esta fábrica permite criar dinamicamente serviços especializados com base
 * no tipo de esporte fornecido.
 *
 * @see MatchServiceMediator
 */
public interface MatchServiceFactory {

    /**
     * Cria uma nova instância de um serviço que implemente {@link MatchStrategy} especializado
     * no tipo de esporte fornecido.
     *
     * @param sport O tipo de esporte no qual o serviço gerado irá lidar.
     * @param context O contexto do Spring responsável por gerenciar a criação e injeção de dependências
     *                para o serviço gerado.
     * @return Uma implementação de {@link MatchStrategy} especializada no esporte fornecido.
     * @throws BadRequestException Se o tipo de esporte fornecida for nulo ou não suportado.
     */
    static MatchStrategy<? extends Match> newMatchSportService(Sports sport, ApplicationContext context) {

        MatchStrategy<? extends Match> service;

        switch (sport) {

            case FUTSAL -> service = context.getBean(FutsalMatchService.class);
            case HANDBALL -> service = context.getBean(HandballMatchService.class);
            case BASKETBALL -> service = context.getBean(BasketballMatchService.class);
            case VOLLEYBALL -> service = context.getBean(VolleyballMatchService.class);
            case TABLE_TENNIS -> service = context.getBean(TableTennisMatchService.class);
            case CHESS -> service = context.getBean(ChessMatchService.class);

            default -> throw new BadRequestException(ExceptionMessages.INVALID_SPORT.message);
        }
        return service;
    }

}
