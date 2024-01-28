package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.actions.Goal;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingGoalDto;
import com.bristotartur.gerenciadordepartidas.dtos.GoalDto;
import com.bristotartur.gerenciadordepartidas.services.actions.GoalService;
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
@RequestMapping("/gerenciador-de-partidas/api/goals")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GoalController {

    private final GoalService goalService;

    @GetMapping
    public ResponseEntity<Page<ExposingGoalDto>> listAllGoals(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();

        log.info("Request to get Goal page of number '{}' and size '{}' was made.", number, size);

        var dtoPage = this.createExposingDtoPage(goalService.findAllGoals(pageable));
        return ResponseEntity.ok().body(dtoPage);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExposingGoalDto> findGoalById(@PathVariable Long id) {

        log.info("Request to find Goal '{}' was made.", id);

        var goal = goalService.findGoalById(id);
        var dto = this.addGoalListLink(goal, PageRequest.of(0, 20));

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<ExposingGoalDto> saveGoal(@RequestBody @Valid GoalDto goalDto) {

        log.info("Request to create a new Goal was made.");

        var goal = goalService.saveGoal(goalDto);
        var dto = this.addGoalListLink(goal, PageRequest.of(0, 20));

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {

        log.info("Request to delete Goal '{}' was made.", id);

        goalService.deleteGoalById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ExposingGoalDto> replaceGoal(@PathVariable Long id,
                                                       @RequestBody @Valid GoalDto goalDto) {

        log.info("Request to update Goal '{}' was made.", id);

        var goal = goalService.replaceGoal(id, goalDto);
        var dto = this.addGoalListLink(goal, PageRequest.of(0, 20));

        return ResponseEntity.ok().body(dto);
    }

    private Page<ExposingGoalDto> createExposingDtoPage(Page<Goal> goalPage) {

        var goals = goalPage.getContent();
        var dtos = goals.stream()
                .map(this::addSingleGoalLink)
                .toList();

        return new PageImpl<>(dtos, goalPage.getPageable(), goalPage.getSize());
    }

    private ExposingGoalDto addSingleGoalLink(Goal goal) {

        var id = goal.getId();
        var playerId = goal.getPlayer().getId();
        var matchId = goal.getMatch().getId();
        var dto = goalService.createExposingGoalDto(goal);

        dto.add(linkTo(methodOn(this.getClass()).findGoalById(id)).withSelfRel());
        dto.add(linkTo(methodOn(ParticipantController.class).findParticipantById(playerId)).withRel("player"));
        dto.add(linkTo(methodOn(MatchController.class).findMatchById(matchId)).withRel("match"));

        return dto;
    }

    private ExposingGoalDto addGoalListLink(Goal goal, Pageable pageable) {

        var playerId = goal.getPlayer().getId();
        var matchId = goal.getMatch().getId();
        var dto = goalService.createExposingGoalDto(goal);

        dto.add(linkTo(methodOn(this.getClass()).listAllGoals(pageable)).withRel("goals"));
        dto.add(linkTo(methodOn(ParticipantController.class).findParticipantById(playerId)).withRel("player"));
        dto.add(linkTo(methodOn(MatchController.class).findMatchById(matchId)).withRel("match"));

        return dto;
    }

}
