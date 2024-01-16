package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.HandballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.HandballMatchRepository;
import com.bristotartur.gerenciadordepartidas.repositories.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    private final MatchRepository matchRepository;

    /**
     * Busca uma partida de handebol pelo seu ID.
     *
     * @param id Identificador único da partida de handebol.
     * @return Uma instância de {@link Match} correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de handebol correspondente ao ID for encontrada.
     */
    @Override
    public Match findMatchById(Long id) {

        return matchRepository.findMatchByType(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.HANDBALL_MATCH_NOT_FOUND.message));
    }

    /**
     * Cria uma nova instância de {@link Match} e a persiste no banco de dados.
     *
     * @return Uma nova instância de {@link Match} criada e salva no banco de dados.
     */
    @Override
    public Match saveMatch(Match match) {
        return matchRepository.save(match);
    }
}
