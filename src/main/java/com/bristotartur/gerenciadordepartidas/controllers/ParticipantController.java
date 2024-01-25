package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.ParticipantDto;
import com.bristotartur.gerenciadordepartidas.services.people.ParticipantService;
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
@RequestMapping(value = "/gerenciador-de-partidas/api/participants")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ParticipantController {

    private final ParticipantService participantService;

    @GetMapping
    public ResponseEntity<List<Participant>> findAllParticipants() {

        log.info("Request to find all Participants was made.");

        List<Participant> participantList = participantService.findAllParticipants();

        if (participantList.isEmpty())
            return ResponseEntity.noContent().build();

        participantList.forEach(this::addSingleParticipantLink);
        return ResponseEntity.ok().body(participantList);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Participant> findParticipantById(@PathVariable Long id) {

        log.info("Request to find Participant '{}' was made.", id);

        var participant = participantService.findParticipantById(id);
        this.addParticipantListLink(participant);

        return ResponseEntity.ok().body(participant);
    }

    @PostMapping
    public ResponseEntity<Participant> saveParticipant(@RequestBody @Valid ParticipantDto participantDto) {

        log.info("Request to create a new Participant was made.");

        Participant participant = participantService.saveParticipant(participantDto);
        this.addParticipantListLink(participant);

        return ResponseEntity.status(HttpStatus.CREATED).body(participant);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {

        log.info("Request to delete Participant '{}' was made.", id);

        participantService.deleteParticipantById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Participant> replaceParticipant(@PathVariable Long id,
                                                          @RequestBody @Valid ParticipantDto participantDto) {

        log.info("Request to update Participant '{}' was made.", id);

        var participant = participantService.replaceParticipant(id, participantDto);
        this.addParticipantListLink(participant);

        return ResponseEntity.ok().body(participant);
    }

    private void addSingleParticipantLink(Participant participant) {

        Long id = participant.getId();
        Long teamId = participant.getTeam().getId();

        participant.add(linkTo(methodOn(this.getClass()).findParticipantById(id)).withSelfRel());
        participant.add(linkTo(methodOn(TeamController.class).findTeamById(teamId)).withRel("Team"));
    }

    private void addParticipantListLink(Participant participant) {

        var teamId = participant.getTeam().getId();

        participant.add(linkTo(methodOn(this.getClass()).findAllParticipants()).withRel("Participant list"));
        participant.add(linkTo(methodOn(TeamController.class).findTeamById(teamId)).withRel("Team"));
    }

}
