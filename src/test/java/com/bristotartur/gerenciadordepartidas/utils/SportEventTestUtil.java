package com.bristotartur.gerenciadordepartidas.utils;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestSportEventDto;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import jakarta.persistence.EntityManager;

public final class SportEventTestUtil {

    private SportEventTestUtil() {
    }

    public static SportEvent createNewSportEvent(Sports type, Modality modality, Edition edition) {

        return SportEvent.builder()
                .type(type)
                .modality(modality)
                .edition(edition)
                .firstPlace(Team.NONE)
                .secondPlace(Team.NONE)
                .thirdPlace(Team.NONE)
                .fourthPlace(Team.NONE)
                .fifthPlace(Team.NONE)
                .build();
    }

    public static SportEvent createNewSportEvent(Sports type, Modality modality, Status status, Integer totalMatches) {

        return SportEvent.builder()
                .type(type)
                .modality(modality)
                .eventStatus(status)
                .totalMatches(totalMatches)
                .firstPlace(Team.NONE)
                .secondPlace(Team.NONE)
                .thirdPlace(Team.NONE)
                .fourthPlace(Team.NONE)
                .fifthPlace(Team.NONE)
                .build();
    }

    public static SportEvent createNewSportEvent(Sports type, Modality modality, Status status, Integer totalMatches, Edition edition) {

        return SportEvent.builder()
                .type(type)
                .modality(modality)
                .eventStatus(status)
                .totalMatches(totalMatches)
                .edition(edition)
                .firstPlace(Team.NONE)
                .secondPlace(Team.NONE)
                .thirdPlace(Team.NONE)
                .fourthPlace(Team.NONE)
                .fifthPlace(Team.NONE)
                .build();
    }

    public static SportEvent createNewSportEvent(Sports type, Modality modality, Status status, Edition edition, EntityManager entityManager) {

        var event = SportEvent.builder()
                .type(type)
                .modality(modality)
                .eventStatus(status)
                .totalMatches(12)
                .edition(edition)
                .firstPlace(Team.NONE)
                .secondPlace(Team.NONE)
                .thirdPlace(Team.NONE)
                .fourthPlace(Team.NONE)
                .fifthPlace(Team.NONE)
                .build();

        entityManager.merge(event);
        return event;
    }

    public static RequestSportEventDto createNewSportEventDto(Sports type, Modality modality, Integer totalMatches, Long editionId) {

        return RequestSportEventDto.builder()
                .type(type)
                .modality(modality)
                .totalMatches(totalMatches)
                .editionId(editionId)
                .build();
    }

}
