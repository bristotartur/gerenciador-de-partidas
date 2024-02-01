package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.actions.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.dtos.exposing.ExposingPenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.dtos.input.PenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.services.actions.PenaltyCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/gerenciador-de-partidas/api/penalty-cards")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PenaltyCardController {

    private final PenaltyCardService penaltyCardService;

    @GetMapping
    public ResponseEntity<Page<ExposingPenaltyCardDto>> listAllPenaltyCards(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        log.info("Request to get Penalty Card page of number '{}' and size '{}' was made.", number, size);

        var dtoPage = this.createExposingDtoPage(penaltyCardService.findAllPenaltyCards(pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExposingPenaltyCardDto> findPenaltyCardById(@PathVariable Long id) {

        log.info("Request to find Penalty Card '{}' was made.", id);

        var penaltyCard = penaltyCardService.findPenaltyCardById(id);
        var dto = this.addPenaltyCardListLink(penaltyCard, PageRequest.of(0, 20));

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<ExposingPenaltyCardDto> savePenaltyCard(@RequestBody @Valid PenaltyCardDto penaltyCardDto) {

        log.info("Request to create a new Penalty Card was made.");

        var penaltyCard = penaltyCardService.savePenaltyCard(penaltyCardDto);
        var dto = this.addPenaltyCardListLink(penaltyCard, PageRequest.of(0, 20));

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePenaltyCard(@PathVariable Long id) {

        log.info("Request to delete Penalty Card '{}' was made.", id);

        penaltyCardService.deletePenaltyCardById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ExposingPenaltyCardDto> replacePenaltyCard(@PathVariable Long id,
                                                                     @RequestBody @Valid PenaltyCardDto penaltyCardDto) {

        log.info("Request to update Penalty Card '{}' was made.", id);

        var penaltyCard = penaltyCardService.replacePenaltyCard(id, penaltyCardDto);
        var dto = this.addPenaltyCardListLink(penaltyCard, PageRequest.of(0, 20));

        return ResponseEntity.ok().body(dto);
    }

    private Page<ExposingPenaltyCardDto> createExposingDtoPage(Page<PenaltyCard> penaltyCardPage) {

        var penaltyCards = penaltyCardPage.getContent();
        var dtos = penaltyCards.stream()
                .map(this::addSingleGoalLink)
                .toList();

        return new PageImpl<>(dtos, penaltyCardPage.getPageable(), penaltyCardPage.getSize());
    }

    private ExposingPenaltyCardDto addSingleGoalLink(PenaltyCard penaltyCard) {

        var id = penaltyCard.getId();
        var playerId = penaltyCard.getPlayer().getId();
        var matchId = penaltyCard.getMatch().getId();
        var dto = penaltyCardService.createExposingPenaltyCardDto(penaltyCard);

        dto.add(linkTo(methodOn(this.getClass()).findPenaltyCardById(id)).withSelfRel());
        dto.add(linkTo(methodOn(ParticipantController.class).findParticipantById(playerId)).withRel("player"));
        dto.add(linkTo(methodOn(MatchController.class).findMatchById(matchId)).withRel("match"));

        return dto;
    }

    private ExposingPenaltyCardDto addPenaltyCardListLink(PenaltyCard penaltyCard, Pageable pageable) {

        var playerId = penaltyCard.getPlayer().getId();
        var matchId = penaltyCard.getMatch().getId();
        var dto = penaltyCardService.createExposingPenaltyCardDto(penaltyCard);

        dto.add(linkTo(methodOn(this.getClass()).listAllPenaltyCards(pageable)).withRel("penalty_cards"));
        dto.add(linkTo(methodOn(ParticipantController.class).findParticipantById(playerId)).withRel("player"));
        dto.add(linkTo(methodOn(MatchController.class).findMatchById(matchId)).withRel("match"));

        return dto;
    }

}
