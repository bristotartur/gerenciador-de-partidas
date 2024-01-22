package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.people.Participant;
import com.bristotartur.gerenciadordepartidas.domain.people.Team;
import com.bristotartur.gerenciadordepartidas.dtos.TeamDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.TeamMapper;
import com.bristotartur.gerenciadordepartidas.repositories.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Classe responsável por fornecer serviços relacionados a operações CRUD para a entidade {@link Team},
 * interagindo com o repositório {@link TeamRepository} para acessar e manipular os dados
 * relacionados a equipes.
 */
@Service
@AllArgsConstructor
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    /**
     * Recupera todas as equipes disponíveis no sistema.
     *
     * @return Uma lista contendo todas as equipes.
     */
    public List<Team> findAllTeams() {
        return teamRepository.findAll();
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

        return team;
    }

    /**
     * Retorna todos os membros de uma equipe.
     *
     * @param id Identificador único da equipe.
     * @return Uma lista contendo todos os membros da equipe.
     */
    public List<Participant> findAllTeamMembers(Long id) {

        this.findTeamById(id);
        return teamRepository.findTeamMembers(id);
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

        if (!teamRepository.findByName(teamName).isEmpty())
            throw new BadRequestException(ExceptionMessages.NAME_ALREADY_IN_USE.message.formatted(teamName));

        var savedTeam = teamRepository.save(teamMapper.toNewTeam(teamDto));
        return savedTeam;
    }

    /**
     * Remove uma equipe do sistema com base em seu ID.
     *
     * @param id Identificador único da equipe a ser removida.
     * @throws NotFoundException Se nenhuma equipe correspondente ao ID fornecido for encontrada.
     */
    public void deleteTeamById(Long id) {

        this.findTeamById(id);
        teamRepository.deleteById(id);
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

        var originalTeamName = findTeamById(id).getName();
        var newTeamName = teamDto.teamName().value;
        var teamWithSameName = teamRepository.findByName(newTeamName);

        if (teamWithSameName.isPresent() && !newTeamName.equals(originalTeamName))
            throw new BadRequestException(ExceptionMessages.NAME_ALREADY_IN_USE.message.formatted(newTeamName));

        var team = teamMapper.toExistingTeam(id, teamDto);
        return teamRepository.save(team);
    }

}
