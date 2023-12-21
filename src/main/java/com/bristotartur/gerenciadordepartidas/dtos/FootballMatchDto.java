package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.domain.match.Match;
import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;

import java.util.List;

public record FootballMatchDto(List<Match> matches,
                               List<Goal> goals) {
}
