package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.enums.MatchStatus;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

public record MatchDto(@NotNull Sports sport,
                       @NotNull @JsonProperty("match_sport_id") Long matchSportId,
                       @NotNull @JsonProperty("team_a_id") Long teamAId,
                       @NotNull @JsonProperty("team_b_id") Long teamBId,
                       @JsonProperty("team_a_score") Integer teamScoreA,
                       @JsonProperty("team_b_score") Integer teamScoreB,
                       @NotNull Modality modality,
                       MatchStatus matchStatus,
                       LocalDateTime matchStart,
                       LocalDateTime matchEnd) {
}
