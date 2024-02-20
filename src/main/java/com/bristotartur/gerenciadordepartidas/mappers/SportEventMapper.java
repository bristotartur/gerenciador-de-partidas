package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.dtos.exposing.ExposingSportEventDto;
import com.bristotartur.gerenciadordepartidas.dtos.input.SportEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SportEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstPlace", expression = "java(Team.NONE)")
    @Mapping(target = "secondPlace", expression = "java(Team.NONE)")
    @Mapping(target = "thirdPlace", expression = "java(Team.NONE)")
    @Mapping(target = "fourthPlace", expression = "java(Team.NONE)")
    @Mapping(target = "fifthPlace", expression = "java(Team.NONE)")
    @Mapping(target = "eventStatus", expression = "java(Status.SCHEDULED)")
    @Mapping(target = "matches", ignore = true)
    @Mapping(target = "participants", ignore = true)
    SportEvent toNewSportEvent(SportEventDto sportEventDto, Edition edition);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", source = "sportEventDto.type")
    @Mapping(target = "modality", source = "sportEventDto.modality")
    @Mapping(target = "totalMatches", source = "sportEventDto.totalMatches")
    @Mapping(target = "edition", source = "edition")
    @Mapping(target = "participants", source = "sportEvent.participants")
    SportEvent toExistingSportEvent(Long id, SportEventDto sportEventDto, SportEvent sportEvent, Edition edition);

    @Mapping(target = "sportEventId", source = "sportEvent.id")
    @Mapping(target = "firstPlace", source = "sportEvent.firstPlace")
    @Mapping(target = "secondPlace", source = "sportEvent.secondPlace")
    @Mapping(target = "thirdPlace", source = "sportEvent.thirdPlace")
    @Mapping(target = "fourthPlace", source = "sportEvent.fourthPlace")
    @Mapping(target = "fifthPlace", source = "sportEvent.fifthPlace")
    ExposingSportEventDto toNewExposingSportEventDto(SportEvent sportEvent);

}
