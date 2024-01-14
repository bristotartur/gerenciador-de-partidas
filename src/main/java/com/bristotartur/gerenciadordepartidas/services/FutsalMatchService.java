package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.FutsalMatch;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.FutsalMatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de futsal ({@link FutsalMatch}).
 * Esta classe implementa a estratégia {@link MatchSportStrategy} para fornecer comportamentos padronizados
 * relacionados à especialização de {@link FutsalMatch}.
 *
 * @see FutsalMatchRepository
 * @see MatchSportServiceFactory
 * @see GeneralMatchSportService
 */
@Service
@AllArgsConstructor
public class FutsalMatchService implements MatchSportStrategy<FutsalMatch> {

    private final FutsalMatchRepository futsalMatchRepository;

    /**
     * Busca uma partida de futsal pelo seu ID.
     *
     * @param id Identificador único da partida de futsal.
     * @return Uma instância de {@link FutsalMatch} correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de futsal correspondente ao ID for encontrada.
     */
    @Override
    public FutsalMatch findMatchSportById(Long id) {

        return futsalMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.FUTSAL_MATCH_NOT_FOUND.message));
    }

    /**
     * Cria uma nova instância de {@link FutsalMatch} e a persiste no banco de dados.
     *
     * @return Uma nova instância de {@link FutsalMatch} criada e salva no banco de dados.
     */
    @Override
    public FutsalMatch createNewMatchSport() {
        return futsalMatchRepository.save(new FutsalMatch());
    }

}
