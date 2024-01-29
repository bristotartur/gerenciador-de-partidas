package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.actions.Goal;
import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingGoalDto;
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
    @Mapping(target = "player", source = "player")
    @Mapping(target = "team", source = "player.team")
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
    @Mapping(target = "player", source = "player")
    @Mapping(target = "team", source = "player.team")
    Goal toExistingGoal(Long id, GoalDto goalDto, Participant player, Match match);

    /**
     * Gera um nova instância de {@link ExposingGoalDto} a partir de qualquer instância de {@link Goal}.
     *
     * @param goal Gol contendo os dados a serem mapeados.
     * @return Uma nova instância de {@link ExposingGoalDto}.
     */
    @Mapping(target = "goalId", source = "goal.id")
    @Mapping(target = "player", source = "player.name")
    @Mapping(target = "team", source = "player.team.name")
    ExposingGoalDto toNewExposingGoalDto(Goal goal);

}