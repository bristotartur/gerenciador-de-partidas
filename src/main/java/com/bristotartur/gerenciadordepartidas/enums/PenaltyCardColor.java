package com.bristotartur.gerenciadordepartidas.enums;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
public enum PenaltyCardColor {
    YELLOW("yellow"),
    RED("red");

    private final String name;

    PenaltyCardColor (String name) {
        this.name = name;
    }
}
