package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Esta classe atua como uma camada de serviço centralizada para operações relacionadas a diferentes
 * tipos de partidas ({@link Match}). Através desta classe é possível criar novas instâncias
 * de {@link Match}, encontrar detalhes específicos de uma partida, como partidas relacionadas a gols ou cartões,
 * e manipular operações gerais associadas a serviços de MatchSport. <br> <br>
 *
 * Ela utiliza uma fábrica, {@link MatchSportServiceFactory}, para criar dinamicamente serviços especializados com
 * base no tipo de esporte fornecido.
 *
 * @see MatchStrategy
 */
@Service
@RequiredArgsConstructor
public class GeneralMatchSportService {

    private final ApplicationContext context;

    /**
     * Retorna uma lista contendo todas as instâncias de uma determinada especialização de {@link Match}.
     *
     * @param sport Esporte no qual as instâncias retornadas na lista serão especializadas.
     * @return Uma lista contendo as instâncias da especialização de {@link Match} definida.
     */
    public List<? extends Match> findMatchesBySport(Sports sport) {

        MatchStrategy service = MatchSportServiceFactory.newMatchSportService(sport, context);
        return service.findAll();
    }

    /**
     * Cria uma nova instância de {@link Match} especializada em um esporte específico.
     *
     * @param sport Tipo de esporte na qual a instância de {@link Match} será especializada.
     * @return Uma nova instância de {@link Match} baseada no tipo de esporte fornecido.
     */
    public Match saveMatch(Match match, Sports sport) {

        MatchStrategy service = MatchSportServiceFactory.newMatchSportService(sport, context);
        return service.saveMatch(match);
    }

    /**
     * Encontra uma instância de {@link Match} com base no seu ID e tipo de esporte.
     *
     * @param id O ID único de {@link Match}.
     * @param sport Tipo de esporte associado à {@link Match}.
     * @return A instância correspondente de {@link Match}.
     * @throws com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException Se nenhum {@link Match}
     * correspondente ao ID for encontrado.
     * @throws BadRequestException Se o tipo de esporte fornecido for nulo.
     */
    public Match findMatch(Long id, Sports sport) {

        MatchStrategy service = MatchSportServiceFactory.newMatchSportService(sport, context);
        return service.findMatchById(id);
    }

    /**
     * Encontra uma instância de {@link Match} associada a um gol com base no ID e tipo de esporte.
     *
     * @param id Identificador único de {@link Match}.
     * @param sport Tipo de esporte associado à {@link Match}.
     * @return A instância correspondente de {@link Match}.
     * @throws com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException Se nenhum {@link Match}
     * correspondente ao ID for encontrado.
     * @throws BadRequestException Se o tipo de esporte não suportar a operação relacionada a gols.
     */
    public Match findMatchForGoal(Long id, Sports sport) {

        MatchStrategy service = MatchSportServiceFactory.newMatchSportService(sport, context);

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
     * @throws com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException Se nenhum {@link Match}
     * correspondente ao ID for encontrado.
     * @throws BadRequestException Se o tipo de esporte não suportar a operação relacionada a cartões de penalidade.
     */
    public Match findMatchForCard(Long id, Sports sport) {

        MatchStrategy service = MatchSportServiceFactory.newMatchSportService(sport, context);
        Match match;

        switch (sport) {
            case FUTSAL, HANDBALL, BASKETBALL -> match = service.findMatchById(id);
            default -> throw new BadRequestException(ExceptionMessages.UNSUPPORTED_FOR_PENALTY_CARDS.message);
        }
        return match;
    }

}
