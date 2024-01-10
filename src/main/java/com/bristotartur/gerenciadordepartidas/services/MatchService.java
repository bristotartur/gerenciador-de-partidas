package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.dtos.MatchDto;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.mappers.MatchMapper;
import com.bristotartur.gerenciadordepartidas.repositories.MatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final GeneralMatchSportService matchSportService;

    public List<Match> findAllMatches() {
        return matchRepository.findAll();
    }

    public Match findMatchById(Long id) {

        var match = matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessages.MATCH_NOT_FOUND.message));

        return match;
    }

    public Match saveMatch(MatchDto matchDto) {

        var savedMatch = matchRepository.save(matchMapper.toNewMatch(matchDto));
        return savedMatch;
    }

    public void deleteMatchById(Long id) {
        matchRepository.deleteById(id);
    }

    public Match replaceMatch(Long id, MatchDto matchDto) {

        this.findMatchById(id);

        var match = matchMapper.toExistingMatch(id, matchDto);
        var updatedMatch = matchRepository.save(match);

        return updatedMatch;
    }

}
