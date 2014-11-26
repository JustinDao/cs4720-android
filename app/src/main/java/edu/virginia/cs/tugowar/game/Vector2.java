package edu.virginia.cs.tugowar.game;

/**
 * Created by andy on 11/24/14.
 */
public class Vector2 {
    public double x;
    public double y;
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Vector2(Vector2 other) {
        this(other.x, other.y);
    }
    public Vector2() {
        this(0,0);
    }
    public double getLength() {
        return Math.sqrt(x*x + y*y);
    }
    public double getDistance(double ox, double oy) {
        double dx = ox - this.x;
        double dy = oy - this.y;
        return Math.sqrt(dx*dx + dy*dy);
    }
    public double getDistance(Vector2 o) {
        return getDistance(o.x, o.y);
    }
}
