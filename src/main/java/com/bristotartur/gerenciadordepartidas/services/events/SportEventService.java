package com.bristotartur.gerenciadordepartidas.services.events;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestSportEventDto;
import com.bristotartur.gerenciadordepartidas.dtos.request.TransferableEventData;
import com.bristotartur.gerenciadordepartidas.enums.EventType;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.exceptions.ConflictException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.exceptions.UnprocessableEntityException;
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

    /**
     * @throws NotFoundException Caso nenhuma edição correspondente ao ID seja encontrada.
     */
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

    /**
     * @throws NotFoundException Caso o nenhum evento esportivo correspondente ao ID seja encontrado.
     */
    @Override
    public Page<Participant> findParticipantsFromEvent(Long id, Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();

        this.findEventById(id);
        var events = sportEventRepository.findParticipantsFromSportEvent(id, pageable);

        log.info("Participant page of number '{}' and size '{}' from SportEvent '{}' was returned.", number, size, id);
        return events;
    }

    /**
    * @throws NotFoundException Caso o nenhum evento esportivo correspondente ao ID seja encontrado.
    */
    @Override
    public SportEvent findEventById(Long id) {

        var event = sportEventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.SPORT_EVENT_NOT_FOUND.message));

        log.info("SportEvent '{}' was found.", id);
        return event;
    }

    /**
     * @throws NotFoundException Caso o nenhum evento correspondente ao ID seja encontrado.
     * @throws UnprocessableEntityException Caso o evento esteja encerrado.
     */
    @Override
    public SportEvent findEventAndCheckStatus(Long id) {

        var event = this.findEventById(id);
        var status = event.getEventStatus();

        if (status.equals(Status.ENDED)) {
            throw new UnprocessableEntityException(ExceptionMessages.INVALID_MATCH_OPERATION_ON_EVENT.message);
        }
        return event;
    }

    /**
     * @throws NotFoundException Caso nenhuma edição correspondente ao ID fornecido no DTO seja encontrada.
     * @throws ConflictException Caso já exista um evento com o mesmo tipo e modalidade passados pelo DTO.
     */
    @Override
    public SportEvent saveEvent(TransferableEventData<SportEvent> eventDto) {

        var dto = (RequestSportEventDto) eventDto;
        var edition = editionService.findEditionById(dto.editionId());
        editionService.checkEditionStatusById(edition.getId());

        var events = sportEventRepository.findSportEventsByEditionId(edition.getId());
        SportEventValidator.checkSportEventForEdition(events, dto);

        var event = sportEventRepository.save(sportEventMapper.toNewSportEvent(dto, edition));

        log.info("SportEvent '{}' was created in Edition '{}'.", event.getId(), edition.getId());
        return event;
    }

    /**
     * @throws NotFoundException Caso o nenhum evento correspondente ao ID seja encontrado.
     * @throws UnprocessableEntityException Caso o evento não esteja agendado.
     */
    @Override
    public void deleteEventById(Long id) {

        var event = this.findEventById(id);

        if (!event.getEventStatus().equals(Status.SCHEDULED)) {
            throw new UnprocessableEntityException(ExceptionMessages.INVALID_STATUS_TO_DELETE.message);
        }
        var editionId = event.getEdition().getId();
        sportEventRepository.deleteById(id);

        log.info("SportEvent '{}' from Edition '{}' was deleted.", id, editionId);
    }

    /**
     * @throws NotFoundException Caso o nenhum evento correspondente ao ID seja encontrado.
     * @throws ConflictException Caso já exista um evento com o mesmo tipo e modalidade passados pelo DTO.
     * @throws UnprocessableEntityException Caso o evento esportivo não esteja apto para ser atualizado ou a edição
     * associada ao evento esteja encerrada.
     */
    @Override
    public SportEvent replaceEvent(Long id, TransferableEventData<SportEvent> eventDto) {

        var dto = (RequestSportEventDto) eventDto;
        var originalEvent = this.findEventById(id);

        SportEventValidator.checkSportEventForUpdate(originalEvent);

        var edition = editionService.findEditionById(dto.editionId());
        editionService.checkEditionStatusById(edition.getId());

        var events = sportEventRepository.findSportEventsByEditionId(edition.getId());
        if (!originalEvent.getType().equals(dto.type()) || !originalEvent.getModality().equals(dto.modality())) {
            SportEventValidator.checkSportEventForEdition(events, dto);
        }
        SportEventValidator.checkNewTotalMatchesForSportEvent(originalEvent, dto.totalMatches());
        var updatedEvent = sportEventRepository.save(sportEventMapper.toExistingSportEvent(id, dto, originalEvent, edition));

        log.info("SportEvent '{}' from Edition '{}' was updated.", originalEvent.getId(), edition.getId());
        return updatedEvent;
    }

    /**
     * @throws NotFoundException Caso o nenhum evento correspondente ao ID seja encontrado.
     * @throws UnprocessableEntityException Caso o evento não esteja apto para ter seu status atualizado.
     */
    @Override
    public SportEvent updateEventStatus(Long id, Status newStatus) {

        var event = this.findEventById(id);
        var originalStatus = event.getEventStatus();
        Status.checkStatus(originalStatus, newStatus);

        if (!originalStatus.equals(newStatus)) {
            var editionStatus = event.getEdition().getEditionStatus();
            SportEventValidator.checkSportEventToUpdateStatus(event, newStatus, editionStatus);
        }
        event.setEventStatus(newStatus);
        var updatedEvent = sportEventRepository.save(event);

        var editionId = event.getEdition().getId();
        log.info("SportEvent '{}' from Edition '{}' had the status updated to '{}'.", id, editionId, newStatus);
        return event;
    }

}
