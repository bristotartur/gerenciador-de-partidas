package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.enums.TeamName;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.TeamRepository;
import com.bristotartur.gerenciadordepartidas.services.people.TeamService;
import com.bristotartur.gerenciadordepartidas.utils.ParticipantTestUtil;
import com.bristotartur.gerenciadordepartidas.utils.TeamTestUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeamServiceTest {

    @Autowired
    private TeamService teamService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TeamRepository teamRepository;

    @Test
    @DisplayName("Should retrieve all Teams from database when searching for all Teams")
    void Should_RetrieveAllTeamsFromDatabase_When_SearchingForAllTeams() {

        var teams = List.of(
                TeamTestUtil.createNewTeam(TeamName.ATOMICA, entityManager),
                TeamTestUtil.createNewTeam(TeamName.MESTRES_DE_OBRAS, entityManager),
                TeamTestUtil.createNewTeam(TeamName.PAPA_LEGUAS, entityManager));

        var result = teamService.findAllTeams();

        assertEquals(result, teams);
    }

    @Test
    @DisplayName("Should find Team when existing Team ID is passed to search")
    void Should_FindTeam_When_ExistingTeamIdIsPassedToSearch() {

        var team = TeamTestUtil.createNewTeam(TeamName.TWISTER, entityManager);
        var result = teamService.findTeamById(team.getId());

        assertEquals(result, team);
    }

    @Test
    @DisplayName("Should find Team when existing TeamName is passed to search")
    void Should_FindTeam_When_ExistingTeamNameIsPassedToSearch() {

        var teamName = TeamName.ATOMICA;
        var team = TeamTestUtil.createNewTeam(teamName, entityManager);
        var result = teamService.findTeamByName(teamName);

        assertEquals(result, team);
    }

    @Test
    @DisplayName("Should throw NotFoundException when invalid TeamName is passed to search")
    void Should_ThrowNotFoundException_When_InvalidTeamNameIsPassedToSearch() {

        var name = TeamName.MESTRES_DE_OBRAS;

        assertThrows(NotFoundException.class, () -> {
            teamService.findTeamByName(name);
        });
    }

    @Test
    @DisplayName("Should retrieve all Team members when Team ID is passed to search Team members")
    void Should_RetrieveAllTeamMembers_When_TeamIdIsPassedToSearchTeamMembers() {

        var team = TeamTestUtil.createNewTeam(TeamName.PAPA_LEGUAS, entityManager);
        var teamMembers = List.of(
                ParticipantTestUtil.createNewParticipant("1-41" ,team, entityManager),
                ParticipantTestUtil.createNewParticipant("2-41" ,team, entityManager),
                ParticipantTestUtil.createNewParticipant("3-41" ,team, entityManager));

        var result = teamService.findAllTeamMembers(team.getId());

        assertEquals(result, teamMembers);
    }

    @Test
    @DisplayName("Should save team when valid TeamDto is passed to save")
    void Should_SaveTeam_When_ValidTeamDtoIsPassedToSave() {

        var teamDto = TeamTestUtil.createNewTeamDto(TeamName.TWISTER, 1000);
        var result = teamService.saveTeam(teamDto);

        assertEquals(result, teamRepository.findById(result.getId()).get());
    }

    @Test
    @DisplayName("Should throw BadRequestException when already in use TeamName is passed to save")
    void Should_ThrowBadRequestException_When_AlreadyInUseTeamNameIsPassedToSave() {

        var teamName = TeamName.UNICONTTI;
        var teamDto = TeamTestUtil.createNewTeamDto(teamName, 1000);
        teamService.saveTeam(teamDto);

        assertThrows(BadRequestException.class, () -> {
            teamService.saveTeam(teamDto);
        });
    }

    @Test
    @DisplayName("Should update Team when valid TeamDto with new values is passed to update")
    void Should_UpdateTeam_When_ValidTeamDtoWithNewValuesIsPassedToUpdate() {

        var teamDto = TeamTestUtil.createNewTeamDto(TeamName.MESTRES_DE_OBRAS, 500);
        var team = teamService.saveTeam(teamDto);

        entityManager.detach(team);

        var updatedTeamDto = TeamTestUtil.createNewTeamDto(TeamName.PAPA_LEGUAS, 1000);
        var result = teamService.replaceTeam(team.getId(), updatedTeamDto);

        assertNotEquals(result, team);
    }

    @Test
    @DisplayName("Should throw BadRequestException when already in use TeamName is passed to update")
    void Should_ThrowBadRequestException_When_AlreadyInUseTeamNameIsPassedToUpdate() {

        var teamName = TeamName.TWISTER;
        var teamDto = TeamTestUtil.createNewTeamDto(teamName, 1000);
        var existingTeamA = teamService.saveTeam(teamDto);

        var newTeamDto = TeamTestUtil.createNewTeamDto(TeamName.UNICONTTI, 500);
        var existingTeamB = teamService.saveTeam(newTeamDto);


        assertThrows(BadRequestException.class, () -> {
            teamService.replaceTeam(existingTeamB.getId(), teamDto);
        });
    }

    @Test
    @DisplayName("Should throw NotFoundException when invalid Team ID is passed to any method")
    void Should_ThrowNotFoundException_When_InvalidTeamIdIsPassedToAnyMethod() {

        var id = getRandomLongId();
        var teamDto = TeamTestUtil.createNewTeamDto(TeamName.UNICONTTI, 500);

        assertThrows(NotFoundException.class, () -> {
            teamService.findTeamById(id);
        });
        assertThrows(NotFoundException.class, () -> {
            teamService.findAllTeamMembers(id);
        });
        assertThrows(NotFoundException.class, () -> {
            teamService.replaceTeam(id, teamDto);
        });
    }

}