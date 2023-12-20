package com.bristotartur.gerenciadordepartidas.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Sports {
    FOOTBALL("futebol"),
    HANDBALL("handebol"),
    VOLLEYBALL("voleibol"),
    BASKETBALL("basquetebol"),
    TABLE_TENNIS("tênis de mesa"),
    CHESS("xadrez");

    public final String name;
}
