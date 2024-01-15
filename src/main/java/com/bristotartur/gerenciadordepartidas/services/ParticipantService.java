package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.participant.Participant;
import com.bristotartur.gerenciadordepartidas.dtos.ParticipantDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.ParticipantMapper;
import com.bristotartur.gerenciadordepartidas.repositories.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

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
        return participantRepository.findAll();
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

        return participant;
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

        return participantRepository.save(participant);
    }

    /**
     * Remove um participante do banco de dados com base no seu ID.
     *
     * @param id Identificador único do participante.
     */
    public void deleteParticipantById(Long id) {
        participantRepository.deleteById(id);
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

        return participantRepository.save(participant);
    }

    /**
     * Verifica se um número da turma de um participante é válido e o reformata caso necessário.
     *
     * @param participant O participante contendo o número da turma.
     * @return O número da turma reformatado.
     * @throws BadRequestException Caso o número da turma do participante seja inválido.
     */
    private void reformatClassNumber(Participant participant) {

        var classNumber = participant.getClassNumber();
        var regex = "^[1-3]-?\\d{2}$";

        if (!(Pattern.matches(regex, classNumber)))
            throw new BadRequestException(ExceptionMessages.INVALID_PATTERN.message.formatted(classNumber));

        if (!(classNumber.contains("-")))
            classNumber = classNumber.substring(0, 1) + "-" + classNumber.substring(1);

        participant.setClassNumber(classNumber);
    }

}
