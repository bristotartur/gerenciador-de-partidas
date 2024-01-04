package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.BasketballMatch;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.BasketballMatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de basquete (BasketballMatch).
 * Esta classe implementa a estratégia MatchSportStrategy para fornecer comportamentos padronizados
 * relacionados à especialização de BasketballMatch.
 */
@Service
@AllArgsConstructor
public class BasketballMatchService implements MatchSportStrategy<BasketballMatch> {

    private final BasketballMatchRepository basketballMatchRepository;

    /**
     * Busca uma partida de basquete pelo seu ID.
     *
     * @param id Identificador único da partida de basquete.
     * @return Uma instância de BasketballMatch correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de basquete correspondente ao ID for encontrada.
     */
    @Override
    public BasketballMatch findMatchSportById(Long id) {

        return basketballMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.BASKETBALL_MATCH_NOT_FOUND.message));
    }

    /**
     * Cria uma nova instância de BasketballMatch e a persiste no banco de dados.
     *
     * @return Uma nova instância de BasketballMatch criada e salva no banco de dados.
     */
    @Override
    public BasketballMatch createNewMatchSport() {
        return basketballMatchRepository.save(new BasketballMatch());
    }
}
