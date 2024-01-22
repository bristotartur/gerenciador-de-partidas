package com.bristotartur.gerenciadordepartidas.domain.events;

import com.bristotartur.gerenciadordepartidas.domain.actions.Goal;
import com.bristotartur.gerenciadordepartidas.domain.actions.PenaltyCard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@DiscriminatorValue("FUTSAL")
@JsonTypeName("FUTSAL")
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class FutsalMatch extends Match {

    @JsonManagedReference
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Goal> goals;

    @JsonManagedReference
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PenaltyCard> penaltyCards;

}
