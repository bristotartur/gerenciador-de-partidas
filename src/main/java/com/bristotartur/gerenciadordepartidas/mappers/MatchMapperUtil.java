package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.TeamRepository;
import com.bristotartur.gerenciadordepartidas.utils.MatchSportFactory;

public class MatchMapperUtil {

    private static TeamRepository teamRepository;
    private static final String NOT_FOUND_MESSAGE = "Equipe não encontrada";

    public static Team findTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
    }

    public static MatchSport matchSportConversor(MatchDto matchDto) {
        return MatchSportFactory.newMatchSport(matchDto.sport());
    }
}
