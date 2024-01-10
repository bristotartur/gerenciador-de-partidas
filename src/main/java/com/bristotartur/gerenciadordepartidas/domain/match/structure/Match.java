package com.bristotartur.gerenciadordepartidas.domain.match.structure;

import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "MATCH")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_a_id", nullable = false)
    private Team teamA;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_b_id", nullable = false)
    private Team teamB;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "sport_id", nullable = false)
    private MatchSport matchSport;

    @Column(nullable = false)
    private Integer teamScoreA;

    @Column(nullable = false)
    private Integer teamScoreB;

    @Column(nullable = false)
    private String modality;

    @Column(nullable = false)
    private String matchStatus;

    @Column(nullable = false)
    private LocalDateTime matchStart;

    @Column(nullable = false)
    private LocalDateTime matchEnd;
}
