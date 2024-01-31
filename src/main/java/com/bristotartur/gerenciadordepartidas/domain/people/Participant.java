package com.bristotartur.gerenciadordepartidas.domain.people;

import com.bristotartur.gerenciadordepartidas.domain.actions.Goal;
import com.bristotartur.gerenciadordepartidas.domain.actions.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entidade responsável por representar todos os participantes da gincana no sistema.
 * Objetos que instanciam esta classe podem às vezes ser representados como "players"
 * em um contexto de partidas, ou como "members" quando se trata de equipes.
 *
 * @see Team
 * @see Match
 * @see Goal
 * @see PenaltyCard
 */
@Entity
@Table(name = "PARTICIPANT")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Participant {

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
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "edition_id", nullable = false)
    private Edition edition;

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
