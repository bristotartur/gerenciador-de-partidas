package com.bristotartur.gerenciadordepartidas.services.matches;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.input.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Importance;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;

import java.util.List;
import java.util.Optional;

/**
 * Classe utilitária responsável por fornecer métodos para a validação de instâncias de {@link Match}
 * e {@link MatchDto}, para gerar, criar ou realizar qualquer tipo de manipulação de dados referentes
 * a partidas.
 *
 * @see SportEvent
 * @see Status
 * @see Sports
 * @see Modality
 * @see MatchService
 */
public final class MatchValidator {

    private MatchValidator() {
    }

    /**
     * Verifica se as equipes presentes no DTO da partida não são as mesmas. Caso sejam, uma exceção
     * será lançada.
     *
     * @param matchDto DTO do tipo {@link MatchDto} contendo as equipes da partida.
     * @throws BadRequestException Caso as duas equipes sejam iguais.
     */
    public static void checkTeamsForMatch(MatchDto matchDto) {

        var teamA = matchDto.teamA();
        var teamB = matchDto.teamB();

        if (matchDto.teamA().equals(matchDto.teamB())) {
            throw new BadRequestException(ExceptionMessages.INVALID_TEAMS_FOR_MATCH.message);
        }
    }

    /**
     * <p>Verifica se a partida está apta para o seu evento. Cada instância de {@link SportEvent} possui um
     * tipo ({@link Sports}) e uma {@link Modality}. Uma partida só será válida para um evento se possuírem
     * o mesmo tipo de esporte e modalidade.</p>
     *
     * @param event Instância de {@link SportEvent} associada a partida.
     * @param matchDto DTO do tipo {@link MatchDto} contendo os dados da partida.
     * @throws BadRequestException Caso a partida não esteja apta para o evento.
     *
     * @apiNote Este método não realiza nenhuma validação prévia entre a relação do evento e do DTO,
     * assumindo que estejam relacionados.
     */
    public static void checkMatchForSportEvent(SportEvent event, MatchDto matchDto) {

        var eventType = event.getType();
        var eventModality = event.getModality();

        if (!matchDto.sport().equals(eventType) || !matchDto.modality().equals(eventModality)) {
            throw new BadRequestException(ExceptionMessages.INVALID_MATCH_FOR_EVENT.message);
        }
    }

    /**
     * <p>Verifica a importância de uma partida para um evento. Cada instância de {@link SportEvent} possui um
     * total de partidas permitidas no evento e apenas uma quantidade específica deste total pode ser de
     * cada importância.</p>
     *
     * <p>Os tipos de importância são definidos nas constantes de {@link Importance}, e a quantidade de
     * partidas permitidas para cada importância em um evento são as seguintes:</p>
     *
     * <ul>
     *     <li>NORMAL - Apenas o mínimo total das partidas do evento podem ter um peso normal. O mínimo total
     *     de partidas é a quantidade mínima de partidas necessárias para começar um evento, que é calculado
     *     pegando o total de partidas e subtraindo por 4.</li>
     *     <li>SEMIFINAL - Apenas duas partidas podem ser semifinais, e as partidas deste tipo só poden ser
     *     definidas após as partidas normais serem encerradas.</li>
     *     <li>THIRD_PLACE_PLAYOFF - Apenas uma partida dever ser de disputa pelo terceiro lugar, e
     *     partidas deste tipo podem ser definidas apenas após as semifinais encerrarem.</li>
     *     <li>FINAL - Apenas uma partida deve ser uma final, que deve ser a última partida a ser definida
     *     no evento.</li>
     * </ul>
     *
     * @param event Instância de {@link SportEvent} associada a partida.
     * @param matchDto DTO do tipo {@link MatchDto} contendo os dados da partida.
     * @throws BadRequestException Caso não seja possível adicionar uma partida com uma determinada importância.
     *
     * @apiNote Este método não realiza nenhuma validação prévia entre a relação do evento e do DTO,
     * assumindo que estejam relacionados.
     */
    public static void checKMatchImportance(SportEvent event, MatchDto matchDto) {

        var importance = matchDto.matchImportance();

        checkMatchesForImportance(event.getMatches(), importance);

        var registeredMatches = event.getMatches().size();
        var totalMatches = event.getTotalMatches();

        switch (importance) {
            case NORMAL -> checkEventForNormalMatch(registeredMatches, totalMatches);
            case SEMIFINAL -> checkEventForSemifinalMatch(registeredMatches, totalMatches);
            case THIRD_PLACE_PLAYOFF -> checkEventForThirdPlacePlayoffMatch(registeredMatches, totalMatches);
            case FINAL -> checkEventForFinalMatch(registeredMatches, totalMatches);
        }
    }

