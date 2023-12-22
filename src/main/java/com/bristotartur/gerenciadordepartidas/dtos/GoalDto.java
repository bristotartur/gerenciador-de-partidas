package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record GoalDto(@NotNull @JsonProperty("goal_time") LocalTime goalTime,
                      @NotNull @JsonProperty("team_id") Long teamId,
                      @NotNull @JsonProperty("match_sport_id") Long matchSportId,
                      @NotNull Sports sport) {
}
