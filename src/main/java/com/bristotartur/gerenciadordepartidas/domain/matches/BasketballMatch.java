package com.bristotartur.gerenciadordepartidas.domain.matches;

import com.bristotartur.gerenciadordepartidas.domain.actions.PenaltyCard;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Classe filha de {@link Match} especializada em partidas de basquete.
 *
 * @see PenaltyCard
 */
@Entity
@DiscriminatorValue("BASKETBALL")
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class BasketballMatch extends Match {

    @JsonManagedReference
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PenaltyCard> penaltyCards;

}
