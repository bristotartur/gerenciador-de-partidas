package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.HandballMatch;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.HandballMatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de handebol (HandballMatch).
 * Esta classe implementa a estratégia MatchSportStrategy para fornecer comportamentos padronizados
 * relacionados à especialização de HandballMatch.
 */
@Service
@AllArgsConstructor
public class HandballMatchService implements MatchSportStrategy<HandballMatch> {

    private final HandballMatchRepository handballMatchRepository;

    /**
     * Busca uma partida de handebol pelo seu ID.
     *
     * @param id Identificador único da partida de handebol.
     * @return Uma instância de HandballMatch correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de handebol correspondente ao ID for encontrada.
     */
    @Override
    public HandballMatch findMatchSportById(Long id) {

        return handballMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.HANDBALL_MATCH_NOT_FOUND.message));
    }

    /**
     * Cria uma nova instância de HandballMatch e a persiste no banco de dados.
     *
     * @return Uma nova instância de HandballMatch criada e salva no banco de dados.
     */
    @Override
    public HandballMatch createNewMatchSport() {
        return handballMatchRepository.save(new HandballMatch());
    }
}
