package com.bristotartur.gerenciadordepartidas.utils;

import java.util.Random;

public final class RandomIdUtil {

    private RandomIdUtil() {
    }

    public static Long getRandomLongId() {

        long timestamp = System.currentTimeMillis();
        Random random = new Random();

        return Math.abs(random.nextLong() + timestamp);
    }
}
