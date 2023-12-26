package com.bristotartur.gerenciadordepartidas.domain.team;

import com.bristotartur.gerenciadordepartidas.domain.match.Match;
import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;
import com.bristotartur.gerenciadordepartidas.domain.match.specifications.PenaltyCard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private Integer points = 0;

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
    private List<Goal> goals; // guarda tanto os gols de futebol quanto os de handebol

    @JsonManagedReference
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PenaltyCard> penaltyCards;

}