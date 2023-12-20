package com.bristotartur.gerenciadordepartidas.domain.match.structure;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MatchSport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
