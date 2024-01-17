package com.bristotartur.gerenciadordepartidas.domain.match.structure;

import com.bristotartur.gerenciadordepartidas.domain.participant.Participant;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MATCH")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Match extends RepresentationModel<Match> {

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

    @ManyToMany
    @JoinTable(
            name = "match_player",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<Participant> players;

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
