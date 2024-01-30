package com.bristotartur.gerenciadordepartidas.domain.people;

import com.bristotartur.gerenciadordepartidas.domain.actions.Goal;
import com.bristotartur.gerenciadordepartidas.domain.actions.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entidade responsável por representar as equipes da gincana no sistema. Cada equipe possui
 * um nome predefinido em {@link TeamName}. Equipes não podem ser salvas com o mesmo nome e este
 * nome não pode ser diferente dos fornecidos no sistema, o que faz com que apenas um número
 * finito de equipes possam ser salvas.
 *
 * @see Participant
 * @see Match
 */
@Entity
@Table(name = "TEAM")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer points;

    @JsonManagedReference
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Participant> members;

    @JsonManagedReference
    @OneToMany(mappedBy = "teamA", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Match> matchesAsTeamA;

    @JsonManagedReference
    @OneToMany(mappedBy = "teamB", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Match> matchesAsTeamB;

    @JsonManagedReference
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Goal> goals;

    @JsonManagedReference
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PenaltyCard> penaltyCards;

}