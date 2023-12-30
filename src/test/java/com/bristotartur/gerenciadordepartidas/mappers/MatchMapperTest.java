package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.ChessMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.FootballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.MatchSport;
import com.bristotartur.gerenciadordepartidas.domain.team.Team;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.MatchStatus;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.services.GeneralMatchSportService;
import com.bristotartur.gerenciadordepartidas.services.TeamService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.bristotartur.gerenciadordepartidas.utils.RandomIdUtil.getRandomLongId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MatchMapperTest {

    @Mock
    private TeamService teamService;
    @Mock
    private GeneralMatchSportService generalMatchSportService;
    @InjectMocks
    private MatchMapper matchMapper;

    private MatchDto matchDto;
    private Match existingMatch;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        matchDto = new MatchDto(Sports.FOOTBALL,
                getRandomLongId(),
                getRandomLongId(),
                getRandomLongId(),
                5,
                1,
                Modality.FEMININE,
                MatchStatus.ENDED,
                LocalDateTime.of(2023, 12, 30, 14, 20, 0),
                LocalDateTime.of(2023, 12, 30, 14, 50, 0));

        existingMatch = Match.builder()
                .id(getRandomLongId())
                .matchSport(createChessMatch())
                .teamA(createTeam())
                .teamB(createTeam())
                .teamScoreA(0)
                .teamScoreB(1)
                .modality(Modality.FEMININE.name)
                .matchStatus(MatchStatus.IN_PROGRESS.name)
                .matchStart(LocalDateTime.of(2023, 12, 30, 14, 20, 0))
                .matchEnd(LocalDateTime.of(2023, 12, 30, 0, 0, 0))
                .build();
    }

    private ChessMatch createChessMatch() {
        return ChessMatch.builder().id(getRandomLongId()).build();
    }

    private Team createTeam() {
        return Team.builder().id(getRandomLongId()).build();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(teamService, generalMatchSportService);
    }

    @Test
    @DisplayName("Should create new MatchSport when MatchDto is mapped to new Match")
    void Should_CreateNewMatchSport_When_MatchDtoIsMappedToNewMatch() {

        var mockMatchSport = new FootballMatch();

        when(generalMatchSportService.newMatchSport(matchDto.sport())).thenReturn(mockMatchSport);

        var match = matchMapper.toNewMatch(matchDto);

        assertNotNull(match.getMatchSport());
        assertInstanceOf(MatchSport.class, match.getMatchSport());
    }

    @Test
    @DisplayName("Should find Team existing Team id is passed")
    void Should_FindTeam_When_ExistingTeamIdIsPassed() {

        var mockTeamA = Team.builder().id(matchDto.teamAId()).build();
        var mockTeamB = Team.builder().id(matchDto.teamBId()).build();

        when(teamService.findTeamById(matchDto.teamAId())).thenReturn(mockTeamA);
        when(teamService.findTeamById(matchDto.teamBId())).thenReturn(mockTeamB);

        var match = matchMapper.toNewMatch(matchDto);

        assertNotNull(match.getTeamA());
        assertNotNull(match.getTeamB());
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing Team id is passed to new Match")
    void Should_ThrowNotFoundException_When_NonExistingTeamIdIsPassed() {

        when(teamService.findTeamById(matchDto.teamAId()))
                .thenThrow(new NotFoundException(ExceptionMessages.TEAM_NOT_FOUND.message));

        assertThrows(NotFoundException.class, () -> matchMapper.toNewMatch(matchDto));
    }

    @Test
    @DisplayName("Should set Teams score to 0 when MatchDto is mapped to new Match")
    void Should_SetTeamsScoreToZero_When_MatchDtoIsMappedToNewMatch() {

        var match = matchMapper.toNewMatch(matchDto);

        assertEquals(0, match.getTeamScoreA());
        assertEquals(0, match.getTeamScoreB());
    }

    @Test
    @DisplayName("Should set matchStart and matchEnd fields to 0 when MatchDto is mapped to new Match")
    void Should_SetTimeOfMatchStartAndMatchEndFieldsToZero_When_MatchDtoIsMappedToNewMatch() {

        var match = matchMapper.toNewMatch(matchDto);

        assertEquals(LocalTime.of(0, 0, 0), match.getMatchStart().toLocalTime());
        assertEquals(LocalTime.of(0, 0, 0), match.getMatchEnd().toLocalTime());
    }

    @Test
    @DisplayName("Should update Match fields when different values are mapped")
    void Should_UpdateMatchFields_When_DifferentValuesAreMapped() {

        var existingId = existingMatch.getId();
        var match = matchMapper.toExistingMatch(existingId, matchDto);

        assertNotEquals(existingMatch.getMatchSport(), match.getMatchSport());
        assertNotEquals(existingMatch.getTeamA(), match.getTeamA());
        assertNotEquals(existingMatch.getTeamB(), match.getTeamB());
        assertNotEquals(existingMatch.getTeamScoreA(), match.getTeamScoreA());
        assertNotEquals(existingMatch.getMatchStatus(), match.getMatchStatus());
        assertNotEquals(existingMatch.getMatchEnd(), match.getMatchEnd());
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing Team id is passed to existing Match")
    void Should_ThrowNotFoundException_When_NonExistingTeamIdIsPassedToExistingMatch() {

        long randomId = getRandomLongId();

        when(teamService.findTeamById(matchDto.teamAId()))
                .thenThrow(new NotFoundException(ExceptionMessages.TEAM_NOT_FOUND.message));

        assertThrows(NotFoundException.class, () -> matchMapper.toExistingMatch(randomId, matchDto));
    }

    @Test
    @DisplayName("Should throw NotFoundException when non existing MatchSport id is passed to existing Match")
    void Should_ThrowNotFoundException_When_NonExistingMatchSportIdIsPassedToExistingMatch() {

        long randomId = getRandomLongId();

        when(generalMatchSportService.findMatchSport(matchDto.sport(), matchDto.matchSportId()))
                .thenThrow(new NotFoundException(ExceptionMessages.FOOTBALL_MATCH_NOT_FOUND.message));

        assertThrows(NotFoundException.class, () -> matchMapper.toExistingMatch(randomId, matchDto));
    }

    @Test
    @DisplayName("Should not update Match fields when equal values are mapped")
    void Should_NotUpdateMatchFields_When_EqualValuesAreMapped() {

        var existingId = existingMatch.getId();
        var match = matchMapper.toExistingMatch(existingId, matchDto);

        assertEquals(existingMatch.getId(), match.getId());
        assertEquals(existingMatch.getTeamScoreB(), match.getTeamScoreB());
        assertEquals(existingMatch.getModality(), match.getModality());
        assertEquals(existingMatch.getMatchStart(), match.getMatchStart());
    }
}