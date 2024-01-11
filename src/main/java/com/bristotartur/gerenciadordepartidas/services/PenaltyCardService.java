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
 * Classe responsável por fornecer serviços relacionados a operações CRUD para a entidade PenaltyCard,
 * interagindo com o repositório PenaltyCardRepository para acessar e manipular dados relacionados a
 * cartões de penalidade.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PenaltyCardService {

    private final PenaltyCardRepository penaltyCardRepository;
    private final PenaltyCardMapper penaltyCardMapper;

    /**
     * Retorna todos os cartões disponíveis no banco de dados.
     *
     * @return Uma lista contendo todos os cartões.
     */
    public List<PenaltyCard> findAllPenaltyCards() {
        return penaltyCardRepository.findAll();
    }

    /**
     * Busca por uma entidade específica do tipo PenaltyCard com base no seu ID.
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
     * Salva um cartão no sistema com base nos dados fornecidos em PenaltyCardDto.
     *
     * @param penaltyCardDto Dados do cartão a ser salvo.
     * @return O cartão recém-salvo.
     */
    public PenaltyCard savePenaltyCard(PenaltyCardDto penaltyCardDto) {

        var savedPenaltyCard = penaltyCardRepository.save(penaltyCardMapper.toNewPenaltyCard(penaltyCardDto));
        return savedPenaltyCard;
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
     * Atualiza um cartão existente no banco de dados com base no seu ID e os dados fornecidos em PenaltyCardDto.
     * Isso envolve a substituição completa dos dados do cartão existente pelos novos dados fornecidos.
     *
     * @param id Identificador único do cartão a ser atualizado.
     * @param penaltyCardDto Dados atualizados do cartão.
     * @return O cartão atualizado.
     * @throws NotFoundException Caso nenhum cartão correspondente ao ID for encontrado.
     */
    public PenaltyCard replacePenaltyCard(Long id, PenaltyCardDto penaltyCardDto) {
        
        this.findPenaltyCardById(id);
        
        var penaltyCard = penaltyCardMapper.toExistingPenaltyCard(id, penaltyCardDto);
        var updatedPenaltyCard = penaltyCardRepository.save(penaltyCard);
        
        return updatedPenaltyCard;
    }

}
