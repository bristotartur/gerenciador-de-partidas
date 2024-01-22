package com.bristotartur.gerenciadordepartidas.domain.events;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Entity
@DiscriminatorValue("TABLE TENNIS")
@JsonTypeName("TABLE TENNIS")
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class TableTennisMatch extends Match {

}
