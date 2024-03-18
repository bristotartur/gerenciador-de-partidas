package com.bristotartur.gerenciadordepartidas.enums;

import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeração contendo os nomes das equipes. Este enum possui métodos para resgatar nomes de equipes
 * com base em critérios específicos, como curso.
 */
@RequiredArgsConstructor
public enum Team {
    ATOMICA("atomica"),
    MESTRES_DE_OBRAS("mestres-de-obras"),
    PAPA_LEGUAS("papa-leguas"),
    TWISTER("twister"),
    UNICONTTI("unicontti"),
    NONE("none");

    /**
     * Valor interno das constantes deste enum.
     */
    public final String value;

    private static final Map<Integer, Team> courseTeamMap = new HashMap<>();

    static {
        courseTeamMap.put(1, TWISTER);
        courseTeamMap.put(2, ATOMICA);
        courseTeamMap.put(3, UNICONTTI);
        courseTeamMap.put(4, MESTRES_DE_OBRAS);
        courseTeamMap.put(5, PAPA_LEGUAS);
        courseTeamMap.put(6, ATOMICA);
        courseTeamMap.put(7, PAPA_LEGUAS);
        courseTeamMap.put(8, UNICONTTI);
    }

    /**
     * <p>Procura o nome da equipe associada ao número da turma. Cada turma possui o número de seu curso,
     * e cada curso pertence somente a uma equipe. O número que representa cada curso é o primeiro número
     * após o hífen. Por exemplo, para a turma 2-53, o número do curso é o 5, que corresponde ao curso
     * de informática.</p>
     *
     * <p>As equipes relacionadas a cada curso são, respectivamente: </p>
     * <ol>
     *     <li>Administração: Twister.</li>
     *     <li>Alimentos: Atômica.</li>
     *     <li>Comércio: Unicontti.</li>
     *     <li>Edificações: Mestres de Obras.</li>
     *     <li>Informática: Papa-Léguas.</li>
     *     <li>Química: Atômica.</li>
     *     <li>Ciêcia de Dados: Papa-Léguas.</li>
     *     <li>Marketing: Unicontti.</li>
     * </ol>
     *
     * @param classNumber O número da turma contendo o número do curso.
     * @return O nome da equipe associada ao número do curso.
     * @throws BadRequestException caso o número do curso seja inválido.
     */
    public static Team findCourseTeam(String classNumber) {

        var courseNumber = Integer.parseInt(classNumber.substring(2, 3));

        if (courseNumber > 8 || courseNumber < 1)
            throw new BadRequestException(ExceptionMessages.INVALID_CLASS_NUMBER.message);

        return courseTeamMap.get(courseNumber);
    }

    /**
     * <p>Busca por uma das constantes deste enum com base em seu valor, sendo que, no contexto deste
     * método, o valor da constante equivale ao seu nome, e não seu valor interno. Caso o valor passado
     * esteja em letras minúsculas ou tenha um espaçamento feito com "-", o método ajustará este valor
     * para o formato correto, passando todos os caracteres para caixa alta e substituindo os espaçamentos
     * feitos com '-' para "_". Abreviações como 'mestres' e 'papa' também são suportadas.</p> <br>
     *
     * Exemplo de uso:
     * <pre>
     *    {@code
     *        Team atomica = Team.findTeamLike("ATOMICA");
     *        Team papaLeguas = Team.findTeamLike("papa-leguas");
     *        Team mestresDeObras = Team.findTeamLike("MESTRES_DE_OBRAS");
     *        Team mestres = Team.findTeamLike("mestres");
     *    }
     * </pre>
     * @param team Valor correspondente as constantes deste enum.
     * @return A constante correspondente ao valor fornecido.
     * @throws BadRequestException Caso o valor fornecido não corresponda a nenhuma das constantes do enum.
     */
    public static Team findTeamLike(String team) {

        if (team.compareToIgnoreCase("mestres") == 0) return MESTRES_DE_OBRAS;
        if (team.compareToIgnoreCase("papa") == 0) return PAPA_LEGUAS;

        try {
            var formattedTeam = team.replace("-", "_").toUpperCase();
            return valueOf(formattedTeam);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(ExceptionMessages.INVALID_TEAM.message, e);
        }
    }

}
