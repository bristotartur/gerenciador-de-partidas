package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.dtos.EditionDto;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingEditionDto;
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
    Edition toNewEdition(EditionDto editionDto);

    @Mapping(target = "atomicaPontuation", ignore = true)
    @Mapping(target = "mestresPontuation", ignore = true)
    @Mapping(target = "papaPontuation", ignore = true)
    @Mapping(target = "twisterPontuation", ignore = true)
    @Mapping(target = "uniconttiPontuation", ignore = true)
    Edition toExistingEdition(Long id, EditionDto editionDto);

    @Mapping(target = "editionId", source = "edition.id")
    @Mapping(target = "atomica", source = "edition.atomicaPontuation")
    @Mapping(target = "mestres", source = "edition.mestresPontuation")
    @Mapping(target = "papaLeguas", source = "edition.papaPontuation")
    @Mapping(target = "twister", source = "edition.twisterPontuation")
    @Mapping(target = "unicontti", source = "edition.uniconttiPontuation")
    @Mapping(target = "status", source = "edition.editionStatus")
    ExposingEditionDto toNewExposingEditionDto(Edition edition);

}
