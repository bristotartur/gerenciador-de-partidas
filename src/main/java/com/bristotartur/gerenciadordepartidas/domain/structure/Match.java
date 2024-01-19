package com.bristotartur.gerenciadordepartidas.domain.structure;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;
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
@EqualsAndHashCode(callSuper = false)
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
