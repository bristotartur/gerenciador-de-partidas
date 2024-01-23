package com.bristotartur.gerenciadordepartidas.domain.events;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * Classe filha de {@link Match} especializada em partidas de tênis de mesa.
 */
@Entity
@DiscriminatorValue("TABLE TENNIS")
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class TableTennisMatch extends Match {

}
