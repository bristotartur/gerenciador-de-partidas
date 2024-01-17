package com.bristotartur.gerenciadordepartidas.domain.match.structure;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@DiscriminatorValue("VOLLEYBALL")
@JsonTypeName("VOLLEYBALL")
@Data
@RequiredArgsConstructor
public class VolleyballMatch extends Match {

}
