package com.bristotartur.gerenciadordepartidas.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Modality {
    MASCULINE("masculino"),
    FEMININE("feminino"),
    MIXED("misto");

    public final String name;
}
