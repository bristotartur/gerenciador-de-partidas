package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.structure.BasketballMatch;
import com.bristotartur.gerenciadordepartidas.domain.structure.Match;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.BasketballMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de basquete ({@link BasketballMatch}).
 * Esta classe implementa a estratégia {@link MatchStrategy} para fornecer comportamentos padronizados
 * relacionados à especialização de {@link BasketballMatch}.
 *
 * @see BasketballMatchRepository
 * @see MatchSportServiceFactory
 * @see GeneralMatchSportService
 */
@Service
@RequiredArgsConstructor
public class BasketballMatchService implements MatchStrategy<BasketballMatch> {

    private final BasketballMatchRepository basketballMatchRepository;

    /**
     * Recupera uma lista contendo todas as instâncias da {@link BasketballMatch}
     *
     * @return Uma lista contendo todas as instâncias de {@link BasketballMatch};
     */
    @Override
    public List<BasketballMatch> findAll() {
        return basketballMatchRepository.findAll();
    }

    /**
     * Busca uma partida de basquete pelo seu ID.
     *
     * @param id Identificador único da partida de basquete.
     * @return Uma instância de {@link Match} correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de basquete correspondente ao ID for encontrada.
     */
    @Override
    public Match findMatchById(Long id) {

        return basketballMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.BASKETBALL_MATCH_NOT_FOUND.message));
    }

    /**
     * Cria uma nova instância de {@link Match} e a persiste no banco de dados.
     *
     * @return Uma nova instância de {@link Match} criada e salva no banco de dados.
     */
    @Override
    public Match saveMatch(Match match) {

        BasketballMatch basketballMatch = new BasketballMatch();

        BeanUtils.copyProperties(match, basketballMatch);
        return basketballMatchRepository.save(basketballMatch);
    }

}
