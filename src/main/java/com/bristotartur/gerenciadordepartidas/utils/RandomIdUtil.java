package com.bristotartur.gerenciadordepartidas.utils;

import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomIdUtil {

    public static Long getRandomLongId() {

        Long timestamp = System.currentTimeMillis();
        Random random = new Random();

        return random.nextLong() + timestamp;
    }
}
