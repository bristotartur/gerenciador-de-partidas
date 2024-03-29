package com.bristotartur.gerenciadordepartidas.dtos.input;

import com.bristotartur.gerenciadordepartidas.domain.events.SportEvent;
import com.bristotartur.gerenciadordepartidas.enums.Modality;
import com.bristotartur.gerenciadordepartidas.enums.Sports;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SportEventDto(@NotNull Sports type,
                            @NotNull Modality modality,
                            @NotNull Integer totalMatches,
                            @NotNull Long editionId) implements TransferableEventData<SportEvent> {
}
