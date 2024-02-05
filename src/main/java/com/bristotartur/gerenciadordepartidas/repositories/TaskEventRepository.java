package com.bristotartur.gerenciadordepartidas.repositories;

import com.bristotartur.gerenciadordepartidas.domain.events.TaskEvent;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.enums.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskEventRepository extends EventRepository<TaskEvent> {

    @Query("SELECT t FROM TaskEvent t WHERE t.edition.id = :id")
    Page<TaskEvent> findTaskEventsByEditionId(@Param("id") Long editionId, Pageable pageable);

    @Query("SELECT t FROM TaskEvent t WHERE t.type = :type")
    Page<TaskEvent> findTaskEventsBySportType(@Param("type") EventType<TaskEvent> type, Pageable pageable);

    @Query("SELECT p FROM Participant p JOIN p.taskEvents t WHERE t.id = :id")
    Page<Participant> findParticipantsFromTaskEvent(@Param("id") Long taskEventId, Pageable pageable);

}
