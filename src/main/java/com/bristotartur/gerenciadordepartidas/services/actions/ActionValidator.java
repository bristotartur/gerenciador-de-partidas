package com.bristotartur.gerenciadordepartidas.services.actions;

import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.exceptions.ConflictException;
import com.bristotartur.gerenciadordepartidas.exceptions.UnprocessableEntityException;

public final class ActionValidator {

    private ActionValidator() {
    }
    
    public static void checkMatchForAction(Match match) {

        if (!match.getMatchStatus().equals(Status.IN_PROGRESS)) {
            throw new ConflictException("Operações envolvendo ações só podem ser realizadas em partidas em andamento.");
        }
    }

    public static void checkPlayerForAction(Participant player, Match match) {

        var matchPlayers = match.getPlayers();

        if (!matchPlayers.contains(player)) {
            var message = "O jogador '%d' não pode ser associado a ação pois não está presente na partida '%d'.";
            throw new UnprocessableEntityException(message.formatted(player.getId(), match.getId()));
        }
    }
    
}
