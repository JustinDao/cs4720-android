package edu.virginia.cs.tugowar.game;

/**
 * Created by andy on 11/24/14.
 */
public class GameUtils {
    public static double DPI = 72;
    public static double randomDouble(double min, double max) {
        return min + Math.random()*(max-min);
    }
}
