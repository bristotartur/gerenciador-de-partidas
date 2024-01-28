package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.actions.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingPenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.dtos.PenaltyCardDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interface responsável por gerar o mapeamento de dados relativos a entidade {@link PenaltyCard}
 * para uma instância concreta da mesma.
 *
 * @see PenaltyCardDto
 */
@Mapper(componentModel = "spring")
public interface PenaltyCardMapper {

    /**
     * Gera uma nova instância de {@link PenaltyCard} com base nos dados fornecidos.
     *
     * @param penaltyCardDto DTO do tipo {@link PenaltyCardDto} contendo os dados e metadados do novo cartão.
     * @param player Jogador do tipo {@link Participant} associado ao cartão.
     * @param match A instância de {@link Match} associada ao cartão.
     * @return Uma nova instância de {@link PenaltyCard} com base nos dados fornecidos.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "color", source = "penaltyCardDto.color.name")
    PenaltyCard toNewPenaltyCard(PenaltyCardDto penaltyCardDto, Participant player, Match match);

    /**
     * Atualiza uma instância existente de {@link PenaltyCard} com base nos dados fornecidos.
     *
     * @param id Identificador único do cartão que será atualizado.
     * @param penaltyCardDto DTO do tipo {@link PenaltyCardDto} contendo os dados do cartão que será atualizado.
     * @param player Jogador do tipo {@link Participant} associado ao cartão.
     * @param match Instância de {@link Match} associada ao cartão.
     * @return Uma nova instância atualizada de {@link PenaltyCard} com base nos dados fornecidos.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "color", source = "penaltyCardDto.color.name")
    PenaltyCard toExistingPenaltyCard(Long id, PenaltyCardDto penaltyCardDto, Participant player, Match match);

    /**
     * Gera uma nova instância de {@link ExposingPenaltyCardDto} a partir de qualquer instância de {@link PenaltyCard}.
     *
     * @param penaltyCard Cartão de penalidade contendo os dados a serem mapeados.
     * @return Uma nova instância de {@link PenaltyCard}.
     */
    @Mapping(target = "player", source = "player.name")
    ExposingPenaltyCardDto toNewExposinfPenaltyCardDto(PenaltyCard penaltyCard);

}
