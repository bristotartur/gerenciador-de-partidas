package com.bristotartur.gerenciadordepartidas.services.matches;

import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingMatchDto;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.MatchMapper;
import com.bristotartur.gerenciadordepartidas.repositories.MatchRepository;
import com.bristotartur.gerenciadordepartidas.services.people.ParticipantService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <p>Classe responsável por fornecer uma camada de serviços geral para o gerenciamento de instâncias de {@link Match},
 * lidando com aspectos gerais de partidas, como criação, atualizações, validações, etc.</p>
 *
 * <p>Para se comunicar com serviços específicos de especializações de {@link Match}, esta classe utiliza
 * {@link MatchServiceMediator} como uma ponte entre estas classes especializadas.</p>
 *
 * @see MatchMapper
 * @see ParticipantService
 * @see MatchServiceMediator
 */
@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class MatchService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final MatchRepository<Match> matchRepository;
    private final MatchMapper matchMapper;
    private final ParticipantService participantService;
    private final MatchServiceMediator matchServiceMediator;

    /**
     * Retorna uma lista paginada das partidas disponíveis no sistema.
     *
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Uma {@link Page} contendo as partidas para a página especificada.
     */
    public Page<Match> findAllMatches(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var matches = matchRepository.findAll(pageable);

        log.info("Match page of number '{}' and size '{}' was returned.", number, size);
        return matches;
    }

    /**
     * Recupera uma lista paginada contendo todas as instâncias de uma especialização específica de {@link Match}.
     *
     * @param sport Esporte no qual as instâncias retornadas na lista serão especializadas.
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Um {@link Page} contendo todas as instâncias da especialização de {@link Match} definida.
     */
    public Page<? extends Match> findMatchesBySport(Sports sport, Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var matches = matchServiceMediator.findMatchesBySport(sport, pageable);

        log.info("Matches page of number '{}' and size '{}' with type '{}' was returned.", number, size, sport);
        return matches;
    }

    /**
     * Retorna uma lista paginada com todos os jogadores presentes em uma partida.
     *
     * @param id Identificador único da partida.
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Um {@link Page} com todos os jogadores da partida.
     * @throws NotFoundException Caso nenhuma partida correspondente ao ID for encontrada.
     */
    public Page<Participant> findAllMatchPlayers(Long id, Pageable pageable) {

        this.findMatchById(id);

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var players = matchRepository.findMatchPlayers(id, pageable);

        log.info("Player page of number '{}' and size '{}' from Match '{}' was returned.", number, size, id);
        return players;
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

        log.info("Match '{}' of type '{}' was found.", id, matchRepository.findMatchTypeById(id, entityManager));
        return match;
    }

    /**
     * Gera um DTO do tipo {@link ExposingMatchDto} com base na partida fornecida.
     *
     * @param match Partida que terá seus dados mapeados para o DTO.
     * @return Nova instância de {@link ExposingMatchDto} contendo os dados fornecidos.
     */
    public ExposingMatchDto createExposingMatchDto(Match match) {

        var sportType = matchRepository.findMatchTypeById(match.getId(), entityManager);
        var sport = Sports.valueOf(sportType);

        return matchMapper.toNewExposingMatchDto(match, sport);
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

        if (matchDto.teamA().equals(matchDto.teamB()))
            throw new BadRequestException(ExceptionMessages.INVALID_TEAMS_FOR_MATCH.message);

        var players = matchDto.playerIds()
                .stream()
                .map(participantService::findParticipantById)
                .toList();

        this.checkPlayers(players, matchDto);

        var savedMatch = matchMapper.toNewMatch(matchDto, players);
        savedMatch = matchServiceMediator.saveMatch(savedMatch, matchDto.sport());

        log.info("Match '{}' with type '{}' was created.", savedMatch.getId(), matchDto.sport());
        return savedMatch;
    }

    /**
     * Remove uma partida do banco de dados com base no seu ID.
     *
     * @param id Identificador único da partida.
     * @throws NotFoundException Caso nenhuma partida correspondente ao ID seja encontrada.
     */
    public void deleteMatchById(Long id) {

        this.findMatchById(id);
        var sport =  matchRepository.findMatchTypeById(id, entityManager);

        matchRepository.deleteById(id);

        log.info("Match '{}' of type '{}' was deleted.", id, sport);
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

        var existingMatch = this.findMatchById(id);

        if (matchDto.teamA().equals(matchDto.teamB()))
            throw new BadRequestException(ExceptionMessages.INVALID_TEAMS_FOR_MATCH.message);

        var players = matchDto.playerIds()
                .stream()
                .map(participantService::findParticipantById)
                .toList();

        this.checkPlayers(players, matchDto);

        var updatedMatch = matchMapper.toExistingMatch(id, matchDto, players);
        updatedMatch.setTeamScoreA(existingMatch.getTeamScoreA());
        updatedMatch.setTeamScoreB(existingMatch.getTeamScoreB());

        updatedMatch = matchServiceMediator.saveMatch(updatedMatch, matchDto.sport());

        log.info("Match '{}' of type '{}' was updated.", id, matchDto.sport());
        return updatedMatch;
    }

    /**
     * Verifica se os jogadores passados pelo DTO estão aptos a participar da partida, ou seja, se
     * pertencem a alguma das equipes presentes nela.
     *
     * @param players Lista do tipo {@link Participant} contendo os jogadores da partida.
     * @param matchDto DTO do tipo {@link MatchDto} contendo os IDs das equipes presentes na partida.
     * @throws BadRequestException Caso algum jogador não pertença a alguma das equipes.
     */
    private void checkPlayers(List<Participant> players, MatchDto matchDto) {

        Optional<Participant> invalidPlayer = players.stream()
                .filter(player -> !player.getTeam().equals(matchDto.teamA())
                               && !player.getTeam().equals(matchDto.teamB()))
                .findFirst();

        invalidPlayer.ifPresent(player -> {
            throw new BadRequestException(
                    ExceptionMessages.PARTICIPANT_INVALID_FOR_MATCH.message.formatted(player.getId())
            );
        });
    }

}
