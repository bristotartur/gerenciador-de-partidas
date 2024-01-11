package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.dtos.PenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.services.GeneralMatchSportService;
import com.bristotartur.gerenciadordepartidas.services.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PenaltyCardMapper {

    private final TeamService teamService;
    private final GeneralMatchSportService generalMatchSportService;

    public PenaltyCard toNewPenaltyCard(PenaltyCardDto penaltyCardDto) {

        return PenaltyCard.builder()
                .team(teamService.findTeamById(penaltyCardDto.teamId()))
                .penaltyCardTime(penaltyCardDto.penaltyCardTime())
                .matchSport(generalMatchSportService
                        .findMatchSportForCard(penaltyCardDto.matchSportId(), penaltyCardDto.sport()))
                .color(penaltyCardDto.color().name())
                .build();
    }

    public PenaltyCard toExistingPenaltyCard(Long id, PenaltyCardDto penaltyCardDto) {

        return PenaltyCard.builder()
                .id(id)
                .penaltyCardTime(penaltyCardDto.penaltyCardTime())
                .team(teamService.findTeamById(penaltyCardDto.teamId()))
                .matchSport(generalMatchSportService
                        .findMatchSportForCard(penaltyCardDto.matchSportId(), penaltyCardDto.sport()))
                .color(penaltyCardDto.color().name())
                .build();
    }

}
