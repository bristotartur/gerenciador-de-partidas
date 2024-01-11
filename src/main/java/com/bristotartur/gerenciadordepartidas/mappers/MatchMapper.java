package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teamScoreA", constant = "0")
    @Mapping(target = "teamScoreB", constant = "0")
    @Mapping(target = "modality", source = "matchDto.modality.name")
    @Mapping(target = "matchStatus", source = "matchDto.matchStatus.name")
    Match toNewMatch(MatchDto matchDto, MatchSport matchSport, Team teamA, Team teamB);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "modality", source = "matchDto.modality.name")
    @Mapping(target = "matchStatus", source = "matchDto.matchStatus.name")
    Match toExistingMatch(Long id, MatchDto matchDto, MatchSport matchSport, Team teamA, Team teamB);

}
