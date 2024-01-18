package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.ChessMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.ChessMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de xadrez ({@link ChessMatch}).
 * Esta classe implementa a estratégia {@link MatchStrategy} para fornecer comportamentos padronizados
 * relacionados à especialização de {@link ChessMatch}.
 *
 * @see ChessMatchRepository
 * @see MatchSportServiceFactory
 * @see GeneralMatchSportService
 */
@Service
@RequiredArgsConstructor
public class ChessMatchService implements MatchStrategy<ChessMatch> {

    private final ChessMatchRepository chessMatchRepository;

    /**
     * Recupera uma lista contendo todas as instâncias da {@link ChessMatch}
     *
     * @return Uma lista contendo todas as instâncias de {@link ChessMatch};
     */
    @Override
    public List<ChessMatch> findAll() {
        return chessMatchRepository.findAll();
    }

    /**
     * Busca uma partida de xadrez pelo seu ID.
     *
     * @param id Identificador único da partida de xadrez.
     * @return Uma instância de {@link Match} correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de xadrez correspondente ao ID for encontrada.
     */
    @Override
    public Match findMatchById(Long id) {

        return chessMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.CHESS_MATCH_NOT_FOUND.message));
    }

    /**
     * Cria uma nova instância de {@link Match} e a persiste no banco de dados.
     *
     * @return Uma nova instância de {@link Match} criada e salva no banco de dados.
     */
    @Override
    public Match saveMatch(Match match) {

        ChessMatch chessMatch = new ChessMatch();

        BeanUtils.copyProperties(match, chessMatch);
        return chessMatchRepository.save(chessMatch);
    }

}
