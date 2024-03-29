package com.bristotartur.gerenciadordepartidas.enums;

import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;

/**
 * Enumeração contendo mensagens de erro para utilizar em exceções como {@link NotFoundException} e
 * {@link BadRequestException}.
 */
@RequiredArgsConstructor
public enum ExceptionMessages {

    // MATCH MESSAGES
    BASKETBALL_MATCH_NOT_FOUND("Partida de basquete não encontrada."),
    CHESS_MATCH_NOT_FOUND("Partida de xadrez não encontrada."),
    FUTSAL_MATCH_NOT_FOUND("Partida de futsal não encontrada."),
    HANDBALL_MATCH_NOT_FOUND("Partida de handebol não encontrada."),
    MATCH_NOT_FOUND("Partida não encontrada."),
    TABLE_TENNIS_MATCH_NOT_FOUND("Partida de tênis de mesa não encontrada."),
    VOLLEYBALL_MATCH_NOT_FOUND("Partida de vôlei não encontrada."),

    CANNOT_UPDATE_MATCH_STATUS("Status de partidas só podem ser atualizadas em eventos sob o status 'IN_PROGRESS.'"),
    CANNOT_HAVE_TWO_MATCHES_IN_PROGRESS("Apenas uma partida de cada vez pode estar sob o status 'IN_PROGRESS'."),
    CANNOT_REGISTER_MATCH("Ainda há partidas agendadas ou em andamento que devem ser encerradas antes de partidas de importância '%s' serem registradas."),
    INVALID_MATCH_IMPORTANCE("O evento não possui partidas necessárias ou não pode mais registrar partidas com importância '%s'."),
    INVALID_MATCH_FOR_EVENT("A partida deve ser de um esporte e modalidade iguais aos de seu evento."),
    INVALID_MATCH_OPERATION("Partidas só podem ser atualizadas ou excluídas enquanto estiverem agendadas."),
    INVALID_TEAMS_FOR_MATCH("Uma partida não pode ter duas equipes iguais."),
    PLAYERS_FROM_SINGLE_TEAM_IN_MATCH("Há apenas jogadores da equipe '%s' na partida."),

    // EVENT MESSAGES
    SPORT_EVENT_NOT_FOUND("Evento esportivo não encontrado."),
    CANNOT_UPDATE_TOTAL_MATCHES("O número de partidas registradas excede o limite do total de partidas."),
    CANNOT_UPDATE_EVENT_STATUS("Eventos só podem ter seu status atualizado caso sua edição estaja com o status 'IN_PROGRESS'."),
    INVALID_MATCH_STATUS_TO_FINISH_EVENT("Eventos esportivos só podem ser encerrados quando todas as partidas estiverem encerradas."),
    INVALID_MATCH_OPERATION_ON_EVENT("Operações relacionadas a partidas não podem ser realizadas em eventos já encerrados."),
    INVALID_SPORT_EVENT_FOR_EDITION("Evento esportivo de tipo '%s' e modalidade '%s' já existe na edição '%d'."),
    INVALID_STATUS_FOR_CREATION("Eventos só podem ser criados com o status 'SCHEDULED'."),
    INVALID_STATUS_TO_DELETE("Eventos só podem ser excluídos se estiverem com o status 'SCHEDULED'."),
    INVALID_STATUS_TO_UPDATE("Apenas eventos com status 'SCHEDULED' e 'OPEN_FOR_EDITIS' podem ser atualizados."),
    INVALID_UPDATE_TO_SPORT_EVENT("Atributos de eventos esportivos só podem ser atualizados com o status 'SCHEDULED'."),
    NO_MATCHES_TO_FINISH("O evento esportivo precisa de '%d' partidas marcadas para encerrar."),
    NO_MATCHES_TO_START("Evento esportivo precisa de '%d' partidas marcadas para começar."),

    // PARTICIPANT MESSAGES
    PARTICIPANT_NOT_FOUND("Participante não encontrado."),
    INVALID_CLASS_NUMBER("Número de turma inválido."),
    INVALID_PARTICIPANT_EXCLUSION_OPERATION("Participantes atrelados a eventos não podem ser excluídos."),
    PARTICIPANT_INVALID_FOR_MATCH("Participante com id '%d' não pertence a nenhuma equipe da partida."),

    // ACTION MESSAGES
    GOAL_NOT_FOUND("Gol não encontrado."),
    PENALTY_CARD_NOT_FOUND("Cartão de penalidade não encontrado."),
    UNSUPPORTED_FOR_GOALS("Esporte não suportado para gols."),
    UNSUPPORTED_FOR_PENALTY_CARDS("Esporte não suportado para cartões."),

    // EXTRAS
    CANNOT_UPDATE_STATUS("Status '%s' só pode ser alterado para '%s'."),
    INVALID_PATTERN("Padrão inválido para '%s'."),
    INVALID_SPORT("Modalidade esportiva não identificada ou não suportada."),
    INVALID_STATUS("Status não identificado ou não suportado."),
    INVALID_TEAM("Equeipe inválida ou não identificada.");

    /**
     * Valor interno das constantes de {@link ExceptionMessages} contendo suas
     * mensagens.
     */
    public final String message;
}
