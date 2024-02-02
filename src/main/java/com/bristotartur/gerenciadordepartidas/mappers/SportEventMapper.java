package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.dtos.input.SportEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SportEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstPlace", expression = "java(com.bristotartur.gerenciadordepartidas.enums.Team.NONE)")
    @Mapping(target = "secondPlace", expression = "java(com.bristotartur.gerenciadordepartidas.enums.Team.NONE)")
    @Mapping(target = "thirdPlace", expression = "java(com.bristotartur.gerenciadordepartidas.enums.Team.NONE)")
    @Mapping(target = "fourthPlace", expression = "java(com.bristotartur.gerenciadordepartidas.enums.Team.NONE)")
    @Mapping(target = "fifthPlace", expression = "java(com.bristotartur.gerenciadordepartidas.enums.Team.NONE)")
    @Mapping(target = "matches", ignore = true)
    SportEvent toNewSportEvent(SportEventDto sportEventDto, Edition edition);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", source = "sportEventDto.type")
    @Mapping(target = "modality", source = "sportEventDto.modality")
    @Mapping(target = "totalMatches", source = "sportEventDto.totalMatches")
    @Mapping(target = "edition", source = "edition")
    SportEvent toExistingSportEvent(Long id, SportEventDto sportEventDto, SportEvent sportEvent, Edition edition);

}
