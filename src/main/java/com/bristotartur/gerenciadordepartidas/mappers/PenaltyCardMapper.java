package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.dtos.PenaltyCardDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PenaltyCardMapper {

    PenaltyCardMapper INSTANCE = Mappers.getMapper(PenaltyCardMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "color", source = "penaltyCardDto.color")
    PenaltyCard toNewPenaltyCard(PenaltyCardDto penaltyCardDto);

    @Mapping(target = "color", source = "penaltyCardDto.color")
    PenaltyCard toExistingPenaltyCard(Long id, PenaltyCardDto penaltyCardDto);
}
