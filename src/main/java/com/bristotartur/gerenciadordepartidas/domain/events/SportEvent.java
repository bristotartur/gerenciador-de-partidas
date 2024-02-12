package com.bristotartur.gerenciadordepartidas.domain.events;

import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "SPORT_EVENT")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder(toBuilder = true)
public class SportEvent extends Event {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sports type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Modality modality;

    @Column(nullable = false)
    private Integer totalMatches;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "edition_id", nullable = false)
    @ToString.Exclude
    private Edition edition;

    @JsonManagedReference
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Match> matches;

    @ManyToMany
    @JoinTable(
            name = "sport_event_participant",
            joinColumns = @JoinColumn(name = "sport_event_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    @ToString.Exclude
    private List<Participant> participants;

    public SportEvent(Long id, Team firstPlace, Team secondPlace, Team thirdPlace, Team fourthPlace, Team fifthPlace, Status eventStatus, Sports type, Modality modality, Integer totalMatches, Edition edition, List<Match> matches, List<Participant> participants) {
        super(id, firstPlace, secondPlace, thirdPlace, fourthPlace, fifthPlace, eventStatus);
        this.type = type;
        this.modality = modality;
        this.totalMatches = totalMatches;
        this.edition = edition;
        this.matches = matches;
        this.participants = participants;
    }

}
