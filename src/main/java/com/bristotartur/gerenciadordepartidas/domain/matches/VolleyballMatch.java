package com.bristotartur.gerenciadordepartidas.domain.matches;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * Classe filha de {@link Match} especializada em partidas de vôlei.
 */
@Entity
@DiscriminatorValue("VOLLEYBALL")
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class VolleyballMatch extends Match {

}
