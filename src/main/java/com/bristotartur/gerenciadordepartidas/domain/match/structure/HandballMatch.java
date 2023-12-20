package com.bristotartur.gerenciadordepartidas.domain.match.structure;

import com.bristotartur.gerenciadordepartidas.domain.match.Match;
import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "handball_match")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HandballMatch extends MatchSport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonManagedReference
    @OneToMany(mappedBy = "matchSport", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Match> matches;

    @JsonManagedReference
    @OneToMany(mappedBy = "matchSport", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Goal> goals;

    @JsonProperty("yellow_cards")
    @Column(name = "yellow_cards")
    private Integer yellowCards;

    @JsonProperty("red_cards")
    @Column(name = "red_cards")
    private Integer redCards;
}
