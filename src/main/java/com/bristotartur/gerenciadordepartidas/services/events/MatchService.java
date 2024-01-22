package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.structure.Match;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingMatchDto;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.MatchMapper;
import com.bristotartur.gerenciadordepartidas.repositories.MatchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private final EntityManager entityManager;
    private final MatchRepository<Match> matchRepository;
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
     * Recupera uma lista contendo todas as instâncias de uma especialização específica de {@link Match}.
     *
     * @param sport Esporte no qual as instâncias retornadas na lista serão especializadas.
     * @return Uma lista contendo todas as instâncias da especialização de {@link Match} definida;
     */
    public List<? extends Match> findMatchesBySport(Sports sport) {
        return generalMatchSportService.findMatchesBySport(sport);
    }

    /**
     * Busca por uma entidade específica do tipo {@link Match} com base no seu ID.
     *
     * @param id Identificador único da partida.
     * @return A partida correspondente ao ID fornecido.
     * @throws NotFoundException Caso nenhuma partida correspondente ao ID for encontrada.
     */
    public Match findMatchById(Long id) {

        return matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.MATCH_NOT_FOUND.message));
    }

    /**
     * Busca pela modalidade esportiva de uma determinada partida com base no seu ID. O valor
     * retornado por este método não corresponde ao valor interno das opções do enum {@link Sports},
     * mas sim ao nome dessas opções.
     *
     * @param id Identificador único da partida.
     * @return A modalidade esportiva correspondente a partida.
     * @throws NotFoundException Caso nenhuma partida correspondente ao ID for encontrada.
     */
    public String findMatchSportById(Long id) {

        this.findMatchById(id);
        return matchRepository.findMatchTypeById(id, entityManager);
    }

    /**
     * Retorna todos os jogadores presentes em uma partida.
     *
     * @param id Identificador único da partida.
     * @return Uma lista com todos os jogadores da partida.
     */
    public List<Participant> findAllMatchPlayers(Long id) {
        return findMatchById(id).getPlayers();
    }

    /**
     * Salva uma partida no sistema com base nos dados fornecidos em {@link MatchDto}, realizando uma validação
     * prévia destes dados antes de gerar a partida e persistí-la.
     *
     * @param matchDto DTO do tipo {@link MatchDto} contendo os dados da partida a ser salva.
     * @return A partida recém-salva.
     * @throws NotFoundException Caso alguma entidade não corresponda aos IDs fornecidos por {@link MatchDto}.
     * @throws BadRequestException Caso a seleção das equipes ou jogadores seja irregular.
     */
    public Match saveMatch(MatchDto matchDto) {

        if (matchDto.teamAId() == matchDto.teamBId())
            throw new BadRequestException(ExceptionMessages.INVALID_TEAMS_FOR_MATCH.message);

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
     * @throws NotFoundException Caso nenhuma partida correspondente ao ID seja encontrada.
     */
    public void deleteMatchById(Long id) {

        this.findMatchById(id);
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
     * @throws BadRequestException Caso a seleção das equipes ou jogadores seja irregular.
     */
    public Match replaceMatch(Long id, MatchDto matchDto) {

        var existingMatch = findMatchById(id);

        if (matchDto.teamAId() == matchDto.teamBId())
            throw new BadRequestException(ExceptionMessages.INVALID_TEAMS_FOR_MATCH.message);

        var teamA = teamService.findTeamById(matchDto.teamAId());
        var teamB = teamService.findTeamById(matchDto.teamBId());
        var players = matchDto.playerIds()
                .stream()
                .map(participantService::findParticipantById)
                .toList();

        this.checkPlayers(players, matchDto);

        var match = matchMapper.toExistingMatch(id, matchDto, players, teamA, teamB);
        
        match.setTeamScoreA(existingMatch.getTeamScoreA());
        match.setTeamScoreB(existingMatch.getTeamScoreB());
        return generalMatchSportService.saveMatch(match, matchDto.sport());
    }

    /**
     * Gera um DTO do tipo {@link ExposingMatchDto} com base na partida fornecida.
     *
     * @param match Partida que terá seus dados mapeados para o DTO.
     * @return Nova instância de {@link ExposingMatchDto} contendo os dados fornecidos.
     */
    public ExposingMatchDto createExposingMatchDto(Match match) {

        var sport = findMatchSportById(match.getId());
        return matchMapper.toNewExposingMatchDto(match, sport);
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
