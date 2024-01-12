package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.ChessMatch;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.ChessMatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de xadrez ({@link ChessMatch}).
 * Esta classe implementa a estratégia {@link MatchSportStrategy} para fornecer comportamentos padronizados
 * relacionados à especialização de {@link ChessMatch}.
 *
 * @see ChessMatchRepository
 * @see MatchSportServiceFactory
 * @see GeneralMatchSportService
 */
@Service
@AllArgsConstructor
public class ChessMatchService implements MatchSportStrategy<ChessMatch> {

    private final ChessMatchRepository chessMatchRepository;

    /**
     * Busca uma partida de xadrez pelo seu ID.
     *
     * @param id Identificador único da partida de xadrez.
     * @return Uma instância de {@link ChessMatch} correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de xadrez correspondente ao ID for encontrada.
     */
    @Override
    public ChessMatch findMatchSportById(Long id) {

        return chessMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.CHESS_MATCH_NOT_FOUND.message));
    }

    /**
     * Cria uma nova instância de {@link ChessMatch} e a persiste no banco de dados.
     *
     * @return Uma nova instância de {@link ChessMatch} criada e salva no banco de dados.
     */
    @Override
    public ChessMatch createNewMatchSport() {
        return chessMatchRepository.save(new ChessMatch());
    }

}
