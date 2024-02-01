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
    private Integer atomicaPontuation;

    @Column(nullable = false)
    private Integer mestresPontuation;

    @Column(nullable = false)
    private Integer papaPontuation;

    @Column(nullable = false)
    private Integer twisterPontuation;

    @Column(nullable = false)
    private Integer uniconttiPontuation;

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
    private List<SportEvent> events;

    @JsonManagedReference
    @OneToMany(mappedBy = "edition", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Participant> participants;

}
