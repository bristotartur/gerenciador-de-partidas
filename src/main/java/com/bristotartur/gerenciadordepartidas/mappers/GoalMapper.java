package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.domain.participant.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.GoalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interface responsável por gerar o mapeamento de dados relativos a entidade {@link Goal}
 * para uma instância concreta da mesma.
 *
 * @see GoalDto
 */
@Mapper(componentModel = "spring")
public interface GoalMapper {

    /**
     * Gera uma nova instância de {@link Goal} com base nos dados fornecidos.
     *
     * @param goalDto DTO do tipo {@link GoalDto} contendo os dados e metadados do novo gol.
     * @param player Jogador do tipo {@link Participant} associado ao gol.
     * @param match A instância de {@link Match} associada ao gol.
     * @return Uma nova instância de {@link Goal} com base nos dados fornecidos.
     */
    @Mapping(target = "id", ignore = true)
    Goal toNewGoal(GoalDto goalDto, Participant player, Match match);

    /**
     * Atualiza uma instância existente de {@link Goal} com base nos dados fornecidos.
     *
     * @param id Identificador único do gol que será atualizado.
     * @param goalDto DTO do tipo {@link GoalDto} contendo os dados do gol que será atualizado.
     * @param player Jogador do tipo {@link Participant} associado ao gol.
     * @param match Instância de {@link Match} associada ao gol.
     * @return Uma nova instância atualizada de {@link Goal} com base nos dados fornecidos.
     */
    @Mapping(target = "id", source = "id")
    Goal toExistingGoal(Long id, GoalDto goalDto, Participant player, Match match);

}