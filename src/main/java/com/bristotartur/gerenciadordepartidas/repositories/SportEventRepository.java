package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.enums.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SportEventRepository extends EventRepository<SportEvent> {

    @Query("SELECT s FROM SportEvent s WHERE s.edition.id = :id")
    List<SportEvent> findSportEventsByEditionId(@Param("id") Long editionId);

    @Query("SELECT s FROM SportEvent s WHERE s.edition.id = :id")
    Page<SportEvent> findSportEventsByEditionId(@Param("id") Long editionId, Pageable pageable);

    @Query("SELECT s FROM SportEvent s WHERE s.type = :type")
    Page<SportEvent> findSportEventsBySportType(@Param("type") EventType<SportEvent> type, Pageable pageable);

    @Query("SELECT p FROM Participant p JOIN p.sportEvents s WHERE s.id = :id")
    Page<Participant> findParticipantsFromSportEvent(@Param("id") Long sportEventId, Pageable pageable);

}
