package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.structure.FutsalMatch;
import com.bristotartur.gerenciadordepartidas.domain.structure.Match;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.FutsalMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de futsal ({@link FutsalMatch}).
 * Esta classe implementa a estratégia {@link MatchStrategy} para fornecer comportamentos padronizados
 * relacionados à especialização de {@link FutsalMatch}.
 *
 * @see FutsalMatchRepository
 * @see MatchSportServiceFactory
 * @see GeneralMatchSportService
 */
@Service
@RequiredArgsConstructor
public class FutsalMatchService implements MatchStrategy<FutsalMatch> {

    private final FutsalMatchRepository futsalMatchRepository;

    /**
     * Recupera uma lista contendo todas as instâncias da {@link FutsalMatch}
     *
     * @return Uma lista contendo todas as instâncias de {@link FutsalMatch};
     */
    @Override
    public List<FutsalMatch> findAll() {
        return futsalMatchRepository.findAll();
    }

    /**
     * Busca uma partida de futsal pelo seu ID.
     *
     * @param id Identificador único da partida de futsal.
     * @return Uma instância de {@link Match} correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de futsal correspondente ao ID for encontrada.
     */
    @Override
    public Match findMatchById(Long id) {

        return futsalMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.FUTSAL_MATCH_NOT_FOUND.message));
    }

    /**
     * Cria uma nova instância de {@link Match} e a persiste no banco de dados.
     *
     * @return Uma nova instância de {@link Match} criada e salva no banco de dados.
     */
    @Override
    public Match saveMatch(Match match) {

        FutsalMatch futsalMatch = new FutsalMatch();

        BeanUtils.copyProperties(match, futsalMatch);
        return futsalMatchRepository.save(futsalMatch);
    }

}
