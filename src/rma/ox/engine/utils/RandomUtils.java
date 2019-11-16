package rma.ox.engine.utils;

import java.util.Random;

public class RandomUtils {

    static Random r = new Random();

    public static int getRandomInt(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return r.nextInt((max - min) + 1) + min;
    }

    public static float getRandomFloat(float min, float max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return r.nextFloat() * (max - min) + min;
    }
}
