package com.bristotartur.gerenciadordepartidas.domain.events;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * Classe filha de {@link Match} especializada em partidas de xadrez.
 */
@Entity
@DiscriminatorValue("CHESS")
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class ChessMatch extends Match {

}
