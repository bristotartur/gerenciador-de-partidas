package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.participant.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/participants")
@RequiredArgsConstructor
@Transactional
public class ParticipantController {

    @GetMapping
    public ResponseEntity<List<Participant>> findAllParticipants() {
        return null;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Participant> findParticipantById(@PathVariable Long id) {
        return null;
    }

    private void addSingleParticipantLink(Participant participant) {

    }

    private void addParticipantListLink(Participant participant) {
        
    }

}
