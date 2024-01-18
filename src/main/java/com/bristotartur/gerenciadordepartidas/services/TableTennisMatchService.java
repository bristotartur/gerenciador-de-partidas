package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.TableTennisMatch;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.MatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de tênis de mesa ({@link TableTennisMatch}).
 * Esta classe implementa a estratégia {@link MatchStrategy} para fornecer comportamentos padronizados
 * relacionados à especialização de {@link TableTennisMatch}.
 *
 * @see MatchRepository
 * @see MatchSportServiceFactory
 * @see GeneralMatchSportService
 */
@Service
@AllArgsConstructor
public class TableTennisMatchService implements MatchStrategy<TableTennisMatch> {

    private final MatchRepository<TableTennisMatch> matchRepository;

    /**
     * Busca uma partida de tênis de mesa pelo seu ID.
     *
     * @param id Identificador único da partida de tênis de mesa.
     * @return Uma instância de {@link Match} correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de tênis de mesa correspondente ao ID for encontrada.
     */
    @Override
    public Match findMatchById(Long id) {

        return matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.TEAM_NOT_FOUND.message));

    }

    /**
     * Cria uma nova instância de {@link Match} e a persiste no banco de dados.
     *
     * @return Uma nova instância de {@link Match} criada e salva no banco de dados.
     */
    @Override
    public Match saveMatch(Match match) {

        TableTennisMatch tableTennisMatch = new TableTennisMatch();

        BeanUtils.copyProperties(match, tableTennisMatch);
        return matchRepository.save(tableTennisMatch);
    }

}
