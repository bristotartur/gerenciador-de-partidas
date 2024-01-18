package com.bristotartur.gerenciadordepartidas.domain.people;

import com.bristotartur.gerenciadordepartidas.domain.actions.Goal;
import com.bristotartur.gerenciadordepartidas.domain.actions.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.domain.structure.Match;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Entity
@Table(name = "PARTICIPANT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participant extends RepresentationModel<Participant> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String classNumber;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @JsonBackReference
    @ManyToMany(mappedBy = "players", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Match> matches;

    @JsonManagedReference
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Goal> goal;

    @JsonManagedReference
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PenaltyCard> penaltyCards;

}
