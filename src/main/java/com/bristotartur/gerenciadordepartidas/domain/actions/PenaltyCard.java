package com.bristotartur.gerenciadordepartidas.domain.actions;

import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.enums.PenaltyCardColor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

/**
 * <p>Entidade responsável por representar cartões de penalidade no sistema. Esta entidade carrega consigo
 * o jogador que recebeu o cartão, sua equipe e a partida em que a ação ocorreu.</p>
 *
 * <p>As cores possíveis para os cartões estão definidas em {@link PenaltyCardColor}.</p>
 *
 * @see Participant
 * @see Match
 */
@Entity
@Table(name = "PENALTY_CARD")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class PenaltyCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PenaltyCardColor color;

    @Column(nullable = false)
    private LocalTime penaltyCardTime;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant player;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

}
