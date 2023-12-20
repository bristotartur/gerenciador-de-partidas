package com.bristotartur.gerenciadordepartidas.domain.match;

import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.enums.MatchStatus;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @JsonProperty("team_a")
    @ManyToOne(cascade = CascadeType.PERSIST) @JoinColumn(name = "team_a_id", nullable = false)
    private Team teamA;

    @JsonBackReference
    @JsonProperty("team_b")
    @ManyToOne(cascade = CascadeType.PERSIST) @JoinColumn(name = "team_b_id", nullable = false)
    private Team teamB;

    @JsonBackReference
    @JsonProperty("sport")
    @ManyToOne(cascade = CascadeType.PERSIST) @JoinColumn(name = "sport_id", nullable = false)
    private MatchSport matchSport;

    @JsonProperty("team_a_score")
    @Column(name = "team_a_score")
    private Integer teamScoreA;

    @JsonProperty("team_b_score")
    @Column(name = "team_b_score")
    private Integer teamScoreB;

    private Modality modality;

    @JsonProperty("match_status")
    @Column(name = "match_status")
    private MatchStatus matchStatus;

    @JsonProperty("match_start")
    @Column(name = "match_start")
    private LocalDateTime matchStart;

    @JsonProperty("match_end")
    @Column(name = "match_end")
    private LocalDateTime matchEnd;
}
