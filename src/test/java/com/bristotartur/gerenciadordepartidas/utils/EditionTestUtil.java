package com.bristotartur.gerenciadordepartidas.utils;

import com.bristotartur.gerenciadordepartidas.domain.events.Edition;
import com.bristotartur.gerenciadordepartidas.enums.Status;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

public final class EditionTestUtil {

    private EditionTestUtil() {
    }

    public static Edition createNewEdition(Status status) {

        return Edition.builder()
                .atomicaScore(0)
                .mestresScore(0)
                .papaScore(0)
                .twisterScore(0)
                .uniconttiScore(0)
                .editionStatus(status)
                .opening(LocalDate.now())
                .closure(LocalDate.now())
                .build();
    }

    public static Edition createNewEdition(Status status, EntityManager entityManager) {

        var edition =  Edition.builder()
                .atomicaScore(0)
                .mestresScore(0)
                .papaScore(0)
                .twisterScore(0)
                .uniconttiScore(0)
                .editionStatus(status)
                .opening(LocalDate.now())
                .closure(LocalDate.now())
                .build();

        entityManager.merge(edition);
        return edition;
    }

}
