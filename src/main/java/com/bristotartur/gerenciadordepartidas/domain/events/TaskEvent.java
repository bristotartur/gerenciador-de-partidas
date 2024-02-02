package com.bristotartur.gerenciadordepartidas.domain.events;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.TaskType;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "TASK_EVENT")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder(toBuilder = true)
public class TaskEvent extends Event {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskType type;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "edition_id", nullable = false)
    private Edition edition;

    @ManyToMany
    @JoinTable(
            name = "task_event_participant",
            joinColumns = @JoinColumn(name = "task_event_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<Participant> participants;

    public TaskEvent(Long id, Team firstPlace, Team secondPlace, Team thirdPlace, Team fourthPlace, Team fifthPlace, Status eventStatus, TaskType type, Edition edition, List<Participant> participants) {
        super(id, firstPlace, secondPlace, thirdPlace, fourthPlace, fifthPlace, eventStatus);
        this.type = type;
        this.edition = edition;
        this.participants = participants;
    }

}
