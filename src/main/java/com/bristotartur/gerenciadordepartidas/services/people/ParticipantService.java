package com.bristotartur.gerenciadordepartidas.services.people;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingParticipantDto;
import com.bristotartur.gerenciadordepartidas.dtos.ParticipantDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.ParticipantMapper;
import com.bristotartur.gerenciadordepartidas.repositories.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Classe responsável por fornecer serviços relacionados a operações CRUD para a entidade {@link Participant},
 * interagindo com o repositório {@link ParticipantRepository} para acessar e manipular dados relacionados
 * a participantes.
 *
 * @see ParticipantMapper
 * @see TeamService
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;
    private final TeamService teamService;

    /**
     * Retorna todos os participantes disponíveis no banco de dados.
     *
     * @return Uma lista contendo todos os participantes.
     */
    public List<Participant> findAllParticipants() {

        List<Participant> participants = participantRepository.findAll();

        log.info("List with all Participants was found.");
        return participants;
    }

    /**
     * Retorna todos os participantes que tenham um nome semelhante ao fornecido.
     *
     * @param name Nome usado para a busca.
     * @return Uma lista contendo todos os participantes cujo o nome se assemelhe ao nome passado como parâmetro.
     */
    public List<Participant> findParticipantsByNameLike(String name) {

        List<Participant> participants = participantRepository.findParticipantsByNameLike(name);

        log.info("List with all Participants with name like '{}' was found.", name);
        return participants;
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
     * Gera um DTO do tipo {@link ExposingParticipantDto} com base no participante fornecido.
     *
     * @param participant Participante terá seus dados mapeados para o DTO.
     * @return Uma nova instância de {@link ExposingParticipantDto} contendo os dados fornecidos.
     */
    public ExposingParticipantDto createExposingParticipantDto(Participant participant) {
        return participantMapper.toNewExposingParticipantDto(participant);
    }

    /**
     * Salva um participante no sistema com base nos dados fornecidos em {@link ParticipantDto}, realizando
     * uma validação prévia destes dados antes de gerar o participante e persistí-lo.
     *
     * @param participantDto DTO do tipo {@link ParticipantDto} contendo os dados do participante a ser salvo.
     * @return O participante recém-salvo.
     * @throws NotFoundException Caso alguma entidade não corresponda aos IDs fornecidos por {@link ParticipantDto}.
     * @throws BadRequestException Caso o número da turma do participante seja inválido.
     */
    public Participant saveParticipant(ParticipantDto participantDto) {

        var team = teamService.findTeamById(participantDto.teamId());
        var participant = participantMapper.toNewParticipant(participantDto, team);

        this.reformatClassNumber(participant);
        var savedParticipant = participantRepository.save(participant);

        log.info("Participant '{}' with name '{}' was created.", savedParticipant.getId(), savedParticipant.getName());
        return savedParticipant;
    }

    /**
     * Remove um participante do banco de dados com base no seu ID.
     *
     * @param id Identificador único do participante.
     * @throws NotFoundException caso o ID fornecido não corresponda a nenhum participante.
     */
    public void deleteParticipantById(Long id) {

        var participant = this.findParticipantById(id);
        participantRepository.deleteById(id);

        log.info("Participant '{}' with name '{}' was deleted.", id, participant.getName());
    }

    /**
     * Atualiza um participante existente no banco de dados com base no seu ID e os dados fornecidos
     * em {@link ParticipantDto} realizando uma validação prévia destes dados antes de atualizar o participante.
     * Isso envolve a substituição completa dos dados do participante existente pelos novos dados fornecidos.
     *
     * @param id Identificador único do participante.
     * @param participantDto DTO do tipo {@link ParticipantDto} contendo os dados do participante a ser salvo.
     * @return O participante atualizado.
     * @throws NotFoundException Caso alguma entidade não corresponda aos IDs fornecidos por {@link ParticipantDto}.
     * @throws BadRequestException Caso o número da turma do participante seja inválido.
     */
    public Participant replaceParticipant(Long id, ParticipantDto participantDto) {

        this.findParticipantById(id);

        var team = teamService.findTeamById(participantDto.teamId());
        var participant = participantMapper.toExistingParticipant(id, participantDto, team);

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

        if (!classNumber.matches(regex))
            throw new BadRequestException(ExceptionMessages.INVALID_PATTERN.message.formatted(classNumber));

        if (!classNumber.contains("-"))
            classNumber = classNumber.substring(0, 1) + "-" + classNumber.substring(1);

        participant.setClassNumber(classNumber);
    }

}
