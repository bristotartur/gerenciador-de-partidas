package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;

/**
 * Estratégia para serviços relacionados a especializações de {@link Match}, fornecendo
 * uma abstração padronizada para gerenciar comportamentos comuns esperados destas
 * implementações, tornando seu uso mais prático e consistente.
 *
 * @see GeneralMatchSportService
 * @see MatchSportServiceFactory
 * @param <T> Especifica com qual especialização de {@link Match} a estratégia irá lidar.
 */
public interface MatchStrategy<T extends Match> {

    /**
     * Procura por um {@link Match} no banco de dados com base no ID fornecido.
     *
     * @param id Identificador único da entidade {@link Match} a ser buscada.
     * @return Uma instância concreta que herda de {@link Match}, correspondente ao ID fornecido.
     */
    Match findMatchById(Long id);

    /**
     * Gera uma nova instância de {@link Match} e a persiste no banco de dados.
     *
     * @return Uma instância concreta que herda de {@link Match}, persistida no banco de dados.
     */
    Match saveMatch(Match match);

}
