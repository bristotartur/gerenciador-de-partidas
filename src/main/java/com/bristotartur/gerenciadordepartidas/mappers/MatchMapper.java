package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingMatchDto;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Interface responsável por gerar o mapeamento de dados relativos a entidade {@link Match}
 * para novas instâncias da mesma e para DTOs.
 *
 * @see MatchDto
 * @see ExposingMatchDto
 */
@Mapper(componentModel = "spring")
public interface MatchMapper {

    /**
     * Gera uma nova instância de {@link Match} com base nos dados fornecidos.
     *
     * @param matchDto DTO do tipo {@link MatchDto} contendo os dados e metadados da nova partida.
     * @param players Lista do tipo {@link Participant} contendo todos os jogadores da partida.
     * @return Uma nova instância de {@link Match} com base nos dados fornecidos.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teamScoreA", constant = "0")
    @Mapping(target = "teamScoreB", constant = "0")
    @Mapping(target = "modality", source = "matchDto.modality")
    @Mapping(target = "matchStatus", source = "matchDto.matchStatus")
    Match toNewMatch(MatchDto matchDto, List<Participant> players);

    /**
     * Atualiza uma instância existente de {@link Match} com base nos dados fornecidos.
     *
     * @param id Identificador único da partida que será atualizada.
     * @param matchDto DTO do tipo {@link MatchDto} contendo os dados da partida que será atualizada.
     * @param players Lista do tipo {@link Participant} contendo todos os jogadores da partida.
     * @return Uma nova instância atualizada de {@link Match} com base nos dados fornecidos.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "teamScoreA", ignore = true)
    @Mapping(target = "teamScoreB", ignore = true)
    @Mapping(target = "modality", source = "matchDto.modality")
    @Mapping(target = "matchStatus", source = "matchDto.matchStatus")
    Match toExistingMatch(Long id, MatchDto matchDto, List<Participant> players);

    /**
     * Gera uma nova instância de {@link ExposingMatchDto} a partir de qualquer instância de {@link Match}
     * ou de suas classes filhas.
     *
     * @param match Partida contendo os dados a serem mapeados.
     * @param sport A modalidade esportiva da partida.
     * @return Uma nova instância de {@link ExposingMatchDto}.
     */
    @Mapping(target = "matchId", source = "match.id")
    @Mapping(target = "teamA", source = "match.teamA")
    @Mapping(target = "teamB", source = "match.teamB")
    ExposingMatchDto toNewExposingMatchDto(Match match, Sports sport);

}
