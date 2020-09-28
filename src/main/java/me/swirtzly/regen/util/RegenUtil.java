package me.swirtzly.regen.util;

import java.util.Random;

public class RegenUtil {

    public static Random RAND = new Random();

    public static float randFloat(float min, float max) {
        return RAND.nextFloat() * (max - min) + min;
    }

}
