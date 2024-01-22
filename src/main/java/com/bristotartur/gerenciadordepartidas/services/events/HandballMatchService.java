package com.bristotartur.gerenciadordepartidas.services.events;

import com.bristotartur.gerenciadordepartidas.domain.events.HandballMatch;
import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.HandballMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de handebol ({@link HandballMatch}).
 * Esta classe implementa a estratégia {@link MatchStrategy} para fornecer comportamentos padronizados
 * relacionados à especialização de {@link HandballMatch}.
 *
 * @see HandballMatchRepository
 * @see MatchSportServiceFactory
 * @see GeneralMatchSportService
 */
@Service
@RequiredArgsConstructor
public class HandballMatchService implements MatchStrategy<HandballMatch> {

    private final HandballMatchRepository handballMatchRepository;

    /**
     * Recupera uma lista contendo todas as instâncias da {@link HandballMatch}
     *
     * @return Uma lista contendo todas as instâncias de {@link HandballMatch};
     */
    @Override
    public List<HandballMatch> findAll() {
        return handballMatchRepository.findAll();
    }

    /**
     * Busca uma partida de handebol pelo seu ID.
     *
     * @param id Identificador único da partida de handebol.
     * @return Uma instância de {@link Match} correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de handebol correspondente ao ID for encontrada.
     */
    @Override
    public HandballMatch findMatchById(Long id) {

        return handballMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.HANDBALL_MATCH_NOT_FOUND.message));
    }

    /**
     * Cria uma nova instância de {@link Match} e a persiste no banco de dados.
     *
     * @return Uma nova instância de {@link Match} criada e salva no banco de dados.
     */
    @Override
    public HandballMatch saveMatch(Match match) {

        HandballMatch handballMatch = new HandballMatch();

        BeanUtils.copyProperties(match, handballMatch);
        return handballMatchRepository.save(handballMatch);
    }

}
