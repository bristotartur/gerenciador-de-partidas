package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.actions.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestPenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.dtos.response.ResponsePenaltyCardDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interface responsável por gerar o mapeamento de dados relativos a entidade {@link PenaltyCard}
 * para uma instância concreta da mesma.
 *
 * @see RequestPenaltyCardDto
 */
@Mapper(componentModel = "spring")
public interface PenaltyCardMapper {

    /**
     * Gera uma nova instância de {@link PenaltyCard} com base nos dados fornecidos.
     *
     * @param dto DTO do tipo {@link RequestPenaltyCardDto} contendo os dados e metadados do novo cartão.
     * @param player Jogador do tipo {@link Participant} associado ao cartão.
     * @param match A instância de {@link Match} associada ao cartão.
     * @return Uma nova instância de {@link PenaltyCard} com base nos dados fornecidos.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "color", source = "dto.color")
    @Mapping(target = "player", source = "player")
    @Mapping(target = "team", source = "player.team")
    PenaltyCard toNewPenaltyCard(RequestPenaltyCardDto dto, Participant player, Match match);

    /**
     * Atualiza uma instância existente de {@link PenaltyCard} com base nos dados fornecidos.
     *
     * @param id Identificador único do cartão que será atualizado.
     * @param dto DTO do tipo {@link RequestPenaltyCardDto} contendo os dados do cartão que será atualizado.
     * @param player Jogador do tipo {@link Participant} associado ao cartão.
     * @param match Instância de {@link Match} associada ao cartão.
     * @return Uma nova instância atualizada de {@link PenaltyCard} com base nos dados fornecidos.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "color", source = "dto.color")
    @Mapping(target = "player", source = "player")
    @Mapping(target = "team", source = "player.team")
    PenaltyCard toExistingPenaltyCard(Long id, RequestPenaltyCardDto dto, Participant player, Match match);

    /**
     * Gera uma nova instância de {@link ResponsePenaltyCardDto} a partir de qualquer instância de {@link PenaltyCard}.
     *
     * @param penaltyCard Cartão de penalidade contendo os dados a serem mapeados.
     * @return Uma nova instância de {@link PenaltyCard}.
     */
    @Mapping(target = "penaltyCardId", source = "penaltyCard.id")
    @Mapping(target = "player", source = "player.name")
    @Mapping(target = "team", source = "player.team")
    ResponsePenaltyCardDto toNewExposinfPenaltyCardDto(PenaltyCard penaltyCard);

}
