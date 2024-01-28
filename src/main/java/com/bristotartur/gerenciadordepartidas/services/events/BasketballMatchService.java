package com.bristotartur.gerenciadordepartidas.services.events;

import com.bristotartur.gerenciadordepartidas.domain.events.BasketballMatch;
import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.BasketballMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de basquete ({@link BasketballMatch}).
 * Esta classe implementa a estratégia {@link MatchStrategy} para fornecer comportamentos padronizados
 * relacionados à especialização de {@link BasketballMatch}.
 *
 * @see BasketballMatchRepository
 * @see MatchServiceFactory
 * @see MatchServiceMediator
 */
@Service
@RequiredArgsConstructor
public class BasketballMatchService implements MatchStrategy<BasketballMatch> {

    private final BasketballMatchRepository basketballMatchRepository;

    /**
     * Recupera uma lista paginada contendo todas as instâncias da {@link BasketballMatch}.
     *
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Um {@link Page} contendo todas as instâncias de {@link BasketballMatch};
     */
    @Override
    public Page<BasketballMatch> findAll(Pageable pageable) {
        return basketballMatchRepository.findAll(pageable);
    }

    /**
     * Busca uma partida de basquete pelo seu ID.
     *
     * @param id Identificador único da partida de basquete.
     * @return Uma instância de {@link BasketballMatch} correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de basquete correspondente ao ID for encontrada.
     */
    @Override
    public BasketballMatch findMatchById(Long id) {

        return basketballMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.BASKETBALL_MATCH_NOT_FOUND.message));
    }

    /**
     * Cria uma nova instância de {@link BasketballMatch} e a persiste no banco de dados.
     *
     * @return Uma nova instância de {@link BasketballMatch} criada e salva no banco de dados.
     */
    @Override
    public BasketballMatch saveMatch(Match match) {

        BasketballMatch basketballMatch = new BasketballMatch();

        BeanUtils.copyProperties(match, basketballMatch);
        return basketballMatchRepository.save(basketballMatch);
    }

}
