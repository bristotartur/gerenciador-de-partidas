package com.bristotartur.gerenciadordepartidas.services;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.FootballMatch;
import com.bristotartur.gerenciadordepartidas.exceptions.NotFoundException;
import com.bristotartur.gerenciadordepartidas.repositories.FootballMatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FootballMatchService {

    private final FootballMatchRepository footballMatchRepository;
    private static final String NOT_FOUND_MESSAGE = "Partida de futebol não encontrada";

    private FootballMatch findFootballMatchById(Long id) {

        return footballMatchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
    }
}
