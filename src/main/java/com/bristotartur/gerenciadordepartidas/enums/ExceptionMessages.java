package com.bristotartur.gerenciadordepartidas.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ExceptionMessages {
    TEAM_NOT_FOUND("Equipe não encontrada"),
    FOOTBALL_MATCH_NOT_FOUND("Partida de futebol não encontrada"),
    HANDBALL_MATCH_NOT_FOUND("Partida de handebol não encontrada"),
    UNSUPPORTED_FOR_GOALS("Esporte não suportado para gols"),
    UNSUPPORTED_FOR_PENALTY_CARDS("Esporte não suportado para cartões");

    public final String message;
}
