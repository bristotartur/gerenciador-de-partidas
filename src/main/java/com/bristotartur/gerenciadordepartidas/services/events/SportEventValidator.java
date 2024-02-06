package com.bristotartur.gerenciadordepartidas.services.events;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.domain.events.Event;
import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.dtos.input.SportEventDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;

import java.util.List;
import java.util.Optional;

/**
 * Classe utilitária responsável por fornecer métodos para a validação de instâncias de {@link SportEvent}
 * e {@link SportEventDto} para gerar ou criar novos eventos esportivos.
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
     * <p>Verifica se há algum evento do tipo {@link SportEvent} associado a uma instância de {@link Edition} com o
     * mesmo tipo esportivo e mesma modalidade passados pelo DTO. Cada evento eportivo deve ser único em sua edição,
     * os eventos de mesmo tipo devem ser de modalidades diferentes e vice-versa. Caso já exista um evento esportivo
     * de mesmo tipo e modalidade que os passados pelo DTO na edição, uma exceção será lançada.</p>
     *
     * <p>O método não realiza nenhuma validação prévia da relação entre os eventos esportivos presentes na listagem
     * passada como argumento, assumindo que todos sejam da mesma edição.</p>
     *
     * @param sportEvents Lista contendo todas as instâncias de {@link SportEvent} associadas a uma mesma edição.
     * @param dto DTO do do tipo {@link SportEventDto} contendo os dados do novo evento.
     * @throws BadRequestException Caso já exista um evento com o mesmo tipo e modalidade passados pelo DTO.
     */
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

    /**
     * Verifica se o DTO passado está apto para atualizar uma instância de {@link SportEvent}. De modo geral,
     * todos os atributos de um evento esportivo podem ser alterados enquanto ele ainda estiver sob o status
     * 'SCHEDULED'. Porém, caso ele possua um status de qualquer outro valor, nenhum de seus atributos poderá
     * mais ser atualizado, com exceção do próprio atributo de status.
     *
     * @param originalEvent Instância de {@link SportEvent} que será atualizada.
     * @param dto DTO do tipo {@link SportEventDto} contendo os novo dados do evento.
     */
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

    /**
     * Verifica as partidas associadas a uma instância de {@link SportEvent} para determinar se o evento está
     * apto para ser atualizado. Alguns dados referentes às partidas podem impedir a mudança do {@link Status}
     * de um evento, como a quantidade de partidas registradas, seus próprios status e o total permitido de
     * partidas no evento.
     *
     * @param originalEvent Instância de {@link SportEvent} contendo as partidas utilizadas no método.
     * @param dto DTO do tipo {@link SportEventDto} contendo os novos dados do evento.
     * @throws BadRequestException Caso o evento não esteja apto para ser atualizado.
     */
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

    /**
     * Verifica se o novo mínimo total de partidas permitidas é menor que o número de partidas já registradas.
     * O mínimo total de partidas é a quantia de partidas registradas necessárias para iniciar o evento. Para
     * calcular o mínimo total de partidas, basta pegar o total de partidas e subtrair 4.
     *
     * @param originalEvent Instância de {@link SportEvent} contendo os dados utilizados na análise.
     * @param newTotal O novo total de partidas, no qual será calculado o mínimo total.
     * @throws BadRequestException Caso a quantidade de partidas registradas exceda o novo mínimo total de partidas.
     */
    private static void checkTotalMatches(SportEvent originalEvent, Integer newTotal) {

        var matchQuantity = originalEvent.getMatches().size();

        if (matchQuantity > newTotal - 4) {
            throw new BadRequestException(ExceptionMessages.CANNOT_UPDATE_TOTAL_MATCHES.message);
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
     * @throws BadRequestException Caso o evento não esteja apto para iniciar.
     */
    private static void checkMatchesToStartEvent(SportEvent originalEvent, Integer totalMatches) {

        var necessaryMatches = totalMatches - 4;
        var hasNoSufficientMatchesToStart = originalEvent.getMatches().size() < necessaryMatches;

        if (hasNoSufficientMatchesToStart) {
            var message = ExceptionMessages.NO_MATCHES_TO_START.message;
            throw new BadRequestException(message.formatted(necessaryMatches));
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
     * @throws BadRequestException Caso o evento não esteja apto para encerrar.
     */
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
