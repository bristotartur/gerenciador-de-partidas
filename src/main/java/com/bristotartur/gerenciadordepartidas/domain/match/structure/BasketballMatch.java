package com.bristotartur.gerenciadordepartidas.domain.match.structure;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@DiscriminatorValue("BASKETBALL")
@JsonTypeName("BASKETBALL")
@Data
@RequiredArgsConstructor
public class BasketballMatch extends Match {

}
