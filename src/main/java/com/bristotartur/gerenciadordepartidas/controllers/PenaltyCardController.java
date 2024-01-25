package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.actions.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.dtos.PenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.services.actions.PenaltyCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/gerenciador-de-partidas/api/penalty-cards")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PenaltyCardController {

    private final PenaltyCardService penaltyCardService;

    @GetMapping
    public ResponseEntity<List<PenaltyCard>> findAllPenaltyCards() {

        log.info("Request to find all Penalty Cards was made.");

        List<PenaltyCard> penaltyCardList = penaltyCardService.findAllPenaltyCards();

        if (penaltyCardList.isEmpty())
            return ResponseEntity.noContent().build();

        penaltyCardList.forEach(this::addSinglePenaltyCardLink);
        return ResponseEntity.ok().body(penaltyCardList);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PenaltyCard> findPenaltyCardById(@PathVariable Long id) {

        log.info("Request to find Penalty Card '{}' was made.", id);

        var penaltyCard = penaltyCardService.findPenaltyCardById(id);
        this.addPenaltyCardListLink(penaltyCard);

        return ResponseEntity.ok().body(penaltyCard);
    }

    @PostMapping
    public ResponseEntity<PenaltyCard> savePenaltyCard(@RequestBody @Valid PenaltyCardDto penaltyCardDto) {

        log.info("Request to create a new Penalty Card was made.");

        var penaltyCard = penaltyCardService.savePenaltyCard(penaltyCardDto);
        this.addPenaltyCardListLink(penaltyCard);

        return ResponseEntity.status(HttpStatus.CREATED).body(penaltyCard);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePenaltyCard(@PathVariable Long id) {

        log.info("Request to delete Penalty Card '{}' was made.", id);

        penaltyCardService.deletePenaltyCardById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PenaltyCard> replacePenaltyCard(@PathVariable Long id,
                                                          @RequestBody @Valid PenaltyCardDto penaltyCardDto) {

        log.info("Request to update Penalty Card '{}' was made.", id);

        var penaltyCard = penaltyCardService.replacePenaltyCard(id, penaltyCardDto);
        this.addPenaltyCardListLink(penaltyCard);

        return ResponseEntity.ok().body(penaltyCard);
    }

    private void addSinglePenaltyCardLink(PenaltyCard penaltyCard) {
        var id = penaltyCard.getId();
        penaltyCard.add(linkTo(methodOn(this.getClass()).findPenaltyCardById(id)).withSelfRel());
    }

    private void addPenaltyCardListLink(PenaltyCard penaltyCard) {
        penaltyCard.add(linkTo(methodOn(this.getClass()).findAllPenaltyCards()).withRel("Penalty cards list"));
    }

}
