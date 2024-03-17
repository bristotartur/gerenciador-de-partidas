package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestParticipantDto;
import com.bristotartur.gerenciadordepartidas.dtos.response.ResponseParticipantDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interface responsável por gerar o mapeamento de dados relativos a entidade {@link Participant}
 * para uma instância concreta da mesma.
 *
 * @see RequestParticipantDto
 */
@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    /**
     * Gera uma nova instância de {@link Participant} com base nos dados fornecidos.
     *
     * @param dto DTO do tipo {@link RequestParticipantDto} contendo os dados do novo participante.
     * @return Uma nova instância do tipo {@link Participant} com base nos dados fornecidos.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "edition", source = "edition")
    @Mapping(target = "matches", ignore = true)
    @Mapping(target = "goal", ignore = true)
    @Mapping(target = "penaltyCards", ignore = true)
    Participant toNewParticipant(RequestParticipantDto dto, Edition edition);

    /**
     * Atualiza uma instância existente de {@link Participant} com base nos dados fornecidos.
     *
     * @param id Identificador único do participante que será atualizado.
     * @param dto DTO do tipo {@link RequestParticipantDto} contendo os dados do novo participante.
     * @return Uma nova instância atualizada de {@link Participant} com base nos dados fornecidos.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "edition", source = "edition")
    @Mapping(target = "matches", ignore = true)
    @Mapping(target = "goal", ignore = true)
    @Mapping(target = "penaltyCards", ignore = true)
    Participant toExistingParticipant(Long id, RequestParticipantDto dto, Edition edition);

    /**
     * Gera uma nova instância de {@link ResponseParticipantDto} a partir de qualquer instância de {@link Participant}.
     *
     * @param participant Participante contendo os dados a serem mapeados.
     * @return Uma nova instãncia de {@link ResponseParticipantDto}.
     */
    @Mapping(target = "participantId", source = "participant.id")
    ResponseParticipantDto toNewExposingParticipantDto(Participant participant);

}
