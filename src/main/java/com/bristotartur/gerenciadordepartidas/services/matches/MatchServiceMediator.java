package com.bristotartur.gerenciadordepartidas.services.matches;

import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Esta classe atua como uma camada de serviço centralizada para operações relacionadas a diferentes
 * especializações de {@link Match}. Cada tipo de partida possui um serviço dedicado, sendo este uma
 * implementação de {@link MatchStrategy}, e a função desta classe é fornecer uma ponte entre estas
 * implementações e os demais serviços.
 *
 * @see MatchStrategy
 * @see MatchServiceFactory
 * @see MatchService
 */
@Service
@RequiredArgsConstructor
public class MatchServiceMediator {

    private final ApplicationContext context;

    /**
     * Retorna uma lista paginada contendo todas as instâncias de uma determinada especialização de {@link Match}.
     *
     * @param sport Esporte no qual as instâncias retornadas na lista serão especializadas.
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Um {@link Page} contendo as instâncias da especialização de {@link Match} definida.
     */
    public Page<? extends Match> findMatchesBySport(Sports sport, Pageable pageable) {

        var service = MatchServiceFactory.newMatchSportService(sport, context);
        return service.findAll(pageable);
    }

    /**
     * Encontra uma instância de {@link Match} com base no seu ID e tipo de esporte.
     *
     * @param id O ID único de {@link Match}.
     * @param sport Tipo de esporte associado à {@link Match}.
     * @return A instância correspondente de {@link Match}.
     * @throws NotFoundException Se nenhuma partida correspondente ao ID ou esporte fornecido for encontrada.
     * @throws BadRequestException Se o tipo de esporte fornecido for nulo.
     */
    public Match findMatch(Long id, Sports sport) {

        var service = MatchServiceFactory.newMatchSportService(sport, context);
        return service.findMatchById(id);
    }

    /**
     * Cria uma nova instância de {@link Match} especializada em um esporte específico.
     *
     * @param sport Tipo de esporte na qual a instância de {@link Match} será especializada.
     * @return Uma nova instância de {@link Match} baseada no tipo de esporte fornecido.
     */
    public Match saveMatch(Match match, Sports sport) {

        var service = MatchServiceFactory.newMatchSportService(sport, context);
        return service.saveMatch(match);
    }

    /**
     * Encontra uma instância de {@link Match} associada a um gol com base no ID e tipo de esporte.
     *
     * @param id Identificador único de {@link Match}.
     * @param sport Tipo de esporte associado à {@link Match}.
     * @return A instância correspondente de {@link Match}.
     * @throws NotFoundException Se nenhuma partida correspondente ao ID ou esporte fornecido for encontrada.
     * @throws BadRequestException Se o tipo de esporte não suportar a operação relacionada a gols.
     */
    public Match findMatchForGoal(Long id, Sports sport) {

        var service = MatchServiceFactory.newMatchSportService(sport, context);

        if (sport.equals(Sports.FUTSAL) || sport.equals(Sports.HANDBALL))
            return service.findMatchById(id);

        throw new BadRequestException(ExceptionMessages.UNSUPPORTED_FOR_GOALS.message);
    }

    /**
     * Encontra uma instância de {@link Match} associada a um cartão de penalidade com base
     * no ID e tipo de esporte.
     *
     * @param id Identificador único de {@link Match}.
     * @param sport Tipo de esporte associado à {@link Match}.
     * @return A instância correspondente de {@link Match}.
     * @throws NotFoundException Se nenhuma partida correspondente ao ID ou esporte fornecido for encontrada.
     * @throws BadRequestException Se o tipo de esporte não suportar a operação relacionada a cartões de penalidade.
     */
    public Match findMatchForCard(Long id, Sports sport) {

        var service = MatchServiceFactory.newMatchSportService(sport, context);
        Match match;

        switch (sport) {
            case FUTSAL, HANDBALL, BASKETBALL -> match = service.findMatchById(id);
            default -> throw new BadRequestException(ExceptionMessages.UNSUPPORTED_FOR_PENALTY_CARDS.message);
        }
        return match;
    }

}
