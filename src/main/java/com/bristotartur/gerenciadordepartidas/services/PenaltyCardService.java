package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.PenaltyCard;
import com.bristotartur.gerenciadordepartidas.dtos.PenaltyCardDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.PenaltyCardMapper;
import com.bristotartur.gerenciadordepartidas.repositories.PenaltyCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Classe responsável por fornecer serviços relacionados a operações CRUD para a entidade {@link PenaltyCard},
 * interagindo com o repositório {@link PenaltyCardRepository} para acessar e manipular dados relacionados a
 * cartões de penalidade.
 *
 * @see PenaltyCardMapper
 * @see ParticipantService
 * @see GeneralMatchSportService
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PenaltyCardService {

    private final PenaltyCardRepository penaltyCardRepository;
    private final PenaltyCardMapper penaltyCardMapper;
    private final ParticipantService participantService;
    private final GeneralMatchSportService generalMatchSportService;

    /**
     * Retorna todos os cartões disponíveis no banco de dados.
     *
     * @return Uma lista contendo todos os cartões.
     */
    public List<PenaltyCard> findAllPenaltyCards() {
        return penaltyCardRepository.findAll();
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

        return penaltyCard;
    }

    /**
     * Salva um cartão no sistema com base nos dados fornecidos em {@link PenaltyCardDto}.
     *
     * @param penaltyCardDto DTO do tipo {@link PenaltyCardDto} contendo os dados do cartão a ser salvo.
     * @return O cartão recém-salvo.
     * @throws NotFoundException Caso alguma entidade não corresponda aos IDs fornecidos por {@link PenaltyCardDto}.
     */
    public PenaltyCard savePenaltyCard(PenaltyCardDto penaltyCardDto) {

        var matchSport = generalMatchSportService.newMatchSport(penaltyCardDto.sport());
        var player = participantService.findParticipantById(penaltyCardDto.playerId());

        var penaltyCard = penaltyCardMapper.toNewPenaltyCard(penaltyCardDto, player, matchSport);

        return penaltyCardRepository.save(penaltyCard);
    }

    /**
     * Remove um cartão do banco de dados com base no seu ID.
     *
     * @param id Identificador único do cartão.
     */
    public void deletePenaltyCardById(Long id) {
        penaltyCardRepository.deleteById(id);
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

        var matchSport = generalMatchSportService.newMatchSport(penaltyCardDto.sport());
        var player = participantService.findParticipantById(penaltyCardDto.playerId());

        var penaltyCard = penaltyCardMapper.toExistingPenaltyCard(id, penaltyCardDto, player, matchSport);

        return penaltyCardRepository.save(penaltyCard);
    }

}
