package com.bristotartur.gerenciadordepartidas.enums;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionMessages {
    TEAM_NOT_FOUND("Equipe não encontrada."),
    INVALID_TEAMS_FOR_MATCH("Uma partida não pode ter duas equipes iguais."),
    PARTICIPANT_NOT_FOUND("Participante não encontrado."),
    PARTICIPANT_INVALID_FOR_MATCH("Participante com id '%d' não pertence a nenhuma equipe da partida."),
    GOAL_NOT_FOUND("Gol não encontrado."),
    PENALTY_CARD_NOT_FOUND("Cartão de penalidade não encontrado."),
    MATCH_NOT_FOUND("Partida não encontrada."),
    BASKETBALL_MATCH_NOT_FOUND("Partida de basquete não encontrada."),
    CHESS_MATCH_NOT_FOUND("Partida de xadrez não encontrada."),
    FUTSAL_MATCH_NOT_FOUND("Partida de futsal não encontrada."),
    HANDBALL_MATCH_NOT_FOUND("Partida de handebol não encontrada."),
    TABLE_TENNIS_MATCH_NOT_FOUND("Partida de tênis de mesa não encontrada."),
    VOLLEYBALL_MATCH_NOT_FOUND("Partida de vôlei não encontrada."),
    UNSUPPORTED_FOR_GOALS("Esporte não suportado para gols."),
    UNSUPPORTED_FOR_PENALTY_CARDS("Esporte não suportado para cartões."),
    UNSUPPORTED_SPORT("Modalidade esportiva não suportada."),
    INVALID_PATTERN("Padrão inválido para '%s'.");

    public final String message;
}
