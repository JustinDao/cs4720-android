package edu.virginia.cs.tugowar.game;

import android.content.*;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

/**
 * Created by andy on 12/2/14.
 */
public class ShoutBubble extends Entity {

    final Player player;
    final double maxAge;
    public ShoutBubble(Player player, double x, double y, double length, double maxAge) {
        this.transform.preTranslate((float)x, (float)y);
        float lengthF = (float)length;
        this.transform.preScale(lengthF, lengthF);
        this.player = player;
        this.maxAge = maxAge;
    }
    private void rectangle(Canvas c, Paint p) {
        c.drawRect(-.5f, -.5f, .5f, .5f, p);
    }
    @Override
    protected void onRender(Canvas c, Paint p) {
        p.setColor(player.color);
        p.setStyle(Paint.Style.FILL);
        rectangle(c, p);
        p.setColor(0xFFFFFFFF);
        p.setStrokeWidth(.03f);
        p.setStyle(Paint.Style.STROKE);
        rectangle(c, p);
//        p.setTextSize(1f);
//        p.setTextAlign(Paint.Align.CENTER);
//        c.drawText("!", -0.9f, 0.5f, p);
    }
    @Override
    protected void onUpdate(double delta) {
        if (getAge() > maxAge) {
            destroy();
        }
    }
}
