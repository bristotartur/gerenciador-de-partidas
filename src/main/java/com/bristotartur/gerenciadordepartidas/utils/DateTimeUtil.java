package com.bristotartur.gerenciadordepartidas.utils;

import com.bristotartur.gerenciadordepartidas.domain.match.structure.Match;
import com.bristotartur.gerenciadordepartidas.enums.ExceptionMessages;
import com.bristotartur.gerenciadordepartidas.enums.Patterns;
import com.bristotartur.gerenciadordepartidas.exceptions.BadRequestException;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class DateTimeUtil {

    public static LocalDateTime parseToPattern(LocalDateTime dateTime) {
        return LocalDateTime.parse(dateTime.format(DateTimeFormatter.ofPattern(Patterns.DATE_TIME.name())));
    }

    public static LocalDateTime toNewMatchTime(LocalDateTime dateTime) {

        LocalDate date = LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());

        return LocalDateTime.of(date, LocalTime.MIN);
    }

}
