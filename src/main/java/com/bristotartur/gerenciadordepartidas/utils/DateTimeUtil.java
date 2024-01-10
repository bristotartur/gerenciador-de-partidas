package com.bristotartur.gerenciadordepartidas.utils;

import com.bristotartur.gerenciadordepartidas.enums.Patterns;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {

    private DateTimeUtil() {
    }

    public static LocalDateTime parseToPattern(LocalDateTime dateTime) {
        return LocalDateTime.parse(dateTime.format(DateTimeFormatter.ofPattern(Patterns.DATE_TIME.value)));
    }

    public static LocalDateTime toNewMatchTime(LocalDateTime dateTime) {
        LocalDate date = LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());
        return LocalDateTime.of(date, LocalTime.MIN);
    }

}
