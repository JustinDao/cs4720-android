package edu.virginia.cs.tugowar.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Justin on 12/2/2014.
 */
public class Text extends Entity {
    public final String text;
    public final int color;
    public final double size;

    public Text(String text, int color, double size, double x, double y, double degrees) {
        this.text = text;
        this.color = color;
        this.size = size;
        transform.preTranslate((float)x, (float)y);
        transform.preRotate((float) degrees);
    }

    @Override
    public void onRender(Canvas c, Paint p) {
        p.setColor(color);
        p.setTextSize((float) size);
        p.setTextAlign(Paint.Align.CENTER);
        c.drawText(text, 0, 0, p);
    }
}
