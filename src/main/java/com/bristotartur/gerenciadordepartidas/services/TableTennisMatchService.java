package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.TableTennisMatch;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.TableTennisMatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de tênis de mesa ({@link TableTennisMatch}).
 * Esta classe implementa a estratégia {@link MatchSportStrategy} para fornecer comportamentos padronizados
 * relacionados à especialização de {@link TableTennisMatch}.
 *
 * @see TableTennisMatchRepository
 * @see MatchSportServiceFactory
 * @see GeneralMatchSportService
 */
@Service
@AllArgsConstructor
public class TableTennisMatchService implements MatchSportStrategy<TableTennisMatch> {

    private final TableTennisMatchRepository tableTennisMatchRepository;

    /**
     * Busca uma partida de tênis de mesa pelo seu ID.
     *
     * @param id Identificador único da partida de tênis de mesa.
     * @return Uma instância de {@link TableTennisMatch} correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de tênis de mesa correspondente ao ID for encontrada.
     */
    @Override
    public TableTennisMatch findMatchSportById(Long id) {

        return tableTennisMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.TABLE_TENNIS_MATCH_NOT_FOUND.message));

    }

    /**
     * Cria uma nova instância de {@link TableTennisMatch} e a persiste no banco de dados.
     *
     * @return Uma nova instância de {@link TableTennisMatch} criada e salva no banco de dados.
     */
    @Override
    public TableTennisMatch createNewMatchSport() {
        return tableTennisMatchRepository.save(new TableTennisMatch());
    }

}
