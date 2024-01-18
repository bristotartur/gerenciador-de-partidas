package com.bristotartur.gerenciadordepartidas.domain.structure;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@DiscriminatorValue("TABLE TENNIS")
@JsonTypeName("TABLE TENNIS")
@Data
@RequiredArgsConstructor
public class TableTennisMatch extends Match {

}
