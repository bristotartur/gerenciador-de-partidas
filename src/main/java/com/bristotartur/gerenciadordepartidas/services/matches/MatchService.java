package com.bristotartur.gerenciadordepartidas.services.matches;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestMatchDto;
import com.bristotartur.gerenciadordepartidas.dtos.response.ResponseMatchDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.MatchMapper;
import com.bristotartur.gerenciadordepartidas.repositories.MatchRepository;
import com.bristotartur.gerenciadordepartidas.services.events.SportEventService;
import com.bristotartur.gerenciadordepartidas.services.people.ParticipantService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private final SportEventService sportEventService;

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
     * Busca por todas as partidas relacionadas a um evento esportivo específico.
     *
     * @param sportEventId Identificador único do evento esportivo.
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Um {@link Page} contendo todas as partidas relacionadas ao evento esportivo especificado.
     */
    public Page<? extends Match> findMatchesBySportEvent(Long sportEventId, Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var matches = sportEventService.findEventById(sportEventId).getMatches();

        log.info("Match page of number '{}' and size '{}' from SportEvent '{}' was returned.", number, size, sportEventId);
        return new PageImpl<>(matches, pageable, matches.size());
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
     * Gera um DTO do tipo {@link ResponseMatchDto} com base na partida fornecida.
     *
     * @param match Partida que terá seus dados mapeados para o DTO.
     * @return Nova instância de {@link ResponseMatchDto} contendo os dados fornecidos.
     */
    public ResponseMatchDto createExposingMatchDto(Match match) {

        var sportType = matchRepository.findMatchTypeById(match.getId(), entityManager);
        var sport = Sports.valueOf(sportType);

        return matchMapper.toNewExposingMatchDto(match, sport);
    }

    /**
     * Salva uma partida no sistema com base nos dados fornecidos em {@link RequestMatchDto}, realizando uma validação
     * prévia destes dados antes de gerar a partida e persistí-la.
     *
     * @param requestMatchDto DTO do tipo {@link RequestMatchDto} contendo os dados da partida a ser salva.
     * @return A partida recém-salva.
     * @throws NotFoundException Caso alguma entidade não corresponda aos IDs fornecidos por {@link RequestMatchDto}.
     * @throws BadRequestException Caso a seleção das equipes ou jogadores seja irregular.
     */
    public Match saveMatch(RequestMatchDto requestMatchDto) {

        var event = sportEventService.findEventAndCheckStatus(requestMatchDto.eventId());
        var players = this.findPlayersById(requestMatchDto.playerIds());

        this.creatingAndUpdatingValidations(requestMatchDto, event, players);

        var match = matchMapper.toNewMatch(requestMatchDto, players, event);
        var savedMatch = matchServiceMediator.saveMatch(match, requestMatchDto.sport());

        log.info("Match '{}' with type '{}' was created.", savedMatch.getId(), requestMatchDto.sport());
        return savedMatch;
    }

    /**
     * Remove uma partida do banco de dados com base no seu ID.
     *
     * @param id Identificador único da partida.
     * @throws NotFoundException Caso nenhuma partida correspondente ao ID seja encontrada.
     */
    public void deleteMatchById(Long id) {

        var match = this.findMatchById(id);
        sportEventService.findEventAndCheckStatus(match.getEvent().getId());

        if (!match.getMatchStatus().equals(Status.SCHEDULED)) {
            throw new BadRequestException(ExceptionMessages.INVALID_MATCH_OPERATION.message);
        }
        var sport = matchRepository.findMatchTypeById(id, entityManager);
        matchRepository.deleteById(id);

        log.info("Match '{}' of type '{}' was deleted.", id, sport);
    }

    /**
     * Atualiza uma partida existente no banco de dados com base no seu ID e os dados fornecidos em {@link RequestMatchDto},
     * realizando uma validação prévia destes dados antes de atualizar a partida. Isso envolve a substituição
     * completa dos dados da partida existente pelos novos dados fornecidos.
     *
     * @param id Identificador único da partida a ser atualizada.
     * @param requestMatchDto DTO do tipo {@link RequestMatchDto} contendo os dados atualizados da partida.
     * @return A partida atualizada.
     * @throws NotFoundException Caso nenhuma partida correspondente ao ID for encontrada ou
     * alguma entidade não corresponda aos IDs fornecidos por {@link RequestMatchDto}.
     * @throws BadRequestException Caso a seleção das equipes ou jogadores seja irregular.
     */
    public Match replaceMatch(Long id, RequestMatchDto requestMatchDto) {

        var existingMatch = this.findMatchById(id);

        if (!existingMatch.getMatchStatus().equals(Status.SCHEDULED)) {
            throw new BadRequestException(ExceptionMessages.INVALID_MATCH_OPERATION.message);
        }
        var event = sportEventService.findEventAndCheckStatus(requestMatchDto.eventId());
        var players = this.findPlayersById(requestMatchDto.playerIds());

        this.creatingAndUpdatingValidations(requestMatchDto, event, players);

        var match = matchMapper.toExistingMatch(id, requestMatchDto, existingMatch, players, event);
        var updatedMatch = matchServiceMediator.saveMatch(match, requestMatchDto.sport());

        log.info("Match '{}' of type '{}' was updated.", id, requestMatchDto.sport());
        return updatedMatch;
    }

    /**
     * Atualiza o {@link Status} de uma instância de {@link Match} existente no sistema com base no seu ID
     * e em um novo status passado como parâmetro.
     *
     * @param id Identificador único da partida.
     * @param newMatchStatus Novo status da partida.
     * @return A partida com o status atuaizado.
     * @throws NotFoundException Caso nenhuma partida correspondente ao ID for encontrada.
     * @throws BadRequestException Caso o status informado seja inválido ou a partida não possa ser atuaizada.
     */
    public Match updateMatchStatus(Long id, Status newMatchStatus) {

        var match = this.findMatchById(id);
        Status.checkStatus(match.getMatchStatus(), newMatchStatus);

        if (!match.getMatchStatus().equals(newMatchStatus)) {
            var event = match.getEvent();
            MatchValidator.checkMatchStatus(event, newMatchStatus);
        }
        match.setMatchStatus(newMatchStatus);

        var sport = Sports.findSportLike(matchRepository.findMatchTypeById(id, entityManager));
        var updatedMatch = matchServiceMediator.saveMatch(match, sport);

        log.info("Match '{}' had the status updated to '{}'.", id, newMatchStatus);
        return updatedMatch;
    }

    /**
     * Procura os jogadores correspondentes aos IDs da listagem passada para o método.
     *
     * @param playersIds Listagem dos IDs dos jogadores.
     * @return Uma lista contendo os jogadores corespondentes aos IDs.
     * @throws NotFoundException Caso o jogador corresponde a algum ID não seja encontrado.
     */
    private List<Participant> findPlayersById(List<Long> playersIds) {

        return playersIds.stream()
                .map(participantService::findParticipantById)
                .toList();
    }

    /**
     * Realiza as validações necessárias para criar e atualizar partidas. A função deste método é tornar o código
     * dos métodos relacionados a criação e atualização de partidas mais conciso, melhorando sua legibilidade.
     *
     * @param dto DTO do tipo {@link RequestMatchDto} contendo os dados da partida a ser criada ou atualizada.
     * @param event Evento relacionado a partida.
     * @param players Listagem dos jogadores relacionados a partida.
     */
    private void creatingAndUpdatingValidations(RequestMatchDto dto, SportEvent event, List<Participant> players) {

        MatchValidator.checkTeamsForMatch(dto);
        MatchValidator.checkMatchForSportEvent(event, dto);
        MatchValidator.checkMatchImportance(event, dto);
        MatchValidator.checkPlayersForMatch(players, dto);
    }

}
