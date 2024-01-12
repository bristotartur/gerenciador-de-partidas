package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.specifications.Goal;
import com.bristotartur.gerenciadordepartidas.dtos.GoalDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.GoalMapper;
import com.bristotartur.gerenciadordepartidas.repositories.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Classe responsável por fornecer serviços relacionados a operações CRUD para a entidade {@link Goal},
 * interagindo com o repositório {@link GoalRepository} para acessar e manipular dados relacionados a gols.
 *
 * @see GoalMapper
 * @see TeamService
 * @see GeneralMatchSportService
 */
@Service
@RequiredArgsConstructor
@Transactional
public class GoalService {

    private final GoalRepository goalRepository;
    private final GoalMapper goalMapper;
    private final TeamService teamService;
    private final GeneralMatchSportService generalMatchSportService;

    /**
     * Retorna todos os gols disponíveis no banco de dados.
     *
     * @return Uma lista contendo todos os gols.
     */
    public List<Goal> findAllGoals() {
        return goalRepository.findAll();
    }

    /**
     * Busca por uma entidade específica do tipo {@link Goal} com base no seu ID.
     *
     * @param id Identificador único do gol.
     * @return O gol correspondente ao ID fornecido.
     * @throws NotFoundException Caso nenhum gol correspondente ao ID for encontrado.
     */
    public Goal findGoalById(Long id) {

        var goal = goalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.GOAL_NOT_FOUND.message));

        return goal;
    }

    /**
     * Salva um gol no sistema com base nos dados fornecidos em GoalDto, realizando uma validação
     * prévia destes dados antes de gerar o gol e persistí-lo.
     *
     * @param goalDto DTO do tipo {@link GoalDto} dados do gol a ser salvo.
     * @return O gol recém-salvo.
     * @throws NotFoundException Caso alguma entidade não corresponda aos IDs fornecidos por {@link GoalDto}.
     */
    public Goal saveGoal(GoalDto goalDto) {

        var matchSport = generalMatchSportService.newMatchSport(goalDto.sport());
        var team = teamService.findTeamById(goalDto.teamId());

        var goal = goalMapper.toNewGoal(goalDto, matchSport, team);

        return goalRepository.save(goal);
    }

    /**
     * Remove um gol do banco de dados com base no seu ID.
     *
     * @param id Identificador único do gol.
     */
    public void deleteGoalById(Long id) {
        goalRepository.deleteById(id);
    }

    /**
     * Atualiza um gol existente no banco de dados com base no seu ID e os dados fornecidos em {@link GoalDto},
     * realizando uma validação prévia destes dados antes de atualizar o gol. Isso envolve a substituição
     * completa dos dados do gol existente pelos novos dados fornecidos.
     *
     * @param id Identificador único do gol a ser atualizado.
     * @param goalDto DTO do tipo {@link GoalDto} contendo os dados atualizados do gol.
     * @return O gol atualizado.
     * @throws NotFoundException Caso nenhum gol correspondente ao ID for encontrado ou
     * alguma entidade não corresponda aos IDs fornecidos por {@link GoalDto}.
     */
    public Goal replaceGoal(Long id, GoalDto goalDto) {

        this.findGoalById(id);

        var matchSport = generalMatchSportService.newMatchSport(goalDto.sport());
        var team = teamService.findTeamById(goalDto.teamId());

        var goal = goalMapper.toExistingGoal(id, goalDto, matchSport, team);

        return goalRepository.save(goal);
    }

}
