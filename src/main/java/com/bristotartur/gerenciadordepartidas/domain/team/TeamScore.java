package com.bristotartur.gerenciadordepartidas.domain.team;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.FootballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.HandballMatch;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "team_score")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TeamScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "teamScore")
//    private Team team;
//
//    @JsonManagedReference
//    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<Goal> goals; // guarda tanto os gols de futebol quanto os de handebol
}
