package com.bristotartur.gerenciadordepartidas.controllers;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;
import com.bristotartur.gerenciadordepartidas.dtos.GoalDto;
import com.bristotartur.gerenciadordepartidas.services.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/gerenciador-de-partidas/api/goals")
@RequiredArgsConstructor
@Transactional
public class GoalController {

    private final GoalService goalService;

    @GetMapping
    public ResponseEntity<List<Goal>> findAllGoals() {

        List<Goal> goalList = goalService.findAllGoals();

        if (goalList.isEmpty())
            return ResponseEntity.noContent().build();

        goalList.forEach(this::addSingleGoalLink);
        return ResponseEntity.ok().body(goalList);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Goal> findGoalById(@PathVariable Long id) {

        var goal = goalService.findGoalById(id);

        this.addGoalListLink(goal);
        return ResponseEntity.ok().body(goal);
    }

    @PostMapping
    public ResponseEntity<Goal> saveGoal(@RequestBody @Valid GoalDto goalDto) {

        var goal = goalService.saveGoal(goalDto);

        this.addGoalListLink(goal);
        return ResponseEntity.status(HttpStatus.CREATED).body(goal);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {

        goalService.deleteGoalById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Goal> replaceGoal(@PathVariable Long id,
                                            @RequestBody @Valid GoalDto goalDto) {

        var goal = goalService.replaceGoal(id, goalDto);

        this.addGoalListLink(goal);
        return ResponseEntity.ok().body(goal);
    }

    private void addSingleGoalLink(Goal goal) {

        var id = goal.getId();
        goal.add(linkTo(methodOn(this.getClass()).findGoalById(id)).withSelfRel());
    }

    private void addGoalListLink(Goal goal) {
        goal.add(linkTo(methodOn(this.getClass()).findAllGoals()).withRel("Goal list"));
    }

}
