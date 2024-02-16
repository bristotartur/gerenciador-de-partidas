package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.services.actions.ActionValidator;
import com.bristotartur.gerenciadordepartidas.utils.MatchTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.ParticipantTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class ActionValidatorTest {

    private Match match;
    private final List<Participant> players = new LinkedList<>();

    @BeforeEach
    void setUp() {
        players.addAll(List.of(
                ParticipantTestUtil.createNewParticipant(any(), any(), any()),
                ParticipantTestUtil.createNewParticipant(any(), any(), any()))
        );
        players.forEach(player -> player.setId(getRandomLongId()));
        match = MatchTestUtil.createNewMatch(any(), any(), players, any());
    }

    @Test
    @DisplayName("Should not throw anything when Match is in progress")
    void Should_NotThrowAnyting_When_MatchIsInProgress() {
        match.setMatchStatus(Status.IN_PROGRESS);
        assertDoesNotThrow(() -> ActionValidator.checkMatchForAction(match));
    }

    @Test
    @DisplayName("Should throw BadRequestException when Match is not in progress")
    void Should_ThrowBadRequestException_When_MatchIsNotInProgress() {
        match.setMatchStatus(Status.ENDED);
        assertThrows(BadRequestException.class, () -> ActionValidator.checkMatchForAction(match));
    }

    @Test
    @DisplayName("Should not throw anything when player is included in Match")
    void Should_NotThrowAnyting_When_PlayerIsIncludedInMatch() {
        var player = players.get(0);
        assertDoesNotThrow(() -> ActionValidator.checkPlayerForAction(player, match));
    }

    @Test
    @DisplayName("Should throw BadRequestException when player is not included in Match")
    void Should_ThrowBadRequestException_When_PlayerIsNotIncludedInMatch() {
        var player = ParticipantTestUtil.createNewParticipant(any(), any(), any());
        assertThrows(BadRequestException.class, () -> ActionValidator.checkPlayerForAction(player, match));
    }

}