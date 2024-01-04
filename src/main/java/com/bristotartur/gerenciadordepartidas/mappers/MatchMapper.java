package com.bristotartur.gerenciadordepartidas.mappers;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.MatchStatus;
import com.bristotartur.gerenciadordepartidas.services.GeneralMatchSportService;
import com.bristotartur.gerenciadordepartidas.services.TeamService;
import com.bristotartur.gerenciadordepartidas.utils.DateTimeUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class MatchMapper {

    private final TeamService teamService;
    private final GeneralMatchSportService generalMatchSportService;

    public Match toNewMatch(MatchDto matchDto) {

        return Match.builder()
                .matchSport(generalMatchSportService.newMatchSport(matchDto.sport()))
                .teamA(teamService.findTeamById(matchDto.teamAId()))
                .teamB(teamService.findTeamById(matchDto.teamBId()))
                .teamScoreA(0)
                .teamScoreB(0)
                .modality(matchDto.modality().name)
                .matchStatus(MatchStatus.SCHEDULED.name)
                .matchStart(DateTimeUtil.toNewMatchTime(matchDto.matchStart()))
                .matchEnd(DateTimeUtil.toNewMatchTime(matchDto.matchEnd()))
                .build();
    }

    public Match toExistingMatch(Long id, MatchDto matchDto) {

        return Match.builder()
                .id(id)
                .teamA(teamService.findTeamById(matchDto.teamAId()))
                .teamB(teamService.findTeamById(matchDto.teamBId()))
                .teamScoreA(matchDto.teamScoreA())
                .teamScoreB(matchDto.teamScoreB())
                .modality(matchDto.modality().name)
                .matchStatus(matchDto.matchStatus().name)
                .matchStart(matchDto.matchStart())
                .matchEnd(matchDto.matchEnd())
                .build();
    }

}
