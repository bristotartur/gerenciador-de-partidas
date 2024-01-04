package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.VolleyballMatch;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.VolleyballMatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de vôlei (VolleyballMatch).
 * Esta classe implementa a estratégia MatchSportStrategy para fornecer comportamentos padronizados
 * relacionados à especialização de VolleyballMatch.
 */
@Service
@AllArgsConstructor
public class VolleyballMatchService implements MatchSportStrategy<VolleyballMatch> {

    private final VolleyballMatchRepository volleyballMatchRepository;

    /**
     * Busca uma partida de vôlei pelo seu ID.
     *
     * @param id Identificador único da partida de vôlei.
     * @return Uma instância de VolleyballMatch correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de vôlei correspondente ao ID for encontrada.
     */
    @Override
    public VolleyballMatch findMatchSportById(Long id) {

        return volleyballMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.VOLLEYBALL_MATCH_NOT_FOUND.message));
    }

    /**
     * Cria uma nova instância de VolleyballMatch e a persiste no banco de dados.
     *
     * @return Uma nova instância de VolleyballMatch criada e salva no banco de dados.
     */
    @Override
    public VolleyballMatch createNewMatchSport() {
        return volleyballMatchRepository.save(new VolleyballMatch());
    }
}
