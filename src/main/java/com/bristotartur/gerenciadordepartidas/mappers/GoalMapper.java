package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.actions.Goal;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestGoalDto;
import com.bristotartur.gerenciadordepartidas.dtos.response.ResponseGoalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interface responsável por gerar o mapeamento de dados relativos a entidade {@link Goal}
 * para uma instância concreta da mesma.
 *
 * @see RequestGoalDto
 */
@Mapper(componentModel = "spring")
public interface GoalMapper {

    /**
     * Gera uma nova instância de {@link Goal} com base nos dados fornecidos.
     *
     * @param dto DTO do tipo {@link RequestGoalDto} contendo os dados e metadados do novo gol.
     * @param player Jogador do tipo {@link Participant} associado ao gol.
     * @param match A instância de {@link Match} associada ao gol.
     * @return Uma nova instância de {@link Goal} com base nos dados fornecidos.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "player", source = "player")
    @Mapping(target = "team", source = "player.team")
    Goal toNewGoal(RequestGoalDto dto, Participant player, Match match);

    /**
     * Atualiza uma instância existente de {@link Goal} com base nos dados fornecidos.
     *
     * @param id Identificador único do gol que será atualizado.
     * @param dto DTO do tipo {@link RequestGoalDto} contendo os dados do gol que será atualizado.
     * @param player Jogador do tipo {@link Participant} associado ao gol.
     * @param match Instância de {@link Match} associada ao gol.
     * @return Uma nova instância atualizada de {@link Goal} com base nos dados fornecidos.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "player", source = "player")
    @Mapping(target = "team", source = "player.team")
    Goal toExistingGoal(Long id, RequestGoalDto dto, Participant player, Match match);

    /**
     * Gera um nova instância de {@link ResponseGoalDto} a partir de qualquer instância de {@link Goal}.
     *
     * @param goal Gol contendo os dados a serem mapeados.
     * @return Uma nova instância de {@link ResponseGoalDto}.
     */
    @Mapping(target = "goalId", source = "goal.id")
    @Mapping(target = "player", source = "player.name")
    @Mapping(target = "team", source = "player.team")
    ResponseGoalDto toNewExposingGoalDto(Goal goal);

}