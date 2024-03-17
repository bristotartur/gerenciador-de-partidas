package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestEditionDto;
import com.bristotartur.gerenciadordepartidas.dtos.response.ResponseEditionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EditionMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "editionStatus", expression = "java(Status.SCHEDULED)")
    @Mapping(target = "atomicaPontuation", constant = "0")
    @Mapping(target = "mestresPontuation", constant = "0")
    @Mapping(target = "papaPontuation", constant = "0")
    @Mapping(target = "twisterPontuation", constant = "0")
    @Mapping(target = "uniconttiPontuation", constant = "0")
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "taskEvents", ignore = true)
    @Mapping(target = "sportEvents", ignore = true)
    Edition toNewEdition(RequestEditionDto dto);

    @Mapping(target = "opening", source = "dto.opening")
    @Mapping(target = "closure", source = "dto.closure")
    Edition toExistingEdition(Long id, RequestEditionDto dto, Edition originalEdition);

    @Mapping(target = "editionId", source = "edition.id")
    @Mapping(target = "atomica", source = "edition.atomicaPontuation")
    @Mapping(target = "mestres", source = "edition.mestresPontuation")
    @Mapping(target = "papaLeguas", source = "edition.papaPontuation")
    @Mapping(target = "twister", source = "edition.twisterPontuation")
    @Mapping(target = "unicontti", source = "edition.uniconttiPontuation")
    ResponseEditionDto toNewExposingEditionDto(Edition edition);

}
