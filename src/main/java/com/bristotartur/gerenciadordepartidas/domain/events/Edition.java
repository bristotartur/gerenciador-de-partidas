package com.bristotartur.gerenciadordepartidas.domain.events;

import com.bristotartur.gerenciadordepartidas.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

}
