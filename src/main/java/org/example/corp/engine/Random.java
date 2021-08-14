package org.example.corp.engine;

import java.util.HashMap;
import java.util.Map;

public class Random {
    private static final java.util.Random random = new java.util.Random();
    private static final Map<Long, java.util.Random> randoms = new HashMap<>();

    public static long gen() {
        return random.nextLong();
    }

    public static long genUnsigned() {
        long l = gen();
        return l < 0 ? -l : l;
    }

    public static long gen(long seed) {
        java.util.Random random;
        if ((random = randoms.get(seed)) == null) {
            random = new java.util.Random(seed);
            randoms.put(seed, random);
        }

        return random.nextLong();
    }

    public static long genUnsigned(long seed) {
        long l = gen(seed);
        return l < 0 ? -l : l;
    }
}
