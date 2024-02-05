package com.bristotartur.gerenciadordepartidas.services.events;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.dtos.input.SportEventDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;

import java.util.List;
import java.util.Optional;

public final class SportEventValidator {

    private SportEventValidator() {
    }

    public static void checkSportEventForEdition(List<SportEvent> sportEvents, SportEventDto dto) {

        var type = dto.type();
        Optional<SportEvent> eventOptional = sportEvents.stream()
                .filter(event -> event.getType().equals(type))
                .findFirst();

        if (eventOptional.isEmpty()) return;

        var modality = dto.modality();
        var event = eventOptional.get();

        if (event.getModality().equals(modality)) {
            var message = ExceptionMessages.INVALID_SPORT_EVENT_FOR_EDITION.message;
            var editionId = event.getEdition().getId();

            throw new BadRequestException(message.formatted(type.name(), modality.name(), editionId));
        }
    }

    public static void checkDtoForUpdate(SportEvent originalEvent, SportEventDto dto) {

        Status.checkStatus(originalEvent.getEventStatus(), dto.eventStatus());

        var isTypeDifferent = !originalEvent.getType().equals(dto.type());
        var isModalityDifferent = !originalEvent.getModality().equals(dto.modality());
        var isTotalMatchesDifferent = !originalEvent.getTotalMatches().equals(dto.totalMatches());
        var isEditionDifferent = !originalEvent.getEdition().getId().equals(dto.editionId());

        var areAttributesDifferent = isTypeDifferent || isModalityDifferent || isTotalMatchesDifferent;
        var status = originalEvent.getEventStatus();

        if (!status.equals(Status.SCHEDULED) && (areAttributesDifferent || isEditionDifferent)) {
            throw new BadRequestException(ExceptionMessages.INVALID_UPDATE_TO_SPORT_EVENT.message);
        }
    }

    public static void checkMatchesToUpdateEvent(SportEvent originalEvent, SportEventDto dto) {

        var newStatus = dto.eventStatus();
        var newTotal = dto.totalMatches();

        if (originalEvent.getTotalMatches() != newTotal) {
            checkTotalMatches(originalEvent, newTotal);
        }
        if (newStatus.equals(Status.IN_PROGRESS)) {
            checkMatchesToStartEvent(originalEvent, newTotal);
        }
        if (newStatus.equals(Status.ENDED)) {
            checkMatchesToFinishEvent(originalEvent);
        }
    }

    private static void checkTotalMatches(SportEvent originalEvent, Integer newTotal) {

        var matchQuantity = originalEvent.getMatches().size();

        if (matchQuantity > newTotal - 4) {
            throw new BadRequestException(ExceptionMessages.CANNOT_UPDATE_TOTAL_MATCHES.message);
        }
    }

    private static void checkMatchesToStartEvent(SportEvent originalEvent, Integer totalMatches) {

        var necessaryMatches = totalMatches - 4;
        var hasNoSufficientMatchesToStart = originalEvent.getMatches().size() < necessaryMatches;

        if (hasNoSufficientMatchesToStart) {
            var message = ExceptionMessages.NO_MATCHES_TO_START.message;
            throw new BadRequestException(message.formatted(necessaryMatches));
        }
    }

    private static void checkMatchesToFinishEvent(SportEvent originalEvent) {

        var totalMatches = originalEvent.getTotalMatches();
        var hasNoSufficientMatchesToEnd = originalEvent.getMatches().size() < totalMatches;

        if (hasNoSufficientMatchesToEnd) {
            var message = ExceptionMessages.NO_MATCHES_TO_FINISH.message;
            throw new BadRequestException(message.formatted(totalMatches));
        }
        var matches = originalEvent.getMatches();
        Optional<Status> notEndedStatus = matches.stream()
                .map(Match::getMatchStatus)
                .filter(status -> !status.equals(Status.ENDED))
                .findFirst();

        if (notEndedStatus.isPresent()) {
            throw new BadRequestException(ExceptionMessages.INVALID_MATCH_STATUS_TO_FINISH_EVENT.message);
        }
    }

}
