package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingMatchDto;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
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
     * @param goalDto DTO do tipo {@link MatchDto} contendo os dados e metadados da nova partida.
     * @param players Lista do tipo {@link Participant} contendo todos os jogadores da partida.
     * @param teamA A instância de {@link Team} associada a partida que representa a equipe A.
     * @param teamB A instância de {@link Team} associada a partida que representa a equipe B.
     * @return Uma nova instância de {@link Match} com base nos dados fornecidos.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teamScoreA", constant = "0")
    @Mapping(target = "teamScoreB", constant = "0")
    @Mapping(target = "modality", source = "matchDto.modality.name")
    @Mapping(target = "matchStatus", source = "matchDto.matchStatus.name")
    Match toNewMatch(MatchDto matchDto, List<Participant> players, Team teamA, Team teamB);

    /**
     * Atualiza uma instância existente de {@link Match} com base nos dados fornecidos.
     *
     * @param id Identificador único da partida que será atualizada.
     * @param goalDto DTO do tipo {@link MatchDto} contendo os dados da partida que será atualizada.
     * @param players Lista do tipo {@link Participant} contendo todos os jogadores da partida.
     * @param teamA Instância de {@link Team} associada a partida que representa a equipe A.
     * @param teamB Instância de {@link Team} associada a partida que representa a equipe B.
     * @return Uma nova instância atualizada de {@link Match} com base nos dados fornecidos.
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "teamScoreA", ignore = true)
    @Mapping(target = "teamScoreB", ignore = true)
    @Mapping(target = "modality", source = "matchDto.modality.name")
    @Mapping(target = "matchStatus", source = "matchDto.matchStatus.name")
    Match toExistingMatch(Long id, MatchDto matchDto, List<Participant> players, Team teamA, Team teamB);

    /**
     * Gera uma nova instância de {@link ExposingMatchDto} a partir de qualquer instância de {@link Match}
     * ou de suas classes filhas.
     *
     * @param match Partida contendo os dados a serem mapeados.
     * @param sport A modalidade esportiva da partida.
     * @return Uma nova instância de {@link ExposingMatchDto}.
     */
    @Mapping(target = "teamA", source = "match.teamA.name")
    @Mapping(target = "teamB", source = "match.teamB.name")
    ExposingMatchDto toNewExposingMatchDto(Match match, String sport);

}
