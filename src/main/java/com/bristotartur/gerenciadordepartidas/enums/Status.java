package com.bristotartur.gerenciadordepartidas.enums;

import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {
    SCHEDULED("Agendado"),
    IN_PROGRESS("Em andamento"),
    ENDED("Encerrado"),
    OPEN_FOR_EDITS("Aberto para edições");

    public final String name;

    public static Status findStatusLike(String status) {

        var formatedStatus = status.replace("-", "_").toUpperCase();

        try {
            return valueOf(formatedStatus);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(ExceptionMessages.INVALID_STATUS.message, e);
        }
    }

    public static void checkStatus(Status originalStatus, Status newStatus) {

        if (originalStatus.equals(newStatus)) return;

        if (originalStatus.equals(SCHEDULED) && !newStatus.equals(IN_PROGRESS)) {
            throw new BadRequestException("Status 'SCHEDULED' só pode ser alterado para 'IN_PROGESS'.");
        }
        if (originalStatus.equals(IN_PROGRESS) && !newStatus.equals(ENDED)) {
            throw new BadRequestException("Status 'IN_PROGRESS' só pode ser alterado para 'ENDED'.");
        }
        if (originalStatus.equals(ENDED) || originalStatus.equals(OPEN_FOR_EDITS)) {
            if (!newStatus.equals(ENDED) && !newStatus.equals(OPEN_FOR_EDITS)) {
                throw new BadRequestException("Status 'ENDED' e 'OPEN_FOR_EDITS' só podem ser alterados para eles mesmos.");
            }
        }
    }

}
