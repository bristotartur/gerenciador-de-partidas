package com.bristotartur.gerenciadordepartidas.utils;

import java.util.Random;

public final class RandomIdUtil {

    private RandomIdUtil() {
    }

    public static Long getRandomLongId() {

        Long timestamp = System.currentTimeMillis();
        Random random = new Random();

        return random.nextLong() + timestamp;
    }
}
