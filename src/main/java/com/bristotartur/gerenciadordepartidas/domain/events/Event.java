package com.bristotartur.gerenciadordepartidas.domain.events;

import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder(toBuilder = true)
public abstract class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Team firstPlace;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Team secondPlace;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Team thirdPlace;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Team fourthPlace;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Team fifthPlace;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status eventStatus;

}
