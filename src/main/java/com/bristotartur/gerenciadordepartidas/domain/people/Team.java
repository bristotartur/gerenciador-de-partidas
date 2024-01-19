package com.bristotartur.gerenciadordepartidas.domain.people;

import com.bristotartur.gerenciadordepartidas.domain.structure.Match;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Entity
@Table(name = "TEAM")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Team extends RepresentationModel<Team> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
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

}