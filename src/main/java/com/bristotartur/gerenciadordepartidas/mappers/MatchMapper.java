package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.dtos.exposing.ExposingMatchDto;
import com.bristotartur.gerenciadordepartidas.dtos.input.MatchDto;
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
     * @param event Instância de {@link SportEvent} na qual a partida está associada.
     * @return Uma nova instância de {@link Match} com base nos dados fornecidos.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teamScoreA", constant = "0")
    @Mapping(target = "teamScoreB", constant = "0")
    @Mapping(target = "modality", source = "matchDto.modality")
    Match toNewMatch(MatchDto matchDto, List<Participant> players, SportEvent event);

    /**
     * Atualiza uma instância existente de {@link Match} com base nos dados fornecidos.
     *
     * @param id Identificador único da partida que será atualizada.
     * @param matchDto DTO do tipo {@link MatchDto} contendo os dados da partida que será atualizada.
     * @param match Instância da partida que será atualizada.
     * @param players Lista do tipo {@link Participant} contendo todos os jogadores da partida.
     * @param event Instância de {@link SportEvent} na qual a partida está associada.
     * @return Uma nova instância atualizada de {@link Match} com base nos dados fornecidos.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "teamA", source = "matchDto.teamA")
    @Mapping(target = "teamB", source = "matchDto.teamB")
    @Mapping(target = "modality", source = "matchDto.modality")
    @Mapping(target = "matchStatus", source = "matchDto.matchStatus")
    @Mapping(target = "matchStart", source = "matchDto.matchStart")
    @Mapping(target = "matchEnd", source = "matchDto.matchEnd")
    Match toExistingMatch(Long id, MatchDto matchDto, Match match, List<Participant> players, SportEvent event);

    /**
     * Gera uma nova instância de {@link ExposingMatchDto} a partir de qualquer instância de {@link Match}
     * ou de suas classes filhas.
     *
     * @param match Partida contendo os dados a serem mapeados.
     * @param sport A modalidade esportiva da partida.
     * @return Uma nova instância de {@link ExposingMatchDto}.
     */
    @Mapping(target = "matchId", source = "match.id")
    ExposingMatchDto toNewExposingMatchDto(Match match, Sports sport);

}
