package com.bristotartur.gerenciadordepartidas.services.events;

import com.bristotartur.gerenciadordepartidas.domain.events.Match;

import java.util.List;

/**
 * Estratégia para serviços relacionados a especializações de {@link Match}, fornecendo
 * uma abstração padronizada para gerenciar comportamentos comuns esperados destas
 * implementações, tornando seu uso mais prático e consistente.
 *
 * @see MatchServiceMediator
 * @see MatchServiceFactory
 * @param <T> Especifica com qual especialização de {@link Match} a estratégia irá lidar.
 */
public interface MatchStrategy<T extends Match> {

    /**
     * Recupera uma lista contendo todas as instâncias da especialização de {@link Match}
     * associada a esta estratégia.
     *
     * @return Uma lista contendo todas as instâncias de {@link Match} específicas desta estratégia.
     */
    List<T> findAll();

    /**
     * Procura por uma instância de uma determinada especialização de {@link Match} no banco de dados
     * com base no ID fornecido.
     *
     * @param id Identificador único da entidade {@link Match} a ser buscada.
     * @return Uma instância concreta que herda de {@link Match}, correspondente ao ID fornecido.
     */
    T findMatchById(Long id);

    /**
     * Persiste uma nova instância de uma entidade especializada flha de {@link Match}.
     *
     * @return Uma instância concreta que herda de {@link Match}, persistida no banco de dados.
     */
    T saveMatch(Match match);

}
