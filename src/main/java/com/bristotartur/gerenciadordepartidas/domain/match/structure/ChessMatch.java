package com.bristotartur.gerenciadordepartidas.domain.match.structure;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@DiscriminatorValue("XADREZ")
@Data
@RequiredArgsConstructor
public class ChessMatch extends Match {

}
