package com.bristotartur.gerenciadordepartidas.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ExceptionMessages {
    TEAM_NOT_FOUND("Equipe não encontrada"),
    BASKETBALL_MATCH_NOT_FOUND("Partida de basquete não encontrada"),
    CHESS_MATCH_NOT_FOUND("Partida de xadrez não encontrada"),
    FOOTBALL_MATCH_NOT_FOUND("Partida de futebol não encontrada"),
    HANDBALL_MATCH_NOT_FOUND("Partida de handebol não encontrada"),
    TABLE_TENNIS_MATCH_NOT_FOUND("Partida de tênis de mesa não encontrada"),
    VOLLEYBALL_MATCH_NOT_FOUND("Partida de vôlei não encontrada"),
    UNSUPPORTED_FOR_GOALS("Esporte não suportado para gols"),
    UNSUPPORTED_FOR_PENALTY_CARDS("Esporte não suportado para cartões"),
    UNSUPPORTED_SPORT("Modalidade esportiva não suportada"),
    INVALID_PATTERN("Padrão inválido");

    public final String message;
}
