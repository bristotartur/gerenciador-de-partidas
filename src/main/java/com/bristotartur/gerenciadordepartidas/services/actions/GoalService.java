package com.bristotartur.gerenciadordepartidas.services.actions;

import com.bristotartur.gerenciadordepartidas.domain.actions.Goal;
import com.bristotartur.gerenciadordepartidas.domain.events.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingGoalDto;
import com.bristotartur.gerenciadordepartidas.dtos.GoalDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.GoalMapper;
import com.bristotartur.gerenciadordepartidas.repositories.GoalRepository;
import com.bristotartur.gerenciadordepartidas.services.events.MatchServiceMediator;
import com.bristotartur.gerenciadordepartidas.services.people.ParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Classe responsável por fornecer serviços relacionados a operações CRUD para a entidade {@link Goal},
 * interagindo com o repositório {@link GoalRepository} para acessar e manipular dados relacionados a gols.
 *
 * @see GoalMapper
 * @see ParticipantService
 * @see MatchServiceMediator
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GoalService {

    private final GoalRepository goalRepository;
    private final GoalMapper goalMapper;
    private final ParticipantService participantService;
    private final MatchServiceMediator matchServiceMediator;

    /**
     * Retorna todos os gols disponíveis no banco de dados.
     *
     * @return Uma lista contendo todos os gols.
     */
    public List<Goal> findAllGoals() {

        List<Goal> goals = goalRepository.findAll();

        log.info("List with all Goals was found.");
        return goals;
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

        log.info("Goal '{}' from Match '{}' was found.", id, goal.getMatch().getId());
        return goal;
    }

    /**
     * Gera um DTO do tipo {@link ExposingGoalDto} com base no gol fornecido.
     *
     * @param goal Gol que terá seus dados mapeados para o DTO.
     * @return Uma nova instância de {@link ExposingGoalDto} contendo os dados fornecidos.
     */
    public ExposingGoalDto createExposingGoalDto(Goal goal) {
        return goalMapper.toNewExposingGoalDto(goal);
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

        var match = matchServiceMediator.findMatchForGoal(goalDto.matchId(), goalDto.sport());
        var player = participantService.findParticipantById(goalDto.playerId());

        this.increaseScore(player.getTeam(), match);
        var savedGoal = goalRepository.save(goalMapper.toNewGoal(goalDto, player, match));

        log.info("Goal '{}' was created in Match '{}'.", savedGoal.getId(), match.getId());
        return savedGoal;
    }

    /**
     * Remove um gol do banco de dados com base no seu ID. A partida na qual este gol
     * estava associado terá seu placar alterado.
     *
     * @param id Identificador único do gol.
     * @throws NotFoundException Caso nenhum gol correspondente ao ID for encontrado.
     */
    public void deleteGoalById(Long id) {

        var goal = this.findGoalById(id);
        var team = goal.getPlayer().getTeam();
        var match = goal.getMatch();

        this.decreaseScore(team, match);
        goalRepository.deleteById(id);

        log.info("Goal '{}' from Match '{}' was deleted.", id, match.getId());
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
     * @throws BadRequestException Caso a modalidade esportiva da partida fornecida não seja suportada para gols.
     */
    public Goal replaceGoal(Long id, GoalDto goalDto) {

        var originalGol = this.findGoalById(id);
        var originaMatch = originalGol.getMatch();
        var originalPlayerTeam = originalGol.getPlayer().getTeam();

        var newMatch = matchServiceMediator.findMatchForGoal(goalDto.matchId(), goalDto.sport());
        var newPlayer = participantService.findParticipantById(goalDto.playerId());

        if (!originaMatch.equals(newMatch) || !originalPlayerTeam.equals(newPlayer.getTeam())) {
            this.increaseScore(newPlayer.getTeam(), newMatch);
            this.decreaseScore(originalPlayerTeam, originaMatch);
        }
        var updatedGoal = goalRepository.save(goalMapper.toExistingGoal(id, goalDto, newPlayer, newMatch));

        log.info("Goal '{}' from Match '{}' was updated.", id, updatedGoal.getMatch().getId());
        return updatedGoal;
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
    }

}
