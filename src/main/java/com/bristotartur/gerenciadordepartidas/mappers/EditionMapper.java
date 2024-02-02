package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.dtos.input.EditionDto;
import com.bristotartur.gerenciadordepartidas.dtos.exposing.ExposingEditionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EditionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "atomicaPontuation", constant = "0")
    @Mapping(target = "mestresPontuation", constant = "0")
    @Mapping(target = "papaPontuation", constant = "0")
    @Mapping(target = "twisterPontuation", constant = "0")
    @Mapping(target = "uniconttiPontuation", constant = "0")
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "taskEvents", ignore = true)
    @Mapping(target = "sportEvents", ignore = true)
    Edition toNewEdition(EditionDto editionDto);
    
    @Mapping(target = "editionStatus", source = "editionDto.editionStatus")
    @Mapping(target = "opening", source = "editionDto.opening")
    @Mapping(target = "closure", source = "editionDto.closure")
    Edition toExistingEdition(Long id, EditionDto editionDto, Edition originalEdition);

    @Mapping(target = "editionId", source = "edition.id")
    @Mapping(target = "atomica", source = "edition.atomicaPontuation")
    @Mapping(target = "mestres", source = "edition.mestresPontuation")
    @Mapping(target = "papaLeguas", source = "edition.papaPontuation")
    @Mapping(target = "twister", source = "edition.twisterPontuation")
    @Mapping(target = "unicontti", source = "edition.uniconttiPontuation")
    @Mapping(target = "status", source = "edition.editionStatus")
    ExposingEditionDto toNewExposingEditionDto(Edition edition);

}
