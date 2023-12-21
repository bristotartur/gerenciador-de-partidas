package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;
import com.bristotartur.gerenciadordepartidas.dtos.GoalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GoalMapper {

    GoalMapper INSTANCE = Mappers.getMapper(GoalMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "matchSport", expression = "java(MapperUtil.matchSportConversor(goalDto.sport()))")
    Goal toNewGoal(GoalDto goalDto);

    @Mapping(target = "matchSport", expression = "java(MapperUtil.matchSportConversor(goalDto.sport()))")
    Goal toExistingGoal(Long id, GoalDto goalDto);
}
