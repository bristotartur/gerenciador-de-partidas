package com.bristotartur.gerenciadordepartidas.dtos.exposing;

import com.bristotartur.gerenciadordepartidas.enums.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public class ExposingEditionDto extends RepresentationModel<ExposingEditionDto> {

    private final Long editionId;
    private final Integer atomica;
    private final Integer mestres;
    private final Integer papaLeguas;
    private final Integer twister;
    private final Integer unicontti;
    private final Status editionStatus;
    private final LocalDate opening;
    private final LocalDate closure;

}
