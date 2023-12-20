package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record TeamDto(@NotNull @JsonProperty("team_name") TeamName teamName,
                      @NotNull Integer points /*,
                      List<Long> matchIds*/) { // comentado, pois não sei se é necessário
}
