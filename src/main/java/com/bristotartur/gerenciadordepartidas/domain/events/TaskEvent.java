package com.bristotartur.gerenciadordepartidas.domain.events;

import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.TaskType;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    public TaskEvent(Long id, Team firstPlace, Team secondPlace, Team thirdPlace, Team fourthPlace, Team fifthPlace, Status eventStatus, Edition edition) {
        super(id, firstPlace, secondPlace, thirdPlace, fourthPlace, fifthPlace, eventStatus);
        this.edition = edition;
    }

}
