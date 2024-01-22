package com.bristotartur.gerenciadordepartidas.dtos;

import com.bristotartur.gerenciadordepartidas.domain.structure.Match;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

/**
 * DTO responsável pela exposição de dados relativos a entidades do tipo {@link Match} e
 * suas classes filhas. O objetivo deste DTO é facilitar a transferência de dados entre
 * serviços que consomem esta API, exibindo apenas os dados necessários para análises
 * gerais das partidas, como placares, horários, equipes envolvidas, etc.
 */
@RequiredArgsConstructor
@Getter
public class ExposingMatchDto extends RepresentationModel<Match> {

    private final Long id;
    private final String sport;
    private final String teamA;
    private final String teamB;
    private final Integer teamScoreA;
    private final Integer teamScoreB;
    private final String modality;
    private final String matchStatus;
    private final LocalDateTime matchStart;
    private final LocalDateTime matchEnd;

}
