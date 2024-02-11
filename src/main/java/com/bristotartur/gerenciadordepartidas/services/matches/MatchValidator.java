package com.bristotartur.gerenciadordepartidas.services.matches;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.input.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Importance;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;

import java.util.List;
import java.util.Optional;

public final class MatchValidator {

    private MatchValidator() {
    }

    public static void checkTeamsForMatch(MatchDto matchDto) {

        var teamA = matchDto.teamA();
        var teamB = matchDto.teamB();

        if (matchDto.teamA().equals(matchDto.teamB())) {
            throw new BadRequestException(ExceptionMessages.INVALID_TEAMS_FOR_MATCH.message);
        }
    }

    public static void checKMatchImportance(SportEvent sportEvent, MatchDto matchDto) {

        var importance = matchDto.matchImportance();

        checkMatchesForImportance(sportEvent.getMatches(), importance);

        var registeredMatches = sportEvent.getMatches().size();
        var totalMatches = sportEvent.getTotalMatches();

        switch (importance) {
            case NORMAL -> checkEventForNormalMatch(registeredMatches, totalMatches);
            case SEMIFINAL -> checkEventForSemifinalMatch(registeredMatches, totalMatches);
            case THIRD_PLACE_PLAYOFF -> checkEventForThirdPlacePlayofflMatch(registeredMatches, totalMatches);
            case FINAL -> checkEventForFinalMatch(registeredMatches, totalMatches);
        }
    }

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

    private static void checkEventForNormalMatch(Integer registeredMatches, Integer totalMatches) {

        if (registeredMatches >= totalMatches - 4) {
            throw new BadRequestException(
                    ExceptionMessages.INVALID_MATCH_IMPORTANCE.message.formatted(Importance.NORMAL.name())
            );
        }
    }

    private static void checkEventForSemifinalMatch(Integer registeredMatches, Integer totalMatches) {

        if (registeredMatches < totalMatches - 4 || registeredMatches >= totalMatches - 2) {
            throw new BadRequestException(
                    ExceptionMessages.INVALID_MATCH_IMPORTANCE.message.formatted(Importance.SEMIFINAL.name())
            );
        }
    }

    private static void checkEventForThirdPlacePlayofflMatch(Integer registeredMatches, Integer totalMatches) {

        if (registeredMatches != totalMatches - 2) {
            throw new BadRequestException(
                    ExceptionMessages.INVALID_MATCH_IMPORTANCE.message.formatted(Importance.THIRD_PLACE_PLAYOFF.name())
            );
        }
    }

    private static void checkEventForFinalMatch(Integer registeredMatches, Integer totalMatches) {

        if (registeredMatches != totalMatches - 1) {
            throw new BadRequestException(
                    ExceptionMessages.INVALID_MATCH_IMPORTANCE.message.formatted(Importance.FINAL.name())
            );
        }
    }

    public static void checkMatchStatus(SportEvent event, Status newStatus) {

        var eventStatus = event.getEventStatus();

        if (!eventStatus.equals(Status.IN_PROGRESS)) {
            throw new BadRequestException(ExceptionMessages.CANNOT_UPDATE_MATCH_STATUS.message);
        }
        Optional<Status> inProgress = event.getMatches().stream()
                .map(Match::getMatchStatus)
                .filter(status -> status.equals(Status.IN_PROGRESS))
                .findFirst();

        inProgress.ifPresent(status -> {
            throw new BadRequestException(ExceptionMessages.CANNOT_HAVE_TWO_MATCHES_IN_PROGRESS.message);
        });
    }

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
     * <p>Verifica se os jogadores passados pertencem a alguma das equipes do DTO. Caso algum dos jogadores
     * pertença a uma equipe diferente das do DTO, o jogador se qualificará como inválido para a partida
     * e uma exceção será lançada.</p>
     *
     * <p>O método não realiza nenhuma validação prévia sobre a relação entre a lista de jogadores e os IDs
     * do DTO, assumindo que os jogadores já estão relacionados com os IDs do DTO.</p>
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
