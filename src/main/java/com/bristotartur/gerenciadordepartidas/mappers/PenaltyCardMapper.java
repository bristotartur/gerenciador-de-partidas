package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.PenaltyCardDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PenaltyCardMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "color", source = "penaltyCardDto.color.name")
    PenaltyCard toNewPenaltyCard(PenaltyCardDto penaltyCardDto, MatchSport matchSport, Team team);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "color", source = "penaltyCardDto.color.name")
    PenaltyCard toExistingPenaltyCard(Long id, PenaltyCardDto penaltyCardDto, MatchSport matchSport, Team team);

}
