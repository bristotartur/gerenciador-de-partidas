package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.domain.participant.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.MatchMapper;
import com.bristotartur.gerenciadordepartidas.repositories.MatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Classe responsável por fornecer serviços relacionados a operações CRUD para a entidade {@link Match},
 * interagindo com o repositório {@link MatchRepository} para acessar e manipular dados relacionados a partidas.
 *
 * @see MatchMapper
 * @see ParticipantService
 * @see TeamService
 * @see GeneralMatchSportService
 */
@Service
@AllArgsConstructor
@Transactional
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final ParticipantService participantService;
    private final TeamService teamService;
    private final GeneralMatchSportService generalMatchSportService;

    /**
     * Retorna todas as partidas disponíveis no banco de dados.
     *
     * @return Uma lista contendo todas as partidas.
     */
    public List<Match> findAllMatches() {
        return matchRepository.findAll();
    }

    /**
     * Busca por uma entidade específica do tipo {@link Match} com base no seu ID.
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
     * Salva uma partida no sistema com base nos dados fornecidos em {@link MatchDto}, realizando uma validação
     * prévia destes dados antes de gerar a partida e persistí-la.
     *
     * @param matchDto DTO do tipo {@link MatchDto} contendo os dados da partida a ser salva.
     * @return A partida recém-salva.
     * @throws NotFoundException Caso alguma entidade não corresponda aos IDs fornecidos por {@link MatchDto}.
     */
    public Match saveMatch(MatchDto matchDto) {

        var teamA = teamService.findTeamById(matchDto.teamAId());
        var teamB = teamService.findTeamById(matchDto.teamBId());

        var players = matchDto.playerIds()
                .stream()
                .map(participantService::findParticipantById)
                .toList();

        this.checkPlayers(players, matchDto);
        var match = matchMapper.toNewMatch(matchDto, players, teamA, teamB);

        return generalMatchSportService.saveMatch(match, matchDto.sport());
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
     * Atualiza uma partida existente no banco de dados com base no seu ID e os dados fornecidos em {@link MatchDto},
     * realizando uma validação prévia destes dados antes de atualizar a partida. Isso envolve a substituição
     * completa dos dados da partida existente pelos novos dados fornecidos.
     *
     * @param id Identificador único da partida a ser atualizada.
     * @param matchDto DTO do tipo {@link MatchDto} contendo os dados atualizados da partida.
     * @return A partida atualizada.
     * @throws NotFoundException Caso nenhuma partida correspondente ao ID for encontrada ou
     * alguma entidade não corresponda aos IDs fornecidos por {@link MatchDto}.
     */
    public Match replaceMatch(Long id, MatchDto matchDto) {

        this.findMatchById(id);

        var teamA = teamService.findTeamById(matchDto.teamAId());
        var teamB = teamService.findTeamById(matchDto.teamBId());

        var players = matchDto.playerIds()
                .stream()
                .map(participantService::findParticipantById)
                .toList();

        this.checkPlayers(players, matchDto);
        var match = matchMapper.toExistingMatch(id, matchDto, players, teamA, teamB);

        return generalMatchSportService.saveMatch(match, matchDto.sport());
    }

    /**
     * Verifica se os jogadores passados pelo DTO estão aptos a participar da partida, ou seja, se
     * pertencem a alguma das equipes presentes nela.
     *
     * @param players Lista do tipo {@link Participant} contendo os jogadores da partida.
     * @param matchDto DTO do tipo {@link MatchDto} contendo os IDs das equipes presentes na partida.
     * @throws BadRequestException Caso algum jogador não pertença a alguma das equipes.
     */
    private void checkPlayers(List<Participant> players, MatchDto matchDto ) {

        Optional<Participant> invalidPlayer = players.stream()
                .filter(player -> player.getTeam().getId() != matchDto.teamAId()
                               && player.getTeam().getId() != matchDto.teamBId())
                .findFirst();

        invalidPlayer.ifPresent(player -> {
            throw new BadRequestException(
                    ExceptionMessages.PARTICIPANT_INVALID_FOR_MATCH.message.formatted(player.getId())
            );
        });
    }

}
