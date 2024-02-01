package com.bristotartur.gerenciadordepartidas.enums;

import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.services.matches.MatchServiceFactory;
import com.bristotartur.gerenciadordepartidas.services.matches.MatchStrategy;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

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
public enum Sports implements EventType {
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
     * <p>Busca por uma das opções deste enum com base em seu valor interno. Os valores internos possíveis
     * são: 'chess', 'basketball', 'futsal', 'handball', 'table-tennis' e 'volleyball'.</p> <br>
     *
     * Exemplo de uso:
     * <pre>
     *    {@code
     *        Sports chess = Sports.findSportByValue("chess");
     *        Sports basketball = Sports.findSportByValue("basketball");
     *    }
     * </pre>
     * @param value Valor interno das opçoões deste enum.
     * @return A opção correspondente ao valor fornecido.
     * @throws BadRequestException Caso o valor fornecido não corresponda a nenhuma das opções do enum.
     */
    public static Sports findSportByValue(String value) {

        Optional<Sports> sportsOptional = Arrays.stream(values())
                .sequential()
                .filter(sport -> sport.value.equals(value))
                .findFirst();

        if (sportsOptional.isEmpty())
            throw new BadRequestException(ExceptionMessages.UNSUPPORTED_SPORT.message);

        return sportsOptional.get();
    }

}
