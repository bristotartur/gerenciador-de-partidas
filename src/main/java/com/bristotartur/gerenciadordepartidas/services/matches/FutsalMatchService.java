package com.bristotartur.gerenciadordepartidas.services.matches;

import com.bristotartur.gerenciadordepartidas.domain.matches.FutsalMatch;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.FutsalMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de futsal ({@link FutsalMatch}).
 * Esta classe implementa a estratégia {@link MatchStrategy} para fornecer comportamentos padronizados
 * relacionados à especialização de {@link FutsalMatch}.
 *
 * @see FutsalMatchRepository
 * @see MatchServiceFactory
 * @see MatchServiceMediator
 */
@Service
@RequiredArgsConstructor
public class FutsalMatchService implements MatchStrategy<FutsalMatch> {

    private final FutsalMatchRepository futsalMatchRepository;

    /**
     * Recupera uma lista paginada contendo todas as instâncias da {@link FutsalMatch}.
     *
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Um {@link Page} contendo todas as instâncias de {@link FutsalMatch};
     */
    @Override
    public Page<FutsalMatch> findAll(Pageable pageable) {
        return futsalMatchRepository.findAll(pageable);
    }

    /**
     * Busca uma partida de futsal pelo seu ID.
     *
     * @param id Identificador único da partida de futsal.
     * @return Uma instância de {@link Match} correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de futsal correspondente ao ID for encontrada.
     */
    @Override
    public FutsalMatch findMatchById(Long id) {

        return futsalMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.FUTSAL_MATCH_NOT_FOUND.message));
    }

    /**
     * Cria uma nova instância de {@link FutsalMatch} e a persiste no banco de dados.
     *
     * @return Uma nova instância de {@link FutsalMatch} criada e salva no banco de dados.
     */
    @Override
    public FutsalMatch saveMatch(Match match) {

        FutsalMatch futsalMatch = new FutsalMatch();

        BeanUtils.copyProperties(match, futsalMatch);
        return futsalMatchRepository.save(futsalMatch);
    }

}
