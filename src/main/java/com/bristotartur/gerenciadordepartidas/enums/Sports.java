package com.bristotartur.gerenciadordepartidas.enums;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.services.matches.MatchServiceFactory;
import com.bristotartur.gerenciadordepartidas.services.matches.MatchStrategy;
import lombok.RequiredArgsConstructor;

/**
 * Enumeração para as modalidades esportivas suportadas no sistema. Este enum pode ser usado em
 * diversas ocasiões, como buscar partidas por um tipo específico, definir a modalidade esportiva
 * de uma partida, filtrar o acesso de determinadas ações a eventos esportivos, etc.
 *
 * @see Status
 * @see Modality
 * @see MatchStrategy
 * @see MatchServiceFactory
 */
@RequiredArgsConstructor
public enum Sports implements EventType<SportEvent> {
    CHESS("chess"),
    BASKETBALL("basketball"),
    FUTSAL("futsal"),
    HANDBALL("handball"),
    TABLE_TENNIS("table-tennis"),
    VOLLEYBALL("volleyball");

    /**
     * Valor interno das opções deste enum.
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
     *        Sports chess = Sports.findSportLike("CHESS");
     *        Sports basketball = Sports.findSportLike("basketball");
     *        Sports tableTennis = Sports.findSportLike("table-tennis");
     *    }
     * </pre>
     * @param sport Valor correspondente as constantes deste enum.
     * @return A constante correspondente ao valor fornecido.
     * @throws BadRequestException Caso o valor fornecido não corresponda a nenhuma das constantes do enum.
     */
    public static Sports findSportLike(String sport) {

        var formatedSport = sport.replace("-", "_").toUpperCase();

        try {
            return valueOf(formatedSport);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(ExceptionMessages.INVALID_SPORT.message, e);
        }
    }

}
