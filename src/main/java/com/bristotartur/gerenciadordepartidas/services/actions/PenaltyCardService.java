package com.bristotartur.gerenciadordepartidas.services.actions;

import com.bristotartur.gerenciadordepartidas.domain.actions.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestPenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.ConflictException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.exceptions.UnprocessableEntityException;
import com.bristotartur.gerenciadordepartidas.mappers.PenaltyCardMapper;
import com.bristotartur.gerenciadordepartidas.repositories.PenaltyCardRepository;
import com.bristotartur.gerenciadordepartidas.services.matches.MatchServiceMediator;
import com.bristotartur.gerenciadordepartidas.services.people.ParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe responsável por fornecer serviços relacionados a operações CRUD para a entidade {@link PenaltyCard},
 * interagindo com o repositório {@link PenaltyCardRepository} para acessar e manipular dados relacionados a
 * cartões de penalidade.
 *
 * @see PenaltyCardMapper
 * @see ParticipantService
 * @see MatchServiceMediator
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PenaltyCardService {

    private final PenaltyCardRepository penaltyCardRepository;
    private final PenaltyCardMapper penaltyCardMapper;
    private final ParticipantService participantService;
    private final MatchServiceMediator matchServiceMediator;

    /**
     * Retorna uma lista paginada dos cartões de penalidade disponíveis no sistema.
     *
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Uma {@link Page} contendo os cartões para a página especificada.
     */
    public Page<PenaltyCard> findAllPenaltyCards(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var penaltyCards = penaltyCardRepository.findAll(pageable);

        log.info("Penalty Card page of number '{}' and size '{}' was returned.", number, size);
        return penaltyCards;
    }

    /**
     * Busca por uma entidade específica do tipo {@link PenaltyCard} com base no seu ID.
     *
     * @param id Identificador único do cartão.
     * @return O cartão correspondente ao ID fornecido.
     * @throws NotFoundException Caso nenhum cartão correspondente ao ID for encontrado.
     */
    public PenaltyCard findPenaltyCardById(Long id) {

        var penaltyCard = penaltyCardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.PENALTY_CARD_NOT_FOUND.message));

        log.info("Penalty Card '{}' from Match '{}' was found.", id, penaltyCard.getMatch().getId());
        return penaltyCard;
    }

    /**
     * Salva um cartão no sistema com base nos dados fornecidos em {@link RequestPenaltyCardDto}.
     *
     * @param requestPenaltyCardDto DTO do tipo {@link RequestPenaltyCardDto} contendo os dados do cartão a ser salvo.
     * @return O cartão recém-salvo.
     *
     * @throws NotFoundException Caso alguma entidade não corresponda aos IDs fornecidos por {@link RequestPenaltyCardDto}.
     * @throws ConflictException Caso tente-se adicionar um cartão a uma partida que não está em andamento.
     * @throws UnprocessableEntityException Caso a modalidade esportiva da partida fornecida não seja suportada para
     * cartões ou o jogador associado ao cartão não esteja relacionado a partida.
     */
    public PenaltyCard savePenaltyCard(RequestPenaltyCardDto requestPenaltyCardDto) {

        var match = matchServiceMediator.findMatchForCard(requestPenaltyCardDto.matchId(), requestPenaltyCardDto.sport());
        var player = participantService.findParticipantById(requestPenaltyCardDto.playerId());
        ActionValidator.checkMatchForAction(match);
        ActionValidator.checkPlayerForAction(player, match);

        var savedPenaltyCard = penaltyCardMapper.toNewPenaltyCard(requestPenaltyCardDto, player, match);
        savedPenaltyCard =  penaltyCardRepository.save(savedPenaltyCard);

        log.info("Penalty Card '{}' was created in Match '{}'.", savedPenaltyCard.getId(), match.getId());
        return savedPenaltyCard;
    }

    /**
     * Remove um cartão do banco de dados com base no seu ID.
     *
     * @param id Identificador único do cartão.
     *
     * @throws NotFoundException Caso nenhum cartão relacionado ao ID seja encontrado.
     * @throws ConflictException Caso a partida relacionada ao cartão não esteja em andamento.
     */
    public void deletePenaltyCardById(Long id) {

        var penaltyCard = this.findPenaltyCardById(id);
        var match = penaltyCard.getMatch();

        ActionValidator.checkMatchForAction(match);
        penaltyCardRepository.deleteById(id);

        log.info("Penalty Card '{}' from Match '{}' was deleted.", id, match.getId());
    }

    /**
     * Atualiza um cartão existente no banco de dados com base no seu ID e os dados fornecidos em
     * {@link RequestPenaltyCardDto}, realizando uma validação prévia destes dados antes de atualizar o cartão.
     * Isso envolve a substituição completa dos dados do cartão existente pelos novos dados fornecidos.
     *
     * @param id Identificador único do cartão a ser atualizado.
     * @param requestPenaltyCardDto DTO do tipo {@link RequestPenaltyCardDto} contendo os dados atualizados do cartão.
     * @return O cartão atualizado.
     *
     * @throws NotFoundException Caso nenhum cartão correspondente ao ID for encontrado ou alguma
     * entidade não corresponda aos IDs fornecidos por {@link RequestPenaltyCardDto}.
     * @throws ConflictException Caso a partida relacionada ao cartão não esteja em andamento.
     * @throws UnprocessableEntityException Caso a modalidade esportiva da partida fornecida não seja suportada para
     * cartões ou o jogador associado ao cartão não esteja relacionado a partida.
     */
    public PenaltyCard replacePenaltyCard(Long id, RequestPenaltyCardDto requestPenaltyCardDto) {

        this.findPenaltyCardById(id);

        var match = matchServiceMediator.findMatchForCard(requestPenaltyCardDto.matchId(), requestPenaltyCardDto.sport());
        var player = participantService.findParticipantById(requestPenaltyCardDto.playerId());
        ActionValidator.checkMatchForAction(match);
        ActionValidator.checkPlayerForAction(player, match);

        var updatedPenaltyCard = penaltyCardMapper.toExistingPenaltyCard(id, requestPenaltyCardDto, player, match);
        updatedPenaltyCard = penaltyCardRepository.save(updatedPenaltyCard);

        log.info("Penalty Card '{}' from Match '{}' was updated.", id, match.getId());
        return updatedPenaltyCard;
    }

}
