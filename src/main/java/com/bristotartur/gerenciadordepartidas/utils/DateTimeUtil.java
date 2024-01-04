package com.bristotartur.gerenciadordepartidas.utils;

import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Patterns;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimeUtil {

    public static LocalDateTime parseToPattern(LocalDateTime dateTime) {
        return LocalDateTime.parse(dateTime.format(DateTimeFormatter.ofPattern(Patterns.DATE_TIME.value)));
    }

    public static LocalDateTime toNewMatchTime(LocalDateTime dateTime) {

        LocalDate date = LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());

        return LocalDateTime.of(date, LocalTime.MIN);
    }

    public static boolean checkPattern(Patterns pattern, LocalDateTime dateTime) {

        if (pattern.equals(Patterns.DATE_TIME))
            return dateTime.toString().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");

        throw new BadRequestException(ExceptionMessages.INVALID_PATTERN.message);
    }

}
