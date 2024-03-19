package com.bristotartur.gerenciadordepartidas.domain.events;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "EDITION")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Edition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer atomicaScore;

    @Column(nullable = false)
    private Integer mestresScore;

    @Column(nullable = false)
    private Integer papaScore;

    @Column(nullable = false)
    private Integer twisterScore;

    @Column(nullable = false)
    private Integer uniconttiScore;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status editionStatus;

    @Column(nullable = false)
    private LocalDate opening;

    @Column(nullable = false)
    private LocalDate closure;

    @JsonManagedReference
    @OneToMany(mappedBy = "edition", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<SportEvent> sportEvents;

    @JsonManagedReference
    @OneToMany(mappedBy = "edition", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<TaskEvent> taskEvents;

    @JsonManagedReference
    @OneToMany(mappedBy = "edition", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Participant> participants;

}
