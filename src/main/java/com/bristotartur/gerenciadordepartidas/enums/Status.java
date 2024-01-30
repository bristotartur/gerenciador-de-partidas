package com.bristotartur.gerenciadordepartidas.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {
    SCHEDULED("agendado"),
    IN_PROGRESS("em andamento"),
    ENDED("encerrado");

    public final String name;
}
