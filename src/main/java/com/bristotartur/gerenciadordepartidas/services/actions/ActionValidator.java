package com.bristotartur.gerenciadordepartidas.services.actions;

import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;

public final class ActionValidator {

    private ActionValidator() {
    }
    
    public static void checkMatchForAction(Match match) {

        if (!match.getMatchStatus().equals(Status.IN_PROGRESS)) {
            throw new BadRequestException("Operações envolvendo ações só podem ser realizadas em partidas em andamento.");
        }
    }

    public static void checkPlayerForAction(Participant player, Match match) {

        var matchPlayers = match.getPlayers();

        if (!matchPlayers.contains(player)) {
            var message = "O jogador '%d' não pode ser associado a ação pois não está presente na partida '%d'.";
            throw new BadRequestException(message.formatted(player.getId(), match.getId()));
        }
    }
    
}
