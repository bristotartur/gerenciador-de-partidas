package com.bristotartur.gerenciadordepartidas.domain.structure;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Entity
@DiscriminatorValue("CHESS")
@JsonTypeName("CHESS")
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class ChessMatch extends Match {

}
