package com.bristotartur.gerenciadordepartidas.services.actions;

import com.bristotartur.gerenciadordepartidas.domain.actions.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingPenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.dtos.PenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
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
     * Gera um DTO do tipo {@link ExposingPenaltyCardDto} com base no cartão forecido.
     *
     * @param penaltyCard Cartão de penalidade que terá seus dados mapeados para o DTO.
     * @return Uma nova instância de {@link ExposingPenaltyCardDto} contendo os dados fornecidos.
     */
    public ExposingPenaltyCardDto createExposingPenaltyCardDto(PenaltyCard penaltyCard) {
        return penaltyCardMapper.toNewExposinfPenaltyCardDto(penaltyCard);
    }

    /**
     * Salva um cartão no sistema com base nos dados fornecidos em {@link PenaltyCardDto}.
     *
     * @param penaltyCardDto DTO do tipo {@link PenaltyCardDto} contendo os dados do cartão a ser salvo.
     * @return O cartão recém-salvo.
     * @throws NotFoundException Caso alguma entidade não corresponda aos IDs fornecidos por {@link PenaltyCardDto}.
     */
    public PenaltyCard savePenaltyCard(PenaltyCardDto penaltyCardDto) {

        var match = matchServiceMediator.findMatch(penaltyCardDto.matchId(), penaltyCardDto.sport());
        var player = participantService.findParticipantById(penaltyCardDto.playerId());

        var savedPenaltyCard = penaltyCardMapper.toNewPenaltyCard(penaltyCardDto, player, match);
        savedPenaltyCard =  penaltyCardRepository.save(savedPenaltyCard);

        log.info("Penalty Card '{}' was created in Match '{}'.", savedPenaltyCard.getId(), match.getId());
        return savedPenaltyCard;
    }

    /**
     * Remove um cartão do banco de dados com base no seu ID.
     *
     * @param id Identificador único do cartão.
     */
    public void deletePenaltyCardById(Long id) {

        var penaltyCard = this.findPenaltyCardById(id);
        var match = penaltyCard.getMatch();

        penaltyCardRepository.deleteById(id);

        log.info("Penalty Card '{}' from Match '{}' was deleted.", id, match.getId());
    }

    /**
     * Atualiza um cartão existente no banco de dados com base no seu ID e os dados fornecidos em
     * {@link PenaltyCardDto}, realizando uma validação prévia destes dados antes de atualizar o cartão.
     * Isso envolve a substituição completa dos dados do cartão existente pelos novos dados fornecidos.
     *
     * @param id Identificador único do cartão a ser atualizado.
     * @param penaltyCardDto DTO do tipo {@link PenaltyCardDto} contendo os dados atualizados do cartão.
     * @return O cartão atualizado.
     * @throws NotFoundException Caso nenhum cartão correspondente ao ID for encontrado ou alguma
     * entidade não corresponda aos IDs fornecidos por {@link PenaltyCardDto}.
     */
    public PenaltyCard replacePenaltyCard(Long id, PenaltyCardDto penaltyCardDto) {

        this.findPenaltyCardById(id);

        var match = matchServiceMediator.findMatchForCard(penaltyCardDto.matchId(), penaltyCardDto.sport());
        var player = participantService.findParticipantById(penaltyCardDto.playerId());

        var updatedPenaltyCard = penaltyCardMapper.toExistingPenaltyCard(id, penaltyCardDto, player, match);
        updatedPenaltyCard = penaltyCardRepository.save(updatedPenaltyCard);

        log.info("Penalty Card '{}' from Match '{}' was updated.", id, match.getId());
        return updatedPenaltyCard;
    }

}
