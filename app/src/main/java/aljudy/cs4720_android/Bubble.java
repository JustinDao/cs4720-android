package aljudy.cs4720_android;

import java.util.Random;

/**
 * Created by Justin on 11/23/2014.
 */
public class Bubble {

    public float x;
    public float y;
    public float radius;

    private final int SCREEN_WIDTH = 1080;
    private final int SCREEN_HEIGHT = 1920;

    public Bubble()
    {
        Random rand = new Random();
        x = rand.nextInt(SCREEN_WIDTH);
        y = rand.nextInt(SCREEN_HEIGHT);
        radius = 100;
    }

    public String toString()
    {
        return "(" + x + ", " + y + "); Radius: " + radius;
    }
}