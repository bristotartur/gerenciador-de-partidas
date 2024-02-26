package com.bristotartur.gerenciadordepartidas.enums;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.events.Event;
import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.events.TaskEvent;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;

/**
 * <p>Enumeração para os Status disponíveis no sistema. Este enum é utilizado em diversas entidades,
 * como {@link Edition} e classes filhas de {@link Event}, para representar o estado no qual elas se
 * encontram, ou seja, se estão encerradas, se ainda não começaram, se estão em andamento ou se podem
 * ser editadas.</p>
 *
 * <p>Este enum também fornece métodos para lidar com ocasiões específicas relacionadas a Status,
 * como procurar por um Status específico ou verificar se um determinado Status pode ser alterado.</p>
 *
 * @see SportEvent
 * @see TaskEvent
 * @see Match
 * @see Sports
 * @see Modality
 */
@RequiredArgsConstructor
public enum Status {
    SCHEDULED("Agendado"),
    IN_PROGRESS("Em andamento"),
    ENDED("Encerrado"),
    OPEN_FOR_EDITS("Aberto para edições");

    /**
     * Valor interno das constantes deste enum.
     */
    public final String value;

    /**
     * <p>Busca por uma das constantes deste enum com base em seu valor, sendo que, no contexto deste
     * método, o valor da constante equivale ao seu nome, e não seu valor interno. Caso o valor passado
     * esteja em letras minúsculas ou tenha um espaçamento feito com "-", o método ajustará este valor
     * para o formato correto, passando todos os caracteres para caixa alta e substituindo os espaçamentos
     * feitos com '-' para "_".</p> <br>
     *
     * Exemplo de uso:
     * <pre>
     *    {@code
     *        Status scheduled = Status.findStatusLike("SCHEDULED");
     *        Status inProgress = Status.findStatusLike("in-progress");
     *        Status openForEdits = Status.findStatusLike("OPEN_FOR_EDITS");
     *    }
     * </pre>
     * @param status Valor correspondente as constantes deste enum.
     * @return A constante correspondente ao valor fornecido.
     * @throws BadRequestException Caso o valor fornecido não corresponda a nenhuma das constantes do enum.
     */
    public static Status findStatusLike(String status) {

        var formatedStatus = status.replace("-", "_").toUpperCase();

        try {
            return valueOf(formatedStatus);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(ExceptionMessages.INVALID_STATUS.message, e);
        }
    }

    /**
     * <p>Verifica se um determinado Status pode ser atualizado para um outro Status. Cada Status pode ser
     * atualizado apenas para um status específico.</p> <br>
     *
     * Atualizações possíveis para cada Status:
     * <ul>
     *     <li>SCHEDULED: Pode ser alterado apenas para 'IN_PROGRESS'.</li>
     *     <li>IN_PROGRESS: Pode ser alterado apenas para 'ENDED'.</li>
     *     <li>ENDED: Pode ser alterado apenas para 'OPEN_FOR_EDITS'.</li>
     *     <li>OPEN_FOR_EDITS: Pode ser alterado apenas para 'IN_PROGRESS'.</li>
     * </ul>
     *
     * <p>Caso o Status original e o novo Status forem o mesmo, nada acontecerá.</p>
     *
     * @param originalStatus Status que será atualizado.
     * @param newStatus O nova Status, que será analizado para verificar se está apto para atualizar
     *                  o Status original.
     * @throws BadRequestException Caso o novo Status não esteja apto para atualizar o antigo Status.
     */
    public static void checkStatus(Status originalStatus, Status newStatus) {

        if (originalStatus.equals(newStatus)) return;

        var message = ExceptionMessages.CANNOT_UPDATE_STATUS.message;

        switch (originalStatus) {
            case SCHEDULED -> {
                if (!newStatus.equals(IN_PROGRESS)) throw new BadRequestException(message.formatted(SCHEDULED, IN_PROGRESS));
            }
            case IN_PROGRESS -> {
                if (!newStatus.equals(ENDED)) throw new BadRequestException(message.formatted(IN_PROGRESS, ENDED));
            }
            case ENDED -> {
                if (!newStatus.equals(OPEN_FOR_EDITS)) throw new BadRequestException(message.formatted(ENDED, OPEN_FOR_EDITS));
            }
            case OPEN_FOR_EDITS -> {
                if (!newStatus.equals(ENDED)) throw new BadRequestException(message.formatted(OPEN_FOR_EDITS, ENDED));
            }
        }
    }

}
