package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.GoalDto;
import com.bristotartur.gerenciadordepartidas.services.GeneralMatchSportService;
import com.bristotartur.gerenciadordepartidas.services.TeamService;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface GoalMapper {

    @Mapping(target = "id", ignore = true)
    Goal toNewGoal(GoalDto goalDto, MatchSport matchSport, Team team);

    @Mapping(target = "id", source = "id")
     Goal toExistingGoal(Long id, GoalDto goalDto, MatchSport matchSport, Team team);

}