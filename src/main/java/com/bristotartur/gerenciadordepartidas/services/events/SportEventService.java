package com.bristotartur.gerenciadordepartidas.services.events;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.input.SportEventDto;
import com.bristotartur.gerenciadordepartidas.dtos.input.TransferableEventData;
import com.bristotartur.gerenciadordepartidas.enums.EventType;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.SportEventMapper;
import com.bristotartur.gerenciadordepartidas.repositories.SportEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe de serviços responsável por fornecer operações CRUD para entidades do tipo {@link SportEvent}.
 * A criação e gerenciamento de eventos esportivos é um processo que deve seguir diversas validações para
 * assegurar uma melhor confiabilidade e segurança de que dados não sejam perdidos e nem danificados durante
 * operações envolvento estes eventos. A realização das vaidações é feita por meio do auxílio da classe
 * utilitária {@link SportEventValidator}, que fornece métodos para seguir os protocolos necessários para o
 * gerenciamento dos eventos esportivos.
 *
 * @see SportEventRepository
 * @see Match
 * @see Participant
 * @see EditionService
 * @see SportEventMapper
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SportEventService implements EventStrategy<SportEvent> {

    private final SportEventRepository sportEventRepository;
    private final EditionService editionService;
    private final SportEventMapper sportEventMapper;

    @Override
    public Page<SportEvent> findAllEvents(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var events = sportEventRepository.findAll(pageable);

        log.info("SportEvent page of number '{}' and size '{}' was returned.", number, size);
        return events;
    }

    @Override
    public Page<SportEvent> findAllEventsFromEdition(Long editionId, Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();

        editionService.findEditionById(editionId);
        var events = sportEventRepository.findSportEventsByEditionId(editionId, pageable);

        log.info("SportEvent page of number '{}' and size '{}' from Edition '{}' was returned.", number, size, editionId);
        return events;
    }

    @Override
    public Page<SportEvent> findAllEventsOfType(EventType<SportEvent> type, Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var events = sportEventRepository.findSportEventsBySportType(type, pageable);

        log.info("SportEvent page of number '{}' and size '{}' of type '{}' was returned.", number, size, type);
        return events;
    }

    @Override
    public Page<Participant> findParticipantsFromEvent(Long id, Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();

        this.findEventById(id);
        var events = sportEventRepository.findParticipantsFromSportEvent(id, pageable);

        log.info("Participant page of number '{}' and size '{}' from SportEvent '{}' was returned.", number, size, id);
        return events;
    }

    @Override
    public SportEvent findEventById(Long id) {

        var event = sportEventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.SPORT_EVENT_NOT_FOUND.message));

        log.info("SportEvent '{}' was found", id);
        return event;
    }

    @Override
    public SportEvent saveEvent(TransferableEventData<SportEvent> eventDto) {

        var dto = (SportEventDto) eventDto;

        if (!dto.eventStatus().equals(Status.SCHEDULED)) {
            throw new BadRequestException(ExceptionMessages.INVALID_STATUS_FOR_CREATION.message);
        }
        var edition = editionService.findEditionById(dto.editionId());
        editionService.checkEditionStatusById(edition.getId());

        var events = sportEventRepository.findSportEventsByEditionId(edition.getId());
        SportEventValidator.checkSportEventForEdition(events, dto);

        var event = sportEventRepository.save(sportEventMapper.toNewSportEvent(dto, edition));

        log.info("SportEvent '{}' was created in Edition '{}'.", event.getId(), edition.getId());
        return event;
    }

    @Override
    public void deleteEventById(Long id) {

        var event = this.findEventById(id);

        if (!event.getEventStatus().equals(Status.SCHEDULED)) {
            throw new BadRequestException(ExceptionMessages.INVALID_STATUS_TO_DELETE.message);
        }
        var editionId = event.getEdition().getId();
        sportEventRepository.deleteById(id);

        log.info("SportEvent '{}' from Edition '{}' was deleted.", id, editionId);
    }

    @Override
    public SportEvent replaceEvent(Long id, TransferableEventData<SportEvent> eventDto) {

        var dto = (SportEventDto) eventDto;
        var originalEvent = this.findEventById(id);

        SportEventValidator.checkDtoForUpdate(originalEvent, dto);

        var status = dto.eventStatus();
        var edition = editionService.findEditionById(dto.editionId());
        editionService.checkEditionStatusById(edition.getId());

        if (status.equals(Status.SCHEDULED)) {
            var events = sportEventRepository.findSportEventsByEditionId(edition.getId());
            SportEventValidator.checkSportEventForEdition(events, dto);
        }
        SportEventValidator.checkMatchesToUpdateEvent(originalEvent, dto);
        var updatedEvent = sportEventRepository.save(sportEventMapper.toExistingSportEvent(id, dto, originalEvent, edition));

        log.info("SportEvent '{}' from Edition '{}' was updated.", originalEvent.getId(), edition.getId());
        return updatedEvent;
    }

}
