package aljudy.cs4720_android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 11/23/2014.
 */
public class GameView extends View {

    static final double ONE_BILLION_LOL = 1000000000.0;

    static final int MAX_PLAYER_BUBBLES = 5;

    private Paint bluePaint = new Paint();
    private Paint greenPaint = new Paint();
    private Paint whitePaint = new Paint();
    private ArrayList<Bubble> blueBubbles = new ArrayList<Bubble>();
    private ArrayList<Bubble> greenBubbles = new ArrayList<Bubble>();

    private int blueScore = 0;
    private int greenScore = 0;
    private final int WIN_SCORE = 20;

    private long lastTime;
    private double spawnTimer = 0;
    private List<Bubble> popped = new ArrayList<Bubble>();

    public GameView(Context context) {
        super(context);
        bluePaint.setARGB(255, 0, 0, 255);
        greenPaint.setARGB(255, 0, 255, 0);
        whitePaint.setARGB(255, 255, 255, 255);
        whitePaint.setTextSize(36 * getResources().getDisplayMetrics().density);

        makeBubbles();
        lastTime = System.nanoTime();
    }

    private void makeBubbles() {
        greenBubbles.add(new Bubble());
        blueBubbles.add(new Bubble());
    }

    private void gameUpdate(double delta) {
        spawnTimer += delta;
        if (spawnTimer >= 1.0) {
            makeBubbles();
            spawnTimer -= 1.0;
        }
        while (blueBubbles.size() > MAX_PLAYER_BUBBLES) {
            blueBubbles.remove(0);
        }
        while (greenBubbles.size() > MAX_PLAYER_BUBBLES) {
            greenBubbles.remove(0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long time = System.nanoTime();
        double delta = (time - lastTime) / ONE_BILLION_LOL;
        lastTime = time;
        gameUpdate(delta);
        gameDraw(canvas);
        super.onDraw(canvas);
    }

    protected void gameDraw(Canvas canvas) {

        for (Bubble blue : blueBubbles) {
            canvas.drawCircle(blue.x, blue.y, blue.radius, bluePaint);
        }

        for (Bubble green : greenBubbles) {
            canvas.drawCircle(green.x, green.y, green.radius, greenPaint);
        }

        canvas.drawText(String.valueOf(blueScore), 300, 100, whitePaint);
        canvas.drawText(String.valueOf(greenScore), 600, 100, whitePaint);


        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        for (Bubble bubble : blueBubbles) {
            if (distance(bubble, x, y) <= bubble.radius) {
                Log.d("blueBubbleHit", bubble.toString());
                blueScore++;
                popped.add(bubble);
            }
        }

        for (Bubble bubble : greenBubbles) {
            if (distance(bubble, x, y) <= bubble.radius) {
                Log.d("greenBubbleHit", bubble.toString());
                greenScore++;
                popped.add(bubble);
            }
        }

        blueBubbles.removeAll(popped);
        greenBubbles.removeAll(popped);

        popped.clear();

        return false;
    }

    private double distance(Bubble bubble, float x, float y) {
        return Math.sqrt(Math.pow(bubble.x - x, 2) + Math.pow(bubble.y - y, 2));
    }
}
