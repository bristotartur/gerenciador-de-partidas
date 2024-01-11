package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Esta classe atua como uma camada de serviço centralizada para operações relacionadas a diferentes
 * tipos de partidas esportivas (MatchSport). Através desta classe é possível criar novas instâncias
 * de MatchSport, encontrar detalhes específicos de uma partida, como partidas relacionadas a gols ou cartões,
 * e manipular operações gerais associadas a serviços de MatchSport. <br> <br>
 *
 * Ela utiliza uma fábrica, MatchSportServiceFactory, para criar dinamicamente serviços especializados com
 * base no tipo de esporte fornecido.
 *
 * @see MatchSportServiceFactory
 * @see MatchSportStrategy
 */
@Service
@RequiredArgsConstructor
public class GeneralMatchSportService {

    private final ApplicationContext context;

    /**
     * Cria uma nova instância de MatchSport especializada em um esporte específico.
     *
     * @param sport Tipo de esporte na qual a instância de MatchSport será especializada.
     * @return Uma nova instância de MatchSport baseada no tipo de esporte fornecido.
     */
    public MatchSport newMatchSport(Sports sport) {
        
        MatchSportStrategy service = MatchSportServiceFactory.newMatchSportService(sport, context);
        return service.createNewMatchSport();
    }

    /**
     * Encontra uma instância de MatchSport com base no seu ID e tipo de esporte.
     *
     * @param id O ID único de MatchSport.
     * @param sport Tipo de esporte associado à MatchSport.
     * @return A instância correspondente de MatchSport.
     * @throws com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException Se nenhum MatchSport
     * correspondente ao ID for encontrado.
     */
    public MatchSport findMatchSport(Long id, Sports sport) {

        MatchSportStrategy service = MatchSportServiceFactory.newMatchSportService(sport, context);
        return service.findMatchSportById(id);
    }

    /**
     * Encontra uma instância de MatchSport associada a um gol com base no ID e tipo de esporte.
     *
     * @param id O identificador único de MatchSport.
     * @param sport Tipo de esporte associado à MatchSport.
     * @return A instância correspondente de MatchSport.
     * @throws com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException Se nenhum MatchSport
     * correspondente ao ID for encontrado.
     * @throws BadRequestException Se o tipo de esporte não suportar a operação relacionada a gols.
     */
    public MatchSport findMatchSportForGoal(Long id, Sports sport) {

        MatchSportStrategy service = MatchSportServiceFactory.newMatchSportService(sport, context);

        if (sport.equals(Sports.FOOTBALL) || sport.equals(Sports.HANDBALL))
            return service.findMatchSportById(id);

        throw new BadRequestException(ExceptionMessages.UNSUPPORTED_FOR_GOALS.message);
    }

    /**
     * Encontra uma instância de MatchSport associada a um cartão de penalidade com base
     * no ID e tipo de esporte.
     *
     * @param id O identificador único de MatchSport.
     * @param sport Tipo de esporte associado à MatchSport.
     * @return A instância correspondente de MatchSport.
     * @throws com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException Se nenhum MatchSport
     * correspondente ao ID for encontrado.
     * @throws BadRequestException Se o tipo de esporte não suportar a operação relacionada a cartões de penalidade.
     */
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
