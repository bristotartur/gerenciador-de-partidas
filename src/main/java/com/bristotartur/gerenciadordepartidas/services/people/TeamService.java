package com.bristotartur.gerenciadordepartidas.services.people;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.dtos.ExposingTeamDto;
import com.bristotartur.gerenciadordepartidas.dtos.TeamDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.TeamMapper;
import com.bristotartur.gerenciadordepartidas.repositories.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe responsável por fornecer serviços relacionados a operações CRUD para a entidade {@link Team},
 * interagindo com o repositório {@link TeamRepository} para acessar e manipular os dados
 * relacionados a equipes.
 */
@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    /**
     * Retorna uma lista paginada de todas as equipes disponíveis no sistema.
     *
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Uma {@link Page} contendo as equipes para página especificada.
     */
    public Page<Team> findAllTeams(Pageable pageable) {

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        var teamPage = teamRepository.findAll(pageable);

        log.info("Team page of number '{}' and size '{}' was returned.", number, size);
        return teamPage;
    }

    /**
     * Procura por todos os membros de uma determinada equipe.
     *
     * @param id Identificador único da equipe.
     * @param pageable Um {@link Pageable} contendo informações sobre a paginação.
     * @return Uma {@link Page} contendo todos os participantes relacionados a equipe
     * @throws NotFoundException Se nenhuma equipe correspondente ao nome fornecido for encontrada.
     */
    public Page<Participant> findAllTeamMembers(Long id, Pageable pageable) {

        var team = this.findTeamById(id);
        var membersPage = teamRepository.findTeamMembers(id, pageable);

        var number = pageable.getPageNumber();
        var size = pageable.getPageSize();
        log.info("Members page of number '{}' and size '{}' from team '{}' was returned.", number, size, id);

        return membersPage;
    }

    /**
     * Busca uma entidade específica do tipo {@link Team} com base em seu ID.
     *
     * @param id Identificador único da equipe.
     * @return A equipe correspondente ao ID fornecido.
     * @throws NotFoundException Se nenhuma equipe correspondente ao ID fornecido for encontrada.
     */
    public Team findTeamById(Long id) {

        var team = teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.TEAM_NOT_FOUND.message));

        log.info("Team '{}' with name '{}' was found.", id, team.getName());
        return team;
    }

    /**
     * Busca uma entidade específica do tipo {@link Team} com base em seu nome.
     *
     * @param name O nome da equipe.
     * @return A equipe correspondente ao nome.
     * @throws NotFoundException Se nenhuma equipe correspondente ao nome fornecido for encontrada.
     */
    public Team findTeamByName(TeamName name) {

        var team = teamRepository.findByName(name.value)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.TEAM_NOT_FOUND.message));

        log.info("Team '{}' with name '{}' was found.", team.getId(), name);
        return team;
    }

    /**
     * Gera um DTO do tipo {@link ExposingTeamDto} com base na equipe fornecida.
     *
     * @param team Equipe que terá seus dados mapeados para o DTO.
     * @return Uma nova instância de {@link ExposingTeamDto} contendo os dados fornecidos.
     */
    public ExposingTeamDto createExposingTeamDto(Team team) {
        return teamMapper.toNewExposingTeamDto(team);
    }

    /**
     * Salva uma nova equipe no sistema com base nos dados fornecidos em {@link TeamDto}.
     *
     * @param teamDto DTO do tipo {@link TeamDto} contendo dados da equipe a serem salvos.
     * @return A equipe recém-salva.
     * @throws BadRequestException Caso o nome da equipe passado pelo DTO já esteja em uso.
     */
    public Team saveTeam(TeamDto teamDto) {

        var teamName = teamDto.teamName().value;

        if (teamRepository.findByName(teamName).isPresent())
            throw new BadRequestException(ExceptionMessages.NAME_ALREADY_IN_USE.message.formatted(teamName));

        var savedTeam = teamRepository.save(teamMapper.toNewTeam(teamDto));

        log.info("Team '{}' with name '{}' was created.", savedTeam.getId(), savedTeam.getName());
        return savedTeam;
    }

    /**
     * Atualiza uma equipe existente no sistema com base em seu ID e os dados fornecidos em {@link TeamDto}.
     * Isso envolve a substituição completa dos dados da equipe existente pelos novos dados fornecidos.
     *
     * @param id Identificador único da equipe a ser atualizada.
     * @param teamDto DTO do tipo {@link TeamDto} contendo dados atualizados da equipe.
     * @return A equipe atualizada.
     * @throws NotFoundException Se nenhuma equipe correspondente ao ID fornecido for encontrada.
     * @throws BadRequestException Caso o nome da equipe passado pelo DTO já esteja em uso.
     */
    public Team replaceTeam(Long id, TeamDto teamDto) {

        var originalTeamName = this.findTeamById(id).getName();
        var newTeamName = teamDto.teamName().value;
        var teamWithSameName = teamRepository.findByName(newTeamName);

        if (teamWithSameName.isPresent() && !newTeamName.equals(originalTeamName))
            throw new BadRequestException(ExceptionMessages.NAME_ALREADY_IN_USE.message.formatted(newTeamName));

        var updatedTeam = teamRepository.save(teamMapper.toExistingTeam(id, teamDto));

        log.info("Team '{}' with name '{}' was updated.", id, updatedTeam.getName());
        return updatedTeam;
    }

}
