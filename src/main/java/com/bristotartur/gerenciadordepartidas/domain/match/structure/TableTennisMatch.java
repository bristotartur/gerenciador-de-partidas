package com.bristotartur.gerenciadordepartidas.domain.match.structure;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@DiscriminatorValue("TÊNIS DE MESA")
@Data
@RequiredArgsConstructor
public class TableTennisMatch extends Match {

}
