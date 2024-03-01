package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.exposing.ExposingMatchDto;
import com.bristotartur.gerenciadordepartidas.dtos.exposing.ExposingParticipantDto;
import com.bristotartur.gerenciadordepartidas.dtos.input.ParticipantDto;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import com.bristotartur.gerenciadordepartidas.mappers.ParticipantMapper;
import com.bristotartur.gerenciadordepartidas.services.people.ParticipantService;
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
@RequestMapping("/gerenciador-de-partidas/api/participants")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ParticipantController {

    private final ParticipantService participantService;
    private final ParticipantMapper participantMapper;
    private final MatchController matchController;

    @GetMapping
    public ResponseEntity<Page<ExposingParticipantDto>> listAllParticipants(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        log.info("Request to get Participant page of number '{}' and size '{}' was made.", number, size);

        var dtoPage = this.createExposingDtoPage(participantService.findAllParticipants(pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @GetMapping(path = "/find")
    public ResponseEntity<Page<ExposingParticipantDto>> findParticipantsByNameLike(@RequestParam String name,
                                                                                   Pageable pageable) {
        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        log.info("Request to get Participant page of number '{}' and size '{}' with name like '{}' was made.", number, size, name);

        var dtoPage = this.createExposingDtoPage(participantService.findParticipantsByNameLike(name, pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @GetMapping(path = "/from")
    public ResponseEntity<Page<ExposingParticipantDto>> listMembersFromTeam(@RequestParam("team") String teamName,
                                                                            Pageable pageable) {
        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var team = Team.findTeamLike(teamName);

        log.info("Request to get Participant page of number '{}' and size '{}' from team '{}' was made.", number, size, team);

        var dtoPage = this.createExposingDtoPage(participantService.findMambersFromTeam(team, pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @GetMapping(path = "/{id}/matches")
    public ResponseEntity<Page<ExposingMatchDto>> listParticipantMatches(@PathVariable Long id, Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        log.info("Request to get Match page of number '{}' and size '{}' from Participant '{}' was made.", number, size, id);

        var dtoPage = matchController.createExposingDtoPage(participantService.findParticipantMatches(id, pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExposingParticipantDto> findParticipantById(@PathVariable Long id) {

        log.info("Request to find Participant '{}' was made.", id);

        var dto = this.createSingleExposingDto(participantService.findParticipantById(id));
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<ExposingParticipantDto> saveParticipant(@RequestBody @Valid ParticipantDto participantDto) {

        log.info("Request to create a new Participant was made.");

        var dto = this.createSingleExposingDto(participantService.saveParticipant(participantDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {

        log.info("Request to delete Participant '{}' was made.", id);

        participantService.deleteParticipantById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ExposingParticipantDto> replaceParticipant(@PathVariable Long id,
                                                                     @RequestBody @Valid ParticipantDto participantDto) {

        log.info("Request to update Participant '{}' was made.", id);

        var dto = this.createSingleExposingDto(participantService.replaceParticipant(id, participantDto));
        return ResponseEntity.ok().body(dto);
    }

    private ExposingParticipantDto createSingleExposingDto(Participant participant) {

        var id = participant.getId();
        var team = participant.getTeam().value;
        var dto = participantMapper.toNewExposingParticipantDto(participant);
        var pageable = PageRequest.of(0, 12);

        dto.add(linkTo(methodOn(this.getClass()).listAllParticipants(pageable)).withRel("participants"));
        dto.add(linkTo(methodOn(this.getClass()).listMembersFromTeam(team, pageable)).withRel("teamMembers"));
        dto.add(linkTo(methodOn(this.getClass()).listParticipantMatches(id, pageable)).withRel("matches"));

        return dto;
    }

    private Page<ExposingParticipantDto> createExposingDtoPage(Page<Participant> participantPage) {

        var participants = participantPage.getContent();
        var dtos = participants.stream()
                .map(this::addSingleParticipantLink)
                .toList();

        return new PageImpl<>(dtos, participantPage.getPageable(), participantPage.getSize());
    }

    private ExposingParticipantDto addSingleParticipantLink(Participant participant) {

        var id = participant.getId();
        var team = participant.getTeam().value;
        var dto = participantMapper.toNewExposingParticipantDto(participant);
        var pageable = PageRequest.of(0, 12);

        dto.add(linkTo(methodOn(this.getClass()).findParticipantById(id)).withSelfRel());
        dto.add(linkTo(methodOn(this.getClass()).listMembersFromTeam(team, pageable)).withRel("teamMembers"));
        dto.add(linkTo(methodOn(this.getClass()).listParticipantMatches(id, pageable)).withRel("matches"));

        return dto;
    }

}
