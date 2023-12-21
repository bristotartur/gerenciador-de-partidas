package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.Match;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchMapper {

    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "matchSport", expression = "java(MapperUtil.matchSportConversor(matchDto.sport()))")
    @Mapping(target = "teamA", expression = "java(MapperUtil.findTeamById(matchDto.teamAId()))")
    @Mapping(target = "teamB", expression = "java(MapperUtil.findTeamById(matchDto.teamBId()))")
    @Mapping(target = "teamScoreA", constant = "0")
    @Mapping(target = "teamScoreB", constant = "0")
    @Mapping(target = "matchStatus", constant = "SCHEDULED")
    @Mapping(target = "matchStart", constant = "0000-00-00 00:00:00")
    @Mapping(target = "matchEnd", constant = "0000-00-00 00:00:00")
    Match toNewMatch(MatchDto matchDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "matchSport", expression = "java(MapperUtil.matchSportConversor(matchDto.sport()))")
    @Mapping(target = "teamA", expression = "java(MapperUtil.findTeamById(matchDto.teamAId()))")
    @Mapping(target = "teamB", expression = "java(MapperUtil.findTeamById(matchDto.teamBId()))")
    Match toExistingMatch(Long id, MatchDto matchDto);

}