    /**
     * Verifica se todas as partidas presentes na listagem passada como parâmetro, e que tenham importância
     * diferente da informada, estão encerradas. Partidas de determianada importância devem ser definidas
     * após partidas de outras importâncias encerrarem. Partidas normais são as primeiras a acontecer,
     * semifinais só podem ser marcadas após partidas normais, dispustas pelo terceiro lugar só podem acontecer
     * após semifinais e normais, e finais só podem ocorrer após todas as demais.
     *
     * @param matches Listagem contendo todas as instâncias de {@link Match} associadas a um evento.
     * @param importance Importância da partida que está sendo analisada.
     * @throws BadRequestException Caso não seja possível registrar uma partida com a importância informada.
     *
     * @apiNote O método não realiza nenhuma validação prévia sobre a relação das partidas presentes na
     * listagem, assumindo que todas estão relacionadas ao mesmo evento. O método também presupõe que
     * a constante de {@link Importance} passada pertença a uma partida válida ou associada ao mesmo evento
     * que as partidas na listagem.
     */
    public static void checkMatchesForImportance(List<Match> matches, Importance importance) {

        Optional<Match> notFinishedMatch = matches.stream()
                .filter(match -> !match.getMatchImportance().equals(importance))
                .filter(match -> !match.getMatchStatus().equals(Status.ENDED))
                .findFirst();

        notFinishedMatch.ifPresent(status -> {
            throw new BadRequestException(
                    ExceptionMessages.CANNOT_REGISTER_MATCH.message.formatted(importance.name())
            );
        });
    }

    /**
     * Verifica se um evento pode registrar partidas normais, baseando-se na quantidade de partidas e no limite
     * permitido no evento.
     *
     * @param registeredMatches Número de partidas registradas no evento.
     * @param totalMatches Total de partidas permitidas no evento.
     * @throws BadRequestException Caso o evento não possa mais registrar partidas normais.
     */
    private static void checkEventForNormalMatch(Integer registeredMatches, Integer totalMatches) {

        var minimumTotal = totalMatches - 4;

        if (registeredMatches >= minimumTotal) {
            throw new BadRequestException(
                    ExceptionMessages.INVALID_MATCH_IMPORTANCE.message.formatted(Importance.NORMAL.name())
            );
        }
    }

    /**
     * Verifica se um evento pode registrar semifinais, baseando-se na quantidade de partidas e no limite
     * permitido no evento.
     *
     * @param registeredMatches Número de partidas registradas no evento.
     * @param totalMatches Total de partidas permitidas no evento.
     * @throws BadRequestException Caso o evento não possua partidas necessárias ou não possa mais registrar semifinais.
     */
    private static void checkEventForSemifinalMatch(Integer registeredMatches, Integer totalMatches) {

        var minimumTotal = totalMatches - 4;
        var limitForSemifinals = totalMatches - 2;

        if (registeredMatches < minimumTotal || registeredMatches >= limitForSemifinals) {
            throw new BadRequestException(
                    ExceptionMessages.INVALID_MATCH_IMPORTANCE.message.formatted(Importance.SEMIFINAL.name())
            );
        }
    }

    /**
     * Verifica se um evento pode registrar disputas por terceiro lugar, baseando-se na quantidade de partidas
     * e no limite permitido no evento.
     *
     * @param registeredMatches Número de partidas registradas no evento.
     * @param totalMatches Total de partidas permitidas no evento.
     * @throws BadRequestException Caso o evento não possua partidas necessárias ou não possa mais registrar
     * disputas por terceiro lugar.
     */
    private static void checkEventForThirdPlacePlayoffMatch(Integer registeredMatches, Integer totalMatches) {

        var limitForThirdPlacePlayoffs = totalMatches - 2;

        if (registeredMatches != limitForThirdPlacePlayoffs) {
            throw new BadRequestException(
                    ExceptionMessages.INVALID_MATCH_IMPORTANCE.message.formatted(Importance.THIRD_PLACE_PLAYOFF.name())
            );
        }
    }

    /**
     * Verifica se um evento pode registrar finais, baseando-se na quantidade de partidas e no limite
     * permitido no evento.
     *
     * @param registeredMatches Número de partidas registradas no evento.
     * @param totalMatches Total de partidas permitidas no evento.
     * @throws BadRequestException Caso o evento não possua partidas necessárias ou não possa mais registrar finais.
     */
    private static void checkEventForFinalMatch(Integer registeredMatches, Integer totalMatches) {

        var limitForFinals = totalMatches - 1;

        if (registeredMatches != limitForFinals) {
            throw new BadRequestException(
                    ExceptionMessages.INVALID_MATCH_IMPORTANCE.message.formatted(Importance.FINAL.name())
            );
        }
    }

