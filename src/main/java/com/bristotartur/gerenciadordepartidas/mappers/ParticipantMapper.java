package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingParticipantDto;
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
    @Mapping(target = "goal", ignore = true)
    @Mapping(target = "penaltyCards", ignore = true)
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
    @Mapping(target = "goal", ignore = true)
    @Mapping(target = "penaltyCards", ignore = true)
    Participant toExistingParticipant(Long id, ParticipantDto participantDto, Team team);

    /**
     * Gera uma nova instância de {@link ExposingParticipantDto} a partir de qualquer instância de {@link Participant}.
     *
     * @param participant Participante contendo os dados a serem mapeados.
     * @return Uma nova instãncia de {@link ExposingParticipantDto}.
     */
    @Mapping(target = "team", source = "team.name")
    ExposingParticipantDto toNewExposingParticipantDto(Participant participant);

}
