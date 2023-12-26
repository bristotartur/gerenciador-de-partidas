package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.dtos.PenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.services.GeneralMatchSportService;
import com.bristotartur.gerenciadordepartidas.services.TeamService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PenaltyCardMapper {

    private final TeamService teamService;
    private final GeneralMatchSportService generalMatchSportService;

    //    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "team", expression = "java(MapperUtil.findTeamById(penaltyCardDto.teamId()))")
//    @Mapping(
//            target = "matchSport",
//            expression = "java(MapperUtil.findMatchSportForCard(penaltyCardDto.matchSportId(), penaltyCardDto.sport()))"
//    )
    public PenaltyCard toNewPenaltyCard(PenaltyCardDto penaltyCardDto) {

        return PenaltyCard.builder()
                .team(teamService.findTeamById(penaltyCardDto.teamId()))
                .matchSport(generalMatchSportService
                        .findMatchSportForCard(penaltyCardDto.matchSportId(), penaltyCardDto.sport()))
                .color(penaltyCardDto.color().name())
                .build();
    }

    //    @Mapping(target = "team", expression = "java(MapperUtil.findTeamById(penaltyCardDto.teamId()))")
//    @Mapping(
//            target = "matchSport",
//            expression = "java(MapperUtil.findMatchSportForCard(penaltyCardDto.matchSportId(), penaltyCardDto.sport()))"
//    )
    public PenaltyCard toExistingPenaltyCard(Long id, PenaltyCardDto penaltyCardDto) {

        return PenaltyCard.builder()
                .id(id)
                .team(teamService.findTeamById(penaltyCardDto.teamId()))
                .matchSport(generalMatchSportService
                        .findMatchSportForCard(penaltyCardDto.matchSportId(), penaltyCardDto.sport()))
                .color(penaltyCardDto.color().name())
                .build();
    }

}
