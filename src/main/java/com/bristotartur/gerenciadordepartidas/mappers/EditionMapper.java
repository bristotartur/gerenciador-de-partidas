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
    @Mapping(target = "atomicaScore", constant = "0")
    @Mapping(target = "mestresScore", constant = "0")
    @Mapping(target = "papaScore", constant = "0")
    @Mapping(target = "twisterScore", constant = "0")
    @Mapping(target = "uniconttiScore", constant = "0")
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "taskEvents", ignore = true)
    @Mapping(target = "sportEvents", ignore = true)
    Edition toNewEdition(RequestEditionDto dto);

    @Mapping(target = "opening", source = "dto.opening")
    @Mapping(target = "closure", source = "dto.closure")
    Edition toExistingEdition(Long id, RequestEditionDto dto, Edition originalEdition);

    @Mapping(target = "editionId", source = "edition.id")
    @Mapping(target = "atomica", source = "edition.atomicaScore")
    @Mapping(target = "mestres", source = "edition.mestresScore")
    @Mapping(target = "papaLeguas", source = "edition.papaScore")
    @Mapping(target = "twister", source = "edition.twisterScore")
    @Mapping(target = "unicontti", source = "edition.uniconttiScore")
    ResponseEditionDto toNewExposingEditionDto(Edition edition);

}
