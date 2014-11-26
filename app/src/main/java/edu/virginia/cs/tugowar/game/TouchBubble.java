package edu.virginia.cs.tugowar.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by andy on 11/24/14.
 */
public class TouchBubble extends Entity {
    final static double STROKE_WIDTH = 10;
    final static double SQRT2 = Math.sqrt(2);

    public final Player player;
    public final double maxAge;
    public final int color;
    public final double baseRadius;

    final Vector2 position;

    public TouchBubble(Player player, double x, double y, double baseRadius, double maxAge) {
        this.player = player;
        this.maxAge = maxAge;
        this.color = player.color;
        this.position = new Vector2(x, y);
        this.baseRadius = baseRadius;
    }

    @Override
    protected void onUpdate(double delta) {
        if (this.getAge() >= this.maxAge) {
            this.destroy();
            return;
        }
    }

    private double getRadius() {
        double percent = getPercentRemaining();
        // percent *= percent; // remove this for faster shrinking
        return Math.max(percent*this.baseRadius, 0) + getStrokeWidth()/2.0;
    }

    private double getPercentRemaining() {
        double percent = this.getAge()/this.maxAge;
        return 1-percent;
    }

    private double getStrokeWidth() {
        return STROKE_WIDTH * getPercentRemaining();
    }

    @Override
    protected void onRender(Canvas canvas, Paint paint) {
        double r = getRadius();
        paint.setColor(this.color);
        canvas.drawCircle((float)position.x, (float)position.y, (float)r, paint);
        paint.setColor(0x80FFFFFF);
        canvas.drawCircle(
            (float)(position.x-r/2/SQRT2),
            (float)(position.y-r/2/SQRT2),
            (float)(r/2.0),
            paint
        );
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xFF000000);
        paint.setStrokeWidth((float)getStrokeWidth());
        canvas.drawCircle((float)position.x, (float)position.y, (float)getRadius(), paint);
    }

    @Override
    protected void onTouchEvent(MotionEvent event) {
        double dist = position.getDistance(event.getX(), event.getY());
        if (dist <= getRadius()) {
            player.score++;
            player.bonusBubbles++;
            destroy();
        }
    }
}
