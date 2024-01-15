package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
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
     * @param goalDto DTO do tipo {@link PenaltyCardDto} contendo os dados e metadados do novo cartão.
     * @param player Jogador do tipo {@link Participant} associado ao cartão.
     * @param matchSport A instância de {@link MatchSport} associada ao cartão.
     * @param team A instância de {@link Team} associada ao cartão.
     * @return Uma nova instância de {@link PenaltyCard} com base nos dados fornecidos.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "color", source = "penaltyCardDto.color.name")
    PenaltyCard toNewPenaltyCard(PenaltyCardDto penaltyCardDto, MatchSport matchSport, Team team);

    /**
     * Atualiza uma instância existente de {@link PenaltyCard} com base nos dados fornecidos.
     *
     * @param id Identificador único do cartão que será atualizado.
     * @param goalDto DTO do tipo {@link PenaltyCardDto} contendo os dados do cartão que será atualizado.
     * @param player Jogador do tipo {@link Participant} associado ao cartão.
     * @param matchSport Instância de {@link MatchSport} associada ao cartão.
     * @param team Instância de {@link Team} associada ao cartão.
     * @return Uma nova instância atualizada de {@link PenaltyCard} com base nos dados fornecidos.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "color", source = "penaltyCardDto.color.name")
    PenaltyCard toExistingPenaltyCard(Long id, PenaltyCardDto penaltyCardDto, MatchSport matchSport, Team team);

}
