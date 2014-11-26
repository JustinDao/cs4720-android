package edu.virginia.cs.tugowar.game;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by andy on 11/24/14.
 */
public class ScoreCounter extends Entity {
    public final Player player;
    public final double size;
    public ScoreCounter(Player player, double x, double y, double degrees, double size) {
        this.player = player;
        transform.preTranslate((float)x, (float)y);
        transform.preRotate((float) degrees);
        this.size = size;
    }
    @Override
    public void onRender(Canvas c, Paint p) {
        p.setColor(player.color);
        p.setTextSize((float) size);
        p.setTextAlign(Paint.Align.CENTER);
        c.drawText("" + player.score, 0, 0, p);
    }
}
