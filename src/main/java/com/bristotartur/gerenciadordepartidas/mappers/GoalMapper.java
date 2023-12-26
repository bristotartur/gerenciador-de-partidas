package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;
import com.bristotartur.gerenciadordepartidas.dtos.GoalDto;
import com.bristotartur.gerenciadordepartidas.services.GeneralMatchSportService;
import com.bristotartur.gerenciadordepartidas.services.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GoalMapper {

    private final GeneralMatchSportService generalMatchSportService;
    private final TeamService teamService;

    public Goal toNewGoal(GoalDto goalDto) {

        return Goal.builder()
                .team(teamService.findTeamById(goalDto.teamId()))
                .matchSport(generalMatchSportService.findMatchSportForGoal(goalDto.matchSportId(), goalDto.sport()))
                .goalTime(goalDto.goalTime())
                .build();
    }
    public Goal toExistingGoal(Long id, GoalDto goalDto) {

        return Goal.builder()
                .id(id)
                .team(teamService.findTeamById(goalDto.teamId()))
                .matchSport(generalMatchSportService.findMatchSportForGoal(goalDto.matchSportId(), goalDto.sport()))
                .goalTime(goalDto.goalTime())
                .build();
    }
}