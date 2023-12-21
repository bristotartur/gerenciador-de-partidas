package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.FootballMatch;
import com.bristotartur.gerenciadordepartidas.domain.match.structure.HandballMatch;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.FootballMatchRepository;
import com.bristotartur.gerenciadordepartidas.repositories.HandballMatchRepository;


public class MatchSportService {

    private static FootballMatchRepository footballMatchRepository;
    private static HandballMatchRepository handballMatchRepository;
    private static final String FOOTBALL_MATCH_NOT_FOUND_MESSAGE = "Partida de futebol não encontrada";
    private static final String HANDBALL_MATCH_NOT_FOUND_MESSAGE = "Partida de handebol não encontrada";

    public FootballMatch findFootballMatchById(Long id) {

        return footballMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(FOOTBALL_MATCH_NOT_FOUND_MESSAGE));
    }

    public static HandballMatch findHandballMatchById(Long id) {

        return handballMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(HANDBALL_MATCH_NOT_FOUND_MESSAGE));
    }
}
