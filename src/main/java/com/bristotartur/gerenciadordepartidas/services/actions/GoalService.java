package com.bristotartur.gerenciadordepartidas.services.actions;

import com.bristotartur.gerenciadordepartidas.domain.actions.Goal;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestGoalDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.ConflictException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.exceptions.UnprocessableEntityException;
import com.bristotartur.gerenciadordepartidas.mappers.GoalMapper;
import com.bristotartur.gerenciadordepartidas.repositories.GoalRepository;
import com.bristotartur.gerenciadordepartidas.services.matches.MatchServiceMediator;
import com.bristotartur.gerenciadordepartidas.services.people.ParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * Retorna uma lista paginada dos gols disponíveis no sistema.
     *
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Uma {@link Page} contendo os gols para a página especificada.
     */
    public Page<Goal> findAllGoals(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var goals = goalRepository.findAll(pageable);

        log.info("Goal page of number '{}' and size '{}' was returned.", number, size);
        return goals;
    }

    /**
     * Retorna uma lista paginada de gols relacionados a uma determinada partida.
     *
     * @param matchId Identificador único da partida.
     * @param sport Modalidade esportiva da partida.
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     *
     * @return Um {@link Page} contendo os gols relacionados a partida especificada.
     */
    public Page<Goal> findGoalsFromMatch(Long matchId, Sports sport, Pageable pageable) {

        matchServiceMediator.findMatch(matchId, sport);

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var goals = goalRepository.findMatchGoals(matchId, pageable);

        log.info("Goal page of number '{}' and size '{}' from Match '{}' was returned.", number, size, matchId);
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
     * Salva um gol no sistema com base nos dados fornecidos em {@link RequestGoalDto}, realizando uma validação
     * prévia destes dados antes de gerar o gol e persistí-lo. A partida na qual este gol estiver associado
     * terá seu placar alterado.
     *
     * @param requestGoalDto DTO do tipo {@link RequestGoalDto} dados do gol a ser salvo.
     * @return O gol recém-salvo.
     *
     * @throws NotFoundException Caso alguma entidade não corresponda aos IDs fornecidos por {@link RequestGoalDto}.
     * @throws ConflictException Caso tente-se adicionar um gol a uma partida que não está em andamento.
     * @throws UnprocessableEntityException Caso o jogador associado ao gol não esteja relacionado a partida.
     */
    public Goal saveGoal(RequestGoalDto requestGoalDto) {

        var match = matchServiceMediator.findMatchForGoal(requestGoalDto.matchId(), requestGoalDto.sport());
        var player = participantService.findParticipantById(requestGoalDto.playerId());

        ActionValidator.checkMatchForAction(match);
        ActionValidator.checkPlayerForAction(player, match);

        this.increaseScore(player.getTeam(), match);
        var savedGoal = goalRepository.save(goalMapper.toNewGoal(requestGoalDto, player, match));

        log.info("Goal '{}' was created in Match '{}'.", savedGoal.getId(), match.getId());
        return savedGoal;
    }

    /**
     * Remove um gol do banco de dados com base no seu ID. A partida na qual este gol
     * estava associado terá seu placar alterado.
     *
     * @param id Identificador único do gol.
     *
     * @throws NotFoundException Caso nenhum gol correspondente ao ID for encontrado.
     * @throws ConflictException Caso tente-se excluír um gol relacionado a uma partida que não está em andamento.
     */
    public void deleteGoalById(Long id) {

        var goal = this.findGoalById(id);
        var match = goal.getMatch();
        var team = goal.getPlayer().getTeam();

        ActionValidator.checkMatchForAction(match);
        goalRepository.deleteById(id);

        this.decreaseScore(team, match);
        log.info("Goal '{}' from Match '{}' was deleted.", id, match.getId());
    }

    /**
     * <p>Atualiza um gol existente no banco de dados com base no seu ID e os dados fornecidos em {@link RequestGoalDto},
     * realizando uma validação prévia destes dados antes de atualizar o gol. Isso envolve a substituição
     * completa dos dados do gol existente pelos novos dados fornecidos.</p>
     *
     * <p>A partida que estiver associada a este gol terá seu placar alterado caso os novos dados fornecidos
     * mudem a equipe que o marcou ou a partida em que ele ocorreu.</p>
     *
     * @param id Identificador único do gol a ser atualizado.
     * @param requestGoalDto DTO do tipo {@link RequestGoalDto} contendo os dados atualizados do gol.
     * @return O gol atualizado.
     *
     * @throws NotFoundException Caso nenhum gol correspondente ao ID for encontrado ou
     * alguma entidade não corresponda aos IDs fornecidos por {@link RequestGoalDto}.
     * @throws BadRequestException Caso a modalidade esportiva da partida fornecida não seja suportada para gols.
     * @throws ConflictException Caso tente-se atualizar um gol relacionado a uma partida que não está em andamento.
     * @throws UnprocessableEntityException Caso o jogador associado ao gol não esteja relacionado a partida.
     */
    public Goal replaceGoal(Long id, RequestGoalDto requestGoalDto) {

        var originalGol = this.findGoalById(id);
        var originaMatch = originalGol.getMatch();
        ActionValidator.checkMatchForAction(originaMatch);

        var newMatch = matchServiceMediator.findMatchForGoal(requestGoalDto.matchId(), requestGoalDto.sport());
        var newPlayer = participantService.findParticipantById(requestGoalDto.playerId());
        ActionValidator.checkMatchForAction(newMatch);
        ActionValidator.checkPlayerForAction(newPlayer, newMatch);

        var originalPlayerTeam = originalGol.getPlayer().getTeam();

        if (!originaMatch.equals(newMatch) || !originalPlayerTeam.equals(newPlayer.getTeam())) {
            this.increaseScore(newPlayer.getTeam(), newMatch);
            this.decreaseScore(originalPlayerTeam, originaMatch);
        }
        var updatedGoal = goalRepository.save(goalMapper.toExistingGoal(id, requestGoalDto, newPlayer, newMatch));

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
