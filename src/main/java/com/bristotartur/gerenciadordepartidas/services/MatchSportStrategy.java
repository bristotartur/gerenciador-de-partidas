package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;

/**
 * Estratégia para serviços relacionados a especializações de MatchSport, fornecendo
 * uma abstração padronizada para gerenciar comportamentos comuns esperados destas
 * implementações, tornando seu uso mais prático e consistente.
 *
 * @see GeneralMatchSportService
 * @see MatchSportServiceFactory
 * @param <T> Especifica com qual especialização de MatchSport a estratégia irá lidar.
 */
public interface MatchSportStrategy<T extends MatchSport> {

    /**
     * Procura por um MatchSport no banco de dados com base no ID fornecido.
     * Se a entidade correspondente não for encontrada, uma NotFoundException
     * é lançada.
     *
     * @param id Identificador único da entidade MatchSport a ser buscada.
     * @return Uma instância concreta que herda de MatchSport, correspondente ao ID fornecido.
     */
    T findMatchSportById(Long id);

    /**
     * Gera uma nova instância de MatchSport e a persiste no banco de dados.
     *
     * @return Uma instância concreta que herda de MatchSport, persistida no banco de dados.
     */
    T createNewMatchSport();

}
