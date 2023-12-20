package com.bristotartur.gerenciadordepartidas.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MatchStatus {
    SCHEDULED("agendado"),
    IN_PROGRESS("em andamento"),
    ENDED("encerrado");

    public final String name;
}
