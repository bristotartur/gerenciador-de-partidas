package com.bristotartur.gerenciadordepartidas.services.events;

import com.bristotartur.gerenciadordepartidas.domain.events.ChessMatch;
import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.ChessMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de xadrez ({@link ChessMatch}).
 * Esta classe implementa a estratégia {@link MatchStrategy} para fornecer comportamentos padronizados
 * relacionados à especialização de {@link ChessMatch}.
 *
 * @see ChessMatchRepository
 * @see MatchServiceFactory
 * @see MatchServiceMediator
 */
@Service
@RequiredArgsConstructor
public class ChessMatchService implements MatchStrategy<ChessMatch> {

    private final ChessMatchRepository chessMatchRepository;

    /**
     * Recupera uma lista paginada contendo todas as instâncias da {@link ChessMatch}.
     *
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Um {@link Page} contendo todas as instâncias de {@link ChessMatch};
     */
    @Override
    public Page<ChessMatch> findAll(Pageable pageable) {
        return chessMatchRepository.findAll(pageable);
    }

    /**
     * Busca uma partida de xadrez pelo seu ID.
     *
     * @param id Identificador único da partida de xadrez.
     * @return Uma instância de {@link ChessMatch} correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de xadrez correspondente ao ID for encontrada.
     */
    @Override
    public ChessMatch findMatchById(Long id) {

        return chessMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.CHESS_MATCH_NOT_FOUND.message));
    }

    /**
     * Cria uma nova instância de {@link Match} e a persiste no banco de dados.
     *
     * @return Uma nova instância de {@link ChessMatch} criada e salva no banco de dados.
     */
    @Override
    public ChessMatch saveMatch(Match match) {

        ChessMatch chessMatch = new ChessMatch();

        BeanUtils.copyProperties(match, chessMatch);
        return chessMatchRepository.save(chessMatch);
    }

}
