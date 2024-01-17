package com.bristotartur.gerenciadordepartidas.domain.match.structure;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@DiscriminatorValue("CHESS")
@JsonTypeName("CHESS")
@Data
@RequiredArgsConstructor
public class ChessMatch extends Match {

}
