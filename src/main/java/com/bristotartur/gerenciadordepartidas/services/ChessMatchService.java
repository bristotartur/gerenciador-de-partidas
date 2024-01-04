package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.ChessMatch;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.ChessMatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChessMatchService implements MatchSportStrategy<ChessMatch> {

    private final ChessMatchRepository chessMatchRepository;

    @Override
    public ChessMatch findMatchSportById(Long id) {

        return chessMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.CHESS_MATCH_NOT_FOUND.message));
    }

    @Override
    public ChessMatch createNewMatchSport() {
        return chessMatchRepository.save(new ChessMatch());
    }
}
