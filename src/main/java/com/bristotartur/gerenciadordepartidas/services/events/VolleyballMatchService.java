package com.bristotartur.gerenciadordepartidas.services.events;

import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.domain.events.VolleyballMatch;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.VolleyballMatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de vôlei ({@link VolleyballMatch}).
 * Esta classe implementa a estratégia {@link MatchStrategy} para fornecer comportamentos padronizados
 * relacionados à especialização de {@link VolleyballMatch}.
 *
 * @see VolleyballMatchRepository
 * @see MatchServiceFactory
 * @see MatchServiceMediator
 */
@Service
@AllArgsConstructor
public class VolleyballMatchService implements MatchStrategy<VolleyballMatch> {

    private final VolleyballMatchRepository volleyballMatchRepository;

    /**
     * Recupera uma lista paginada contendo todas as instâncias da {@link VolleyballMatch}.
     *
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Um {@link Page} contendo todas as instâncias de {@link VolleyballMatch};
     */
    @Override
    public Page<VolleyballMatch> findAll(Pageable pageable) {
        return volleyballMatchRepository.findAll(pageable);
    }

    /**
     * Busca uma partida de vôlei pelo seu ID.
     *
     * @param id Identificador único da partida de vôlei.
     * @return Uma instância de {@link VolleyballMatch} correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de vôlei correspondente ao ID for encontrada.
     */
    @Override
    public VolleyballMatch findMatchById(Long id) {

        return volleyballMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.VOLLEYBALL_MATCH_NOT_FOUND.message));
    }

    /**
     * Cria uma nova instância de {@link VolleyballMatch} e a persiste no banco de dados.
     *
     * @return Uma nova instância de {@link VolleyballMatch} criada e salva no banco de dados.
     */
    @Override
    public VolleyballMatch saveMatch(Match match) {

        VolleyballMatch volleyballMatch = new VolleyballMatch();

        BeanUtils.copyProperties(match, volleyballMatch);
        return volleyballMatchRepository.save(volleyballMatch);
    }

}