    /**
     * Verifica o {@link Status} de uma partida. Cada evento esportivo pode ter apenas uma partida em andamento
     * e o status de uma partida só pode ser alterado se este evento estiver em andamento.
     *
     * @param event Instância de {@link SportEvent} relacionada a partida.
     * @param newStatus Status da partida analisada.
     * @throws BadRequestException Caso haja uma outra partida em andamento ou o evento não esteja em andamento.
     *
     * @apiNote O método não realiza nenhuma validação sobre o status passado, assumindo que esteja relacionado
     * a alguma partida. O método também pressupõe que a partida na qual o status está relacionado também esteja
     * relacionada com o evento informado.
     */
    public static void checkMatchStatus(SportEvent event, Status newStatus) {

        var eventStatus = event.getEventStatus();

        if (!eventStatus.equals(Status.IN_PROGRESS)) {
            throw new BadRequestException(ExceptionMessages.CANNOT_UPDATE_MATCH_STATUS.message);
        }
        if (!newStatus.equals(Status.IN_PROGRESS)) return;

        Optional<Status> inProgress = event.getMatches().stream()
                .map(Match::getMatchStatus)
                .filter(status -> status.equals(Status.IN_PROGRESS))
                .findFirst();

        inProgress.ifPresent(status -> {
            throw new BadRequestException(ExceptionMessages.CANNOT_HAVE_TWO_MATCHES_IN_PROGRESS.message);
        });
    }

    /**
     * Verifica se os jogadores presentes na listagem passada como parâmetro estão aptos para participar
     * da partida. Para um jogador participar de uma partida, ele deve obrigatóriamente pertencer a uma
     * das equipes dela. Além disso, o método também verifica se não há jogadores de apenas uma das equipes
     * registrados na partida.
     *
     * @param players Listagem dos jogadores pertencentes a partida.
     * @param matchDto DTO do tipo {@link MatchDto} contendo os dados da partida.
     * @throws BadRequestException Caso haja jogadores de apenas uma equipe ou jogadores de outras equipes
     * na partida.
     *
     * @apiNote O método não realiza nenhuma validação prévia sobre a relação entre a lista de jogadores e os IDs
     * do DTO, assumindo que os jogadores já estão relacionados com os IDs do DTO.
     */
    public static void checkPlayersForMatch(List<Participant> players, MatchDto matchDto) {

        checkPlayersWithDifferentTeams(players, matchDto);

        var teamA = matchDto.teamA();
        var teamB = matchDto.teamB();

        var playersTeams = players.stream()
                .map(Participant::getTeam)
                .toList();

        if (playersTeams.contains(teamA) && !playersTeams.contains(teamB)) {
            throw new BadRequestException(ExceptionMessages.PLAYERS_FROM_SINGLE_TEAM_IN_MATCH.message.formatted(teamA));
        }
        if (playersTeams.contains(teamB) && !playersTeams.contains(teamA)) {
            throw new BadRequestException(ExceptionMessages.PLAYERS_FROM_SINGLE_TEAM_IN_MATCH.message.formatted(teamB));
        }
    }

    /**
     * Verifica se os jogadores passados pertencem a alguma das equipes do DTO. Caso algum dos jogadores
     * pertença a uma equipe diferente das do DTO, o jogador se qualificará como inválido para a partida
     * e uma exceção será lançada.
     *
     * @param players Lista do tipo {@link Participant} contendo os jogadores da partida.
     * @param matchDto DTO do tipo {@link MatchDto} contendo os IDs das equipes presentes na partida.
     * @throws BadRequestException Caso algum jogador não pertença a alguma das equipes.
     */
    private static void checkPlayersWithDifferentTeams(List<Participant> players, MatchDto matchDto) {

        var teamA = matchDto.teamA();
        var teamB = matchDto.teamB();

        Optional<Participant> invalidPlayer = players.stream()
                .filter(player -> !player.getTeam().equals(teamA)
                               && !player.getTeam().equals(teamB))
                .findFirst();

        invalidPlayer.ifPresent(player -> {
            throw new BadRequestException(
                    ExceptionMessages.PARTICIPANT_INVALID_FOR_MATCH.message.formatted(player.getId())
            );
        });
    }

}
