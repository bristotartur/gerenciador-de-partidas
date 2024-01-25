package com.bristotartur.gerenciadordepartidas.services.events;

import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.domain.events.TableTennisMatch;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.TableTennisMatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável por gerenciar operações relacionadas a partidas de tênis de mesa ({@link TableTennisMatch}).
 * Esta classe implementa a estratégia {@link MatchStrategy} para fornecer comportamentos padronizados
 * relacionados à especialização de {@link TableTennisMatch}.
 *
 * @see TableTennisMatchRepository
 * @see MatchServiceFactory
 * @see MatchServiceMediator
 */
@Service
@AllArgsConstructor
public class TableTennisMatchService implements MatchStrategy<TableTennisMatch> {

    private final TableTennisMatchRepository tableTennisMatchRepository;

    /**
     * Recupera uma lista contendo todas as instâncias da {@link TableTennisMatch}
     *
     * @return Uma lista contendo todas as instâncias de {@link TableTennisMatch};
     */
    @Override
    public List<TableTennisMatch> findAll() {
        return tableTennisMatchRepository.findAll();
    }

    /**
     * Busca uma partida de tênis de mesa pelo seu ID.
     *
     * @param id Identificador único da partida de tênis de mesa.
     * @return Uma instância de {@link TableTennisMatch} correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma partida de tênis de mesa correspondente ao ID for encontrada.
     */
    @Override
    public TableTennisMatch findMatchById(Long id) {

        return tableTennisMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.TABLE_TENNIS_MATCH_NOT_FOUND.message));

    }

    /**
     * Cria uma nova instância de {@link TableTennisMatch} e a persiste no banco de dados.
     *
     * @return Uma nova instância de {@link TableTennisMatch} criada e salva no banco de dados.
     */
    @Override
    public TableTennisMatch saveMatch(Match match) {

        TableTennisMatch tableTennisMatch = new TableTennisMatch();

        BeanUtils.copyProperties(match, tableTennisMatch);
        return tableTennisMatchRepository.save(tableTennisMatch);
    }

}