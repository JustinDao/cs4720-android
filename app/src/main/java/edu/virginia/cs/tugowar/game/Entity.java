package edu.virginia.cs.tugowar.game;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by andy on 11/24/14.
 */
public abstract class Entity {
    // private variables
    private double age = 0;
    private boolean destroyed = false;

    // public variables
    public boolean antiAlias = true;
    public final Matrix transform = new Matrix();

    // overrides
    protected void onUpdate(double delta) {}
    protected void onRender(Canvas canvas, Paint paint) {}
    protected void onTouchEvent(MotionEvent event) {}

    // finalized behavior
    public final void update(double delta) {
        this.onUpdate(delta);
        this.age += delta;
    }
    public final void render(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.concat(transform);
        paint.setAntiAlias(antiAlias);
        this.onRender(canvas, paint);
        paint.reset();
        canvas.restore();
    }
    public final void touchEvent(MotionEvent event) {
        this.onTouchEvent(event);
    }
    public final void destroy() {
        this.destroyed = true;
    }
    public final double getAge() {
        return age;
    }
    public final boolean isDestroyed() {
        return this.destroyed;
    }
}
