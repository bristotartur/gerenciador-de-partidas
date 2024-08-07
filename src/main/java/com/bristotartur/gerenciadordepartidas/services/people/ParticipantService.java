package com.bristotartur.gerenciadordepartidas.services.people;

import com.bristotartur.gerenciadordepartidas.domain.matches.Match;
import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.request.RequestParticipantDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Team;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.ParticipantMapper;
import com.bristotartur.gerenciadordepartidas.repositories.ParticipantRepository;
import com.bristotartur.gerenciadordepartidas.services.events.EditionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe responsável por fornecer serviços relacionados a operações CRUD para a entidade {@link Participant},
 * interagindo com o repositório {@link ParticipantRepository} para acessar e manipular dados relacionados
 * a participantes.
 *
 * @see ParticipantMapper
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;
    private final EditionService editionService;

    /**
     * Retorna uma lista paginada dos participantes disponíveis no sistema.
     *
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Uma {@link Page} contendo os participantes para a página especificada.
     */
    public Page<Participant> findAllParticipants(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var participantPage = participantRepository.findAll(pageable);

        log.info("Participant page of number '{}' and size '{}' was returned.", number, size);
        return participantPage;
    }

    /**
     * Retorna uma lista paginada dos participantes que tenham um nome semelhante ao fornecido.
     *
     * @param name Nome usado para a busca.
     * @return Uma lista contendo todos os participantes cujo o nome se assemelhe ao nome passado como parâmetro.
     */
    public Page<Participant> findParticipantsByNameLike(String name, Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var participantPage = participantRepository.findParticipantsByNameLike(name, pageable);

        log.info("Participant page of number '{}' and size '{}' with name like '{}' was returned.", name, number, size);
        return participantPage;
    }

    /**
     * Procura por todos os membros de uma determinada equipe.
     *
     * @param team equipe do participante.
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Uma {@link Page} contendo todos os participantes relacionados a equipe
     * @throws NotFoundException Se nenhuma equipe correspondente ao nome fornecido for encontrada.
     */
    public Page<Participant> findMambersFromTeam(Team team, Pageable pageable) {

        var membersPage = participantRepository.findTeamMembers(team, pageable);

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        log.info("Members page of number '{}' and size '{}' from team '{}' was returned.", number, size, team);

        return membersPage;
    }

    public Page<Match> findParticipantMatches(Long id, Pageable pageable) {

        this.findParticipantById(id);
        var matchPage = participantRepository.findParticipantMatches(id, pageable);

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        log.info("Match page of number '{}' and size '{}' from Participant '{}' was returned.", number, size, id);

        return matchPage;
    }

    /**
     * Busca por uma entidade específica do tipo {@link Participant} com base no seu ID.
     *
     * @param id Identificador único do participant.
     * @return O participante correspondente ao ID fornecido.
     * @throws NotFoundException Caso nenhum participante correspondente ao ID for encontrado.
     */
    public Participant findParticipantById(Long id) {

        var participant = participantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.PARTICIPANT_NOT_FOUND.message));

        log.info("Participant '{}' with name '{}' was found.", participant.getId(), participant.getName());
        return participant;
    }

    /**
     * Salva um participante no sistema com base nos dados fornecidos em {@link RequestParticipantDto}, realizando
     * uma validação prévia destes dados antes de gerar o participante e persistí-lo.
     *
     * @param requestParticipantDto DTO do tipo {@link RequestParticipantDto} contendo os dados do participante a ser salvo.
     * @return O participante recém-salvo.
     * @throws NotFoundException Caso alguma entidade não corresponda aos IDs fornecidos por {@link RequestParticipantDto}.
     * @throws BadRequestException Caso o número da turma do participante seja inválido.
     */
    public Participant saveParticipant(RequestParticipantDto requestParticipantDto) {

        var edition = editionService.findEditionById(requestParticipantDto.editionId());
        var participant = participantMapper.toNewParticipant(requestParticipantDto, edition);

        this.reformatClassNumber(participant);
        var savedParticipant = participantRepository.save(participant);

        log.info("Participant '{}' with name '{}' was created.", savedParticipant.getId(), savedParticipant.getName());
        return savedParticipant;
    }

    /**
     * Remove um participante da base de dados com base no seu ID. Esta operação só poderá ser utilizada ao
     * adicionar participantes por engano ao sistema pois, ao associá-los a qualquer tipo de evento, eles não
     * poderão mais ser removidos.
     *
     * @param id Identificador único do participante.
     * @throws NotFoundException Caso o ID fornecido não corresponda a nenhum participante.
     * @throws BadRequestException Caso o participante já esteja atrelado a algum evento.
     */
    public void deleteParticipantById(Long id) {

        var participant = this.findParticipantById(id);
        var participantMatches = participantRepository.findMatchesByParticipantId(id);

        if (!participantMatches.isEmpty()) {
            throw new BadRequestException(ExceptionMessages.INVALID_PARTICIPANT_EXCLUSION_OPERATION.message);
        }
        participantRepository.deleteById(id);
        log.info("Participant '{}' with name '{}' was deleted.", id, participant.getName());
    }

    /**
     * Atualiza um participante existente no banco de dados com base no seu ID e os dados fornecidos
     * em {@link RequestParticipantDto} realizando uma validação prévia destes dados antes de atualizar o participante.
     * Isso envolve a substituição completa dos dados do participante existente pelos novos dados fornecidos.
     *
     * @param id Identificador único do participante.
     * @param requestParticipantDto DTO do tipo {@link RequestParticipantDto} contendo os dados do participante a ser salvo.
     * @return O participante atualizado.
     * @throws NotFoundException Caso alguma entidade não corresponda aos IDs fornecidos por {@link RequestParticipantDto}.
     * @throws BadRequestException Caso o número da turma do participante seja inválido.
     */
    public Participant replaceParticipant(Long id, RequestParticipantDto requestParticipantDto) {

        this.findParticipantById(id);

        var edition = editionService.findEditionById(requestParticipantDto.editionId());
        var participant = participantMapper.toExistingParticipant(id, requestParticipantDto, edition);

        this.reformatClassNumber(participant);
        var updatedParticipant = participantRepository.save(participant);

        log.info("Participant '{}' with name '{}' was updated.", id, updatedParticipant.getName());
        return updatedParticipant;
    }

    /**
     * Verifica se o número da turma de um participante é válido e o reformata caso necessário.
     *
     * @param participant O participante contendo o número da turma.
     * @throws BadRequestException Caso o número da turma do participante seja inválido.
     */
    private void reformatClassNumber(Participant participant) {

        var classNumber = participant.getClassNumber();
        var regex = "^[1-3]-?\\d{2}$";

        if (!classNumber.matches(regex)) {
            throw new BadRequestException(ExceptionMessages.INVALID_PATTERN.message.formatted(classNumber));
        }
        if (!classNumber.contains("-")) {
            classNumber = classNumber.charAt(0) + "-" + classNumber.substring(1);
        }
        participant.setClassNumber(classNumber);
    }

}
