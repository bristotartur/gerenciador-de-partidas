package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.MatchMapper;
import com.bristotartur.gerenciadordepartidas.repositories.MatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Classe responsável por fornecer serviços relacionados a operações CRUD para a entidade Match,
 * interagindo com o repositório MatchRepository para acessar e manipular dados relacionados a partidas.
 */
@Service
@AllArgsConstructor
@Transactional
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final GeneralMatchSportService matchSportService;

    /**
     * Retorna todas as partidas disponíveis no banco de dados.
     *
     * @return Uma lista contendo todas as partidas.
     */
    public List<Match> findAllMatches() {
        return matchRepository.findAll();
    }

    /**
     * Busca por uma entidade específica do tipo Match com base no seu ID.
     *
     * @param id Identificador único da partida.
     * @return A partida correspondente ao ID fornecido.
     * @throws NotFoundException Caso nenhuma partida correspondente ao ID for encontrada.
     */
    public Match findMatchById(Long id) {

        var match = matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.MATCH_NOT_FOUND.message));

        return match;
    }

    /**
     * Salva uma partida no sistema com base nos dados fornecidos em MatchDto.
     *
     * @param matchDto Dados da partida a ser salva.
     * @return A partida recém-salva.
     */
    public Match saveMatch(MatchDto matchDto) {

        var savedMatch = matchRepository.save(matchMapper.toNewMatch(matchDto));
        return savedMatch;
    }

    /**
     * Remove uma partida do banco de dados com base no seu ID.
     *
     * @param id Identificador único da partida.
     */
    public void deleteMatchById(Long id) {
        matchRepository.deleteById(id);
    }

    /**
     * Atualiza uma partida existente no banco de dados com base no seu ID e os dados fornecidos em MatchDto.
     * Isso envolve a substituição completa dos dados da partida existente pelos novos dados fornecidos.
     *
     * @param id Identificador único da partida a ser atualizada.
     * @param matchDto Dados atualizados da partida.
     * @return A partida atualizada.
     * @throws NotFoundException Caso nenhuma partida correspondente ao ID for encontrada.
     */
    public Match replaceMatch(Long id, MatchDto matchDto) {

        this.findMatchById(id);

        var match = matchMapper.toExistingMatch(id, matchDto);
        var updatedMatch = matchRepository.save(match);

        return updatedMatch;
    }

}
