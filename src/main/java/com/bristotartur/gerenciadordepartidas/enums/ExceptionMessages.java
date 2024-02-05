package com.bristotartur.gerenciadordepartidas.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExceptionMessages {
    BASKETBALL_MATCH_NOT_FOUND("Partida de basquete não encontrada."),
    CHESS_MATCH_NOT_FOUND("Partida de xadrez não encontrada."),
    FUTSAL_MATCH_NOT_FOUND("Partida de futsal não encontrada."),
    GOAL_NOT_FOUND("Gol não encontrado."),
    HANDBALL_MATCH_NOT_FOUND("Partida de handebol não encontrada."),
    MATCH_NOT_FOUND("Partida não encontrada."),
    PARTICIPANT_NOT_FOUND("Participante não encontrado."),
    PENALTY_CARD_NOT_FOUND("Cartão de penalidade não encontrado."),
    SPORT_EVENT_NOT_FOUND("Evento esportivo não encontrado."),
    TABLE_TENNIS_MATCH_NOT_FOUND("Partida de tênis de mesa não encontrada."),
    VOLLEYBALL_MATCH_NOT_FOUND("Partida de vôlei não encontrada."),
    CANNOT_UPDATE_TOTAL_MATCHES("O número de partidas registradas excede o limite do total de partidas."),
    INVALID_CLASS_NUMBER("Número de turma inválido."),
    INVALID_MATCH_STATUS_TO_FINISH_EVENT("Eventos esportivos só pode ser encerrados quando todas as partidas estiverem encerradas."),
    INVALID_PARTICIPANT_EXCLUSION_OPERATION("Participantes atrelados a eventos não podem ser excluídos."),
    INVALID_PATTERN("Padrão inválido para '%s'."),
    INVALID_TEAMS_FOR_MATCH("Uma partida não pode ter duas equipes iguais."),
    INVALID_SPORT_EVENT_FOR_EDITION("Evento esportivo do tipo '%s' e modalidade '%s' já existe na edição '%d'."),
    INVALID_STATUS_FOR_CREATION("Eventos só podem ser criados com o status 'SCHEDULED'."),
    INVALID_STATUS_TO_DELETE("Eventos só podem ser excluídos se estiverem com o status 'SCHEDULED'."),
    INVALID_STATUS_TO_UPDATE("Apenas eventos com status 'SCHEDULED' e 'OPEN_FOR_EDITIS' podem ser atualizados."),
    INVALID_UPDATE_TO_SPORT_EVENT("Atributos de eventos esportivos só podem ser atualizados com o status 'SCHEDULED'."),
    NO_MATCHES_TO_START("Evento esportivo precisa de '%d' partidas marcadas para começar."),
    NO_MATCHES_TO_FINISH("O evento esportivo precisa de '%d' partidas marcadas para encerrar."),
    PARTICIPANT_INVALID_FOR_MATCH("Participante com id '%d' não pertence a nenhuma equipe da partida."),
    UNSUPPORTED_FOR_GOALS("Esporte não suportado para gols."),
    UNSUPPORTED_FOR_PENALTY_CARDS("Esporte não suportado para cartões."),
    UNSUPPORTED_SPORT("Modalidade esportiva não suportada.");

    public final String message;
}
