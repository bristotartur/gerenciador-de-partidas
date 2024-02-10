package com.bristotartur.gerenciadordepartidas.services.events;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.events.TaskEvent;
import com.bristotartur.gerenciadordepartidas.domain.events.Event;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.exposing.ExposableEventData;
import com.bristotartur.gerenciadordepartidas.dtos.input.TransferableEventData;
import com.bristotartur.gerenciadordepartidas.enums.EventType;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Estratégia para classes de serviço relacionadas a especializações de {@link Event}.
 * Esta interface define métodos CRUD comuns a estas entidades, como buscar eventos por tipo,
 * por ID, criar novos eventos, atualizar eventos, etc.
 *
 * @see SportEvent
 * @see TaskEvent
 * @see EventType
 * @see TransferableEventData
 *
 * @param <T> Enspecialização de {@link Event} na qual a implementação irá tratar.
 */
public interface EventStrategy<T extends Event> {

    /**
     * Busca por todas as instâncias da especialização de {@link Event} de forma paginada.
     *
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Uma {@link Page} contendo todas as entidades da especialização.
     */
    Page<T> findAllEvents(Pageable pageable);

    /**
     * Busca por todas as instâncias da especialização de {@link Event} relacionadas a uma
     * determinada instância de {@link Edition}.
     *
     * @param editionId Identificador único da instância de {@link Edition}.
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Uma {@link Page} contendo todas as entidades da especialização relacionadas a edição especificada.
     */
    Page<T> findAllEventsFromEdition(Long editionId, Pageable pageable);

    /**
     * Busca por todas as instâncias da especialização de {@link Event} por um determinado {@link EventType}.
     *
     * @param type Tipo do evento.
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Uma {@link Page} contendo todas as entidades da especialização do tipo especificado.
     */
    Page<T> findAllEventsOfType(EventType<T> type, Pageable pageable);

    /**
     * Busca por todos os participantes associados a uma instância da especialização de {@link Event}.
     *
     * @param id Identificador único do evento.
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Uma {@link Page} contendo todas os participantes associados ao evento.
     */
    Page<Participant> findParticipantsFromEvent(Long id, Pageable pageable);

    /**
     * Busca por uma instância da especialização de {@link Event} pelo seu ID.
     *
     * @param id Identificador único do evento.
     * @return A instância de {@link Event} correspondente ao ID.
     */
    T findEventById(Long id);

    /**
     * Busca por uma instância da especialização de {@link Event} pelo seu ID e verifica o seu {@link Status}.
     *
     * @param id Identificador único do evento.
     * @return A instância de {@link Event} correspondente ao ID.
     */
    T findEventAndCheckStatus(Long id);

    /**
     * Gera um nova instância de uma implementação de {@link ExposableEventData} a partir dos dados de
     * um evento.
     *
     * @param event Evento utilizado para gerar o DTO.
     * @return Um DTO que implementaa interface {@link ExposableEventData}.
     */
    ExposableEventData<T> createExposingEventDto(T event);

    /**
     * Salva um evento no sistema com base nos dados fornecidos no DTO.
     *
     * @param eventDto DTO contendo os dados do novo evento.
     * @return O novo evento salvo no banco de dados.
     */
    T saveEvent(TransferableEventData<T> eventDto);

    /**
     * Exclui um evento no sistema com base no seu ID.
     *
     * @param id Identificador único do evento.
     */
    void deleteEventById(Long id);

    /**
     * Atualiza um evento no sistema com base em seu ID e nos dados fornecidos no DTO.
     *
     * @param id Identificador único do evento.
     * @param eventDto DTO contendo os novos dados do evento.
     * @return O evento atualizado.
     */
    T replaceEvent(Long id, TransferableEventData<T> eventDto);

}
