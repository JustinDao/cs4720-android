package edu.virginia.cs.tugowar.game;

import java.util.Random;

/**
 * Created by andy on 11/24/14.
 */
public class GameUtils {
    public static Random rng = new Random();
    public static double DPI = 72;
    public static double randomDouble(double min, double max) {
        return min + Math.random()*(max-min);
    }
    public static int randomInt(int min, int max) { // INCLUSIVE
        return min + rng.nextInt(max - min + 1);
    }
}
