package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingTeamDto;
import com.bristotartur.gerenciadordepartidas.dtos.TeamDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interface responsável por gerar o mapeamento de dados relativos a entidade {@link Team}
 * para uma instância concreta da mesma.
 *
 * @see TeamDto
 */
@Mapper(componentModel = "spring")
public interface TeamMapper {

    /**
     * Realiza o mapeamento de um um objeto do tipo {@link TeamDto} para um novo objeto do tipo {@link Team}.
     * Os valores do campo points deste novo objeto {@link Team} será sempre 0, mesmo que o DTO possua
     * algum valor em seu campo correspondente.
     *
     * @param teamDto DTO do tipo {@link TeamDto} contendo os dados da nova equipe.
     * @return Uma nova instância do tipo {@link Team} com base nos dados fornecidos.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "teamDto.teamName.value")
    @Mapping(target = "points", constant = "0")
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "matchesAsTeamA", ignore = true)
    @Mapping(target = "matchesAsTeamB", ignore = true)
    Team toNewTeam(TeamDto teamDto);

    /**
     * Realiza o mapeamento de um objeto do tipo {@link TeamDto} para um objeto existente do tipo {@link Team},
     * atualizando os valores de seus respectivos campos aos correspondentes do DTO.
     *
     * @param id Identificador único da equipe que será atualizada.
     * @param teamDto DTO do tipo {@link TeamDto} contendo os dados da equipe que será atualizada.
     * @return Uma nova instância do tipo {@link Team} com seus valores atualizados.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "teamDto.teamName.value")
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "matchesAsTeamA", ignore = true)
    @Mapping(target = "matchesAsTeamB", ignore = true)
    Team toExistingTeam(Long id, TeamDto teamDto);

    /**
     * Gera um nova instância de {@link ExposingTeamDto} a partir de qualquer instância de {@link Team}.
     *
     * @param team Equipe contenco os dados a serem mapeados.
     * @return Uma nova instância de {@link Team}.
     */
    @Mapping(target = "teamName", source = "team.name")
    ExposingTeamDto toNewExposingTeamDto(Team team);

}
