package com.bristotartur.gerenciadordepartidas.services.events;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.events.Event;
import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestSportEventDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.exceptions.ConflictException;
import com.bristotartur.gerenciadordepartidas.exceptions.UnprocessableEntityException;

import java.util.List;
import java.util.Optional;

/**
 * Classe utilitária responsável por fornecer métodos para a validação de instâncias de {@link SportEvent}
 * e {@link RequestSportEventDto} para gerar ou criar novos eventos esportivos.
 *
 * @see Event
 * @see Status
 * @see Sports
 * @see Modality
 * @see SportEventService
 */
public final class SportEventValidator {

    private SportEventValidator() {
    }

    /**
     * Verifica se há algum evento do tipo {@link SportEvent} associado a uma instância de {@link Edition} com o
     * mesmo tipo esportivo e mesma modalidade passados pelo DTO. Cada evento eportivo deve ser único em sua edição,
     * os eventos de mesmo tipo devem ser de modalidades diferentes e vice-versa. Caso já exista um evento esportivo
     * de mesmo tipo e modalidade que os passados pelo DTO na edição, uma exceção será lançada.
     *
     * @param sportEvents Lista contendo todas as instâncias de {@link SportEvent} associadas a uma mesma edição.
     * @param dto DTO do do tipo {@link RequestSportEventDto} contendo os dados do novo evento.
     * @throws ConflictException Caso já exista um evento com o mesmo tipo e modalidade passados pelo DTO.
     *
     * @apiNote O método não realiza nenhuma validação prévia da relação entre os eventos esportivos presentes
     * na listagem passada como argumento, assumindo que todos sejam da mesma edição.
     */
    public static void checkSportEventForEdition(List<SportEvent> sportEvents, RequestSportEventDto dto) {

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

            throw new ConflictException(message.formatted(type.name(), modality.name(), editionId));
        }
    }

    /**
     * Verifica se a instância de {@link SportEvent} passada está apta para ter seus atributos atualizados,
     * sendo que eventos esportivos só podem ter os atributos alterados quando estão agendados.
     *
     * @param originalEvent Instância de {@link SportEvent} que será atualizada.
     * @throws UnprocessableEntityException Caso o evento esportivo não esteja apto para ser atualizado.
     */
    public static void checkSportEventForUpdate(SportEvent originalEvent) {

        var status = originalEvent.getEventStatus();

        if (!status.equals(Status.SCHEDULED)) {
            throw new UnprocessableEntityException(ExceptionMessages.INVALID_UPDATE_TO_SPORT_EVENT.message);
        }
    }

    /**
     * Verifica se o novo mínimo total de partidas permitidas é menor que o número de partidas já registradas.
     * O mínimo total de partidas é a quantia de partidas registradas necessárias para iniciar o evento. Para
     * calcular o mínimo total de partidas, basta pegar o total de partidas e subtrair 4.
     *
     * @param originalEvent Instância de {@link SportEvent} contendo os dados utilizados na análise.
     * @param newTotal O novo total de partidas, no qual será calculado o mínimo total.
     * @throws UnprocessableEntityException Caso a quantidade de partidas registradas exceda o novo mínimo
     * total de partidas.
     */
    public static void checkNewTotalMatchesForSportEvent(SportEvent originalEvent, Integer newTotal) {

        var matchQuantity = originalEvent.getMatches().size();

        if (matchQuantity > newTotal - 4) {
            throw new UnprocessableEntityException(ExceptionMessages.CANNOT_UPDATE_TOTAL_MATCHES.message);
        }
    }

    /**
     * Verifica se uma instância de {@link SportEvent} está apta para ter seu status atualizado. Para atualizar
     * o status de um evento esportivo, certas circunstâncias precisam ser atendidas:
     *
     * <ul>
     *     <li>A instância de {@link Edition} associada ao evento precisa ter o status IN_PROGRESS.</li>
     *     <li>Caso o novo status seja 'IN_PROGRESS', o evento necessitará ter o mínimo total de
     *     partidas marcadas.</li>
     *     <li>Caso o novo status seja 'ENDED', a quantidade de partidas registradas deve ser igual ao total
     *     permitido pelo evento e todas devem estar encerradas.</li>
     * </ul>
     *
     * @param originalEvent O evento que terá o stus atualizado.
     * @param newStatus O novo status do evento.
     * @param editionStatus O status da edição associada ao evento.
     * @throws UnprocessableEntityException Caso alguma das circunstâncias não seja atendida corretamente.
     *
     * @apiNote O método não realiza nenhuma validação prévia entre a relação do 'editionStatus' passado como
     * argumento e uma instância real de {@link Edition}, assumindo que estejam relacionados e que esta edição
     * está vinculada ao evento passado.
     */
    public static void checkSportEventToUpdateStatus(SportEvent originalEvent, Status newStatus, Status editionStatus) {

        if (!editionStatus.equals(Status.IN_PROGRESS)) {
            throw new UnprocessableEntityException(ExceptionMessages.CANNOT_UPDATE_EVENT_STATUS.message);
        }
        var totalMatches = originalEvent.getTotalMatches();

        if (newStatus.equals(Status.IN_PROGRESS)) {
            checkMatchesToStartEvent(originalEvent, totalMatches);
        }
        if (newStatus.equals(Status.ENDED)) {
            checkMatchesToFinishEvent(originalEvent);
        }
    }

    /**
     * <p>Verifica se uma instância de {@link SportEvent} possui a quantidade necessária de partidas registradas
     * para iniciar. As partidas de um evento esportivo podem ser normais ou decisivas (seminfinal, final e disputa
     * de terceito lugar). As partidas decisivas são formadas a partir dos resultados das partidas não decisivas,
     * portanto, todas as partidas não decisivas devem ser marcadas antes do evento iniciar. A função deste método
     * é garantir que o evento tenha o número necessário de partidas para iniciar.</p>
     *
     * <p>A quantidade de partidas necessárias para iniciar um evento esportivo é chamada de mínimo total.
     * Para calculá-lo, basta pegar o total de partidas permitidas e subtrair 4.</p>
     *
     * @param originalEvent Instância de {@link SportEvent} contendo os dados utilizados na análise.
     * @param totalMatches O total de partidas, no qual será calculado o mínimo total.
     * @throws UnprocessableEntityException Caso o evento não esteja apto para iniciar.
     */
    private static void checkMatchesToStartEvent(SportEvent originalEvent, Integer totalMatches) {

        var necessaryMatches = totalMatches - 4;
        var hasNoSufficientMatchesToStart = originalEvent.getMatches().size() < necessaryMatches;

        if (hasNoSufficientMatchesToStart) {
            var message = ExceptionMessages.NO_MATCHES_TO_START.message;
            throw new UnprocessableEntityException(message.formatted(necessaryMatches));
        }
    }

    /**
     * Verifica se uma instância de {@link SportEvent} está apta para ser encerrada. Para um evento esportivo
     * ser encerrado, é necessário que duas condições sejam atendidas:
     * <ol>
     *     <li>A quantidade de partidas registradas deve ser igual ao total de partidas permitidas.</li>
     *     <li>Todas as partidas registradas devem ter o {@link Status} 'ENDED', ou seja, estarem encerradas.</li>
     * </ol>
     * Caso nenhuma destas condições seja cumprida, uma exceção será lançada.
     *
     * @param originalEvent Instância de {@link SportEvent} que será analizada para encerrar.
     * @throws UnprocessableEntityException Caso o evento não esteja apto para encerrar.
     */
    private static void checkMatchesToFinishEvent(SportEvent originalEvent) {

        var totalMatches = originalEvent.getTotalMatches();
        var hasNoSufficientMatchesToEnd = originalEvent.getMatches().size() < totalMatches;

        if (hasNoSufficientMatchesToEnd) {
            var message = ExceptionMessages.NO_MATCHES_TO_FINISH.message;
            throw new UnprocessableEntityException(message.formatted(totalMatches));
        }
        var matches = originalEvent.getMatches();
        Optional<Status> notEndedStatus = matches.stream()
                .map(Match::getMatchStatus)
                .filter(status -> !status.equals(Status.ENDED))
                .findFirst();

        if (notEndedStatus.isPresent()) {
            throw new UnprocessableEntityException(ExceptionMessages.INVALID_MATCH_STATUS_TO_FINISH_EVENT.message);
        }
    }

}
