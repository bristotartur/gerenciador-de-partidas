package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.participant.Participant;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.ParticipantDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interface responsável por gerar o mapeamento de dados relativos a entidade {@link Participant}
 * para uma instância concreta da mesma.
 *
 * @see ParticipantDto
 */
@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    /**
     * Gera uma nova instância de {@link Participant} com base nos dados fornecidos.
     *
     * @param participantDto DTO do tipo {@link ParticipantDto} contendo os dados do novo participante.
     * @param team A instância de {@link Team} associada ao participante.
     * @return Uma nova instância do tipo {@link Participant} com base nos dados fornecidos.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "participantDto.name")
    @Mapping(target = "matches", ignore = true)
    Participant toNewParticipant(ParticipantDto participantDto, Team team);

    /**
     * Atualiza uma instância existente de {@link Participant} com base nos dados fornecidos.
     *
     * @param id Identificador único do participante que será atualizado.
     * @param participantDto DTO do tipo {@link ParticipantDto} contendo os dados do novo participante.
     * @param team team A instância de {@link Team} associada ao participante.
     * @return Uma nova instância atualizada de {@link Participant} com base nos dados fornecidos.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "participantDto.name")
    @Mapping(target = "matches", ignore = true)
    Participant toExistingParticipant(Long id, ParticipantDto participantDto, Team team);

}
