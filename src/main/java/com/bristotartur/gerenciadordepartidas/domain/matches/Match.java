package com.bristotartur.gerenciadordepartidas.domain.matches;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.enums.Importance;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>Entidade reposnsável por fornecer uma representação geral de partidas no sistema, provendo
 * todos os atributos essenciais e comuns para qualquer tipo de partida, como jogadores, placar,
 * equipes envolvidas, horários, etc.</p>
 *
 * <p>Cada modalidade esportiva possui uma entidade filha de {@link Match} especializada nela, sendo
 * que estas especializações possuem atributos específicos para o seu tipo esportivo, como gols
 * para uma especialização em futsal ou sets para uma especialização em vôlei.</p>
 *
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
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Importance matchImportance;

    @Column(nullable = false, name = "team_a")
    @Enumerated(EnumType.STRING)
    private Team teamA;

    @Column(nullable = false, name = "team_b")
    @Enumerated(EnumType.STRING)
    private Team teamB;

    @ManyToMany
    @JoinTable(
            name = "match_player",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    @ToString.Exclude
    private List<Participant> players;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "event_id", nullable = false) 
    private SportEvent event;

    @Column(nullable = false, name = "team_score_a")
    private Integer teamScoreA;

    @Column(nullable = false, name = "team_score_b")
    private Integer teamScoreB;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Modality modality;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status matchStatus;

    @Column(nullable = false)
    private LocalDateTime matchStart;

    @Column(nullable = false)
    private LocalDateTime matchEnd;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Match match = (Match) obj;
        return Objects.equals(id, match.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matchImportance, teamA, teamB, players, event, teamScoreA, teamScoreB, modality, matchStatus, matchStart, matchEnd);
    }

}
