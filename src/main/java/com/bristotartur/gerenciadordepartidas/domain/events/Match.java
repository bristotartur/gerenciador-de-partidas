package com.bristotartur.gerenciadordepartidas.domain.events;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>Entidade reposnsável por fornecer uma representação geral de partidas no sistema, provendo
 * todos os atributos essenciais e comuns para qualquer tipo de partida, como jogadores, placar,
 * equipes envolvidas, horários, etc.</p>
 *
 * <p>Cada modalidade esportiva possui uma entidade filha de {@link Match} especializada nela, sendo
 * que estas especializações possuem atributos específicos para o seu tipo esportivo, como gols
 * para uma especialização em futsal ou sets para uma especialização em vôlei.</p>
 *
 * @see Team
 * @see Participant
 * @see FutsalMatch
 * @see HandballMatch
 * @see BasketballMatch
 * @see VolleyballMatch
 * @see TableTennisMatch
 * @see ChessMatch
 */
@Entity
@Table(name = "MATCH")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
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
