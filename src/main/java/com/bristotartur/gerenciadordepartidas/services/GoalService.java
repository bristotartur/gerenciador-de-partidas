package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.actions.Goal;
import com.bristotartur.gerenciadordepartidas.domain.structure.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
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
 * @see ParticipantService
 * @see GeneralMatchSportService
 */
@Service
@RequiredArgsConstructor
@Transactional
public class GoalService {

    private final GoalRepository goalRepository;
    private final GoalMapper goalMapper;
    private final ParticipantService participantService;
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
     * Salva um gol no sistema com base nos dados fornecidos em {@link GoalDto}, realizando uma validação
     * prévia destes dados antes de gerar o gol e persistí-lo. A partida na qual este gol estiver associado
     * terá seu placar alterado.
     *
     * @param goalDto DTO do tipo {@link GoalDto} dados do gol a ser salvo.
     * @return O gol recém-salvo.
     * @throws NotFoundException Caso alguma entidade não corresponda aos IDs fornecidos por {@link GoalDto}.
     */
    public Goal saveGoal(GoalDto goalDto) {

        var match = generalMatchSportService.findMatchForGoal(goalDto.matchId(), goalDto.sport());
        var player = participantService.findParticipantById(goalDto.playerId());

        this.increaseScore(player.getTeam(), match);
        var goal = goalMapper.toNewGoal(goalDto, player, match);

        return goalRepository.save(goal);
    }

    /**
     * Remove um gol do banco de dados com base no seu ID. A partida na qual este gol
     * estava associado terá seu placar alterado.
     *
     * @param id Identificador único do gol.
     */
    public void deleteGoalById(Long id) {

        var goal = findGoalById(id);
        var team = goal.getPlayer().getTeam();

        this.decreaseScore(team, goal.getMatch());
        goalRepository.deleteById(id);
    }

    /**
     * <p>Atualiza um gol existente no banco de dados com base no seu ID e os dados fornecidos em {@link GoalDto},
     * realizando uma validação prévia destes dados antes de atualizar o gol. Isso envolve a substituição
     * completa dos dados do gol existente pelos novos dados fornecidos.</p>
     *
     * <p>A partida que estiver associada a este gol terá seu placar alterado caso os novos dados fornecidos
     * mudem a equipe que o marcou ou a partida em que ele ocorreu.</p>
     *
     * @param id Identificador único do gol a ser atualizado.
     * @param goalDto DTO do tipo {@link GoalDto} contendo os dados atualizados do gol.
     * @return O gol atualizado.
     * @throws NotFoundException Caso nenhum gol correspondente ao ID for encontrado ou
     * alguma entidade não corresponda aos IDs fornecidos por {@link GoalDto}.
     */
    public Goal replaceGoal(Long id, GoalDto goalDto) {

        var existingGoal = findGoalById(id);
        var originaMatch = existingGoal.getMatch();
        var originalPlayerTeam = existingGoal.getPlayer().getTeam();

        var newMatch = generalMatchSportService.findMatchForGoal(goalDto.matchId(), goalDto.sport());
        var newPlayer = participantService.findParticipantById(goalDto.playerId());

        if (!originaMatch.equals(newMatch) || !originalPlayerTeam.equals(newPlayer.getTeam())) {
            this.increaseScore(newPlayer.getTeam(), newMatch);
            this.decreaseScore(originalPlayerTeam, originaMatch);
        }
        var goal = goalMapper.toExistingGoal(id, goalDto, newPlayer, newMatch);
        return goalRepository.save(goal);
    }

    /**
     * Incrementa o placar da equipe fornecida na partida especificada. Caso a equipe seja a
     * equipe A da partida, o placar da equipe A será incrementado. Caso contrário, o placar da equipe B
     * será incrementado. O método não realiza validação adicional sobre a existência ou elegibilidade
     * da equipe na partida, assumindo que a equipe já está associada à partida.
     *
     * @param team Equipe que receberá o ponto.
     * @param match Partida que terá o placar alterado.
     */
    private void increaseScore(Team team, Match match) {

        if (team.equals(match.getTeamA())) {
            match.setTeamScoreA(match.getTeamScoreA() + 1);
            return;
        }
        match.setTeamScoreB(match.getTeamScoreB() + 1);
        return;
    }

    /**
     * Decrementa o placar da equipe fornecida na partida especificada. Caso a equipe seja a
     * equipe A da partida, o placar da equipe A será reduzido. Caso contrário, o placar da equipe B
     * será reduzido. O método não realiza validação adicional sobre a existência ou elegibilidade
     * da equipe na partida, assumindo que a equipe já está associada à partida.
     *
     * @param team Equipe que perderá o ponto.
     * @param match Partida que terá o placar alterado.
     */
    private void decreaseScore(Team team, Match match) {

        if (team.equals(match.getTeamA())) {
            match.setTeamScoreA(match.getTeamScoreA() - 1);
            return;
        }
        match.setTeamScoreB(match.getTeamScoreB() - 1);
        return;
    }

}
