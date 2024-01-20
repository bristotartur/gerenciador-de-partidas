package com.bristotartur.gerenciadordepartidas.enums;

import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum TeamName {
    ATOMICA("atômica"),
    MESTRES_DE_OBRAS("mestres de obras"),
    PAPA_LEGUAS("papa-léguas"),
    TWISTER("twister"),
    UNICONTTI("unicontti");

    public final String name;
    private static Map<Integer, TeamName> courseTeamMap = new HashMap<>();

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
     *     <li>Administração: Twister</li>
     *     <li>Alimentos: Atômica</li>
     *     <li>Comércio: Unicontti</li>
     *     <li>Edificações: Mestres de Obras</li>
     *     <li>Informática: Papa-Léguas</li>
     *     <li>Química: Atômica</li>
     *     <li>Ciêcia de Dados: Papa-Léguas</li>
     *     <li>Marketing: Unicontti</li>
     * </ol>
     *
     * @param classNumber O número da turma contendo o número do curso.
     * @return O nome da equipe associada ao número do curso.
     * @throws BadRequestException caso o número do curso seja inválido.
     */
    public static TeamName findCourseTeam(String classNumber) {

        var courseNumber = Integer.parseInt(classNumber.substring(2, 3));

        if (courseNumber > 8 || courseNumber < 1)
            throw new BadRequestException(ExceptionMessages.INVALID_CLASS_NUMBER.message);

        return courseTeamMap.get(courseNumber);
    }

}
