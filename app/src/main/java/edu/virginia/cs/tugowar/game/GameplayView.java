package edu.virginia.cs.tugowar.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Justin on 11/23/2014.
 */
public class GameplayView extends View {

    static final int WIN_SCORE_DIFFERENCE = 20;
    static final double ONE_BILLION_LOL = 1000000000.0;
    static final int MAX_PLAYER_BUBBLES = 5;
    static final double MAX_DELTA = 1.0/30;
    static final double BUBBLE_INTERVAL = 0.5;

    double inch;
    float inchf;

    private boolean initialized = false;

    final Paint paint = new Paint();
    final EntitySet bubbles = new EntitySet();
    final EntitySet hud = new EntitySet();

    // ok ok this is kind of ugly. w/e
    Player[] players = new Player[] {
        new Player(0xFF0000FF),
        new Player(0XFF00FF00)
    };
    Player[] playersReverse = new Player[] {
        players[1],
        players[0]
    };
    boolean flip = false;

    private long lastTime;
    private double spawnTimer = 0;

    public GameplayView(Context context) {
        super(context);
    }

    private void init() {
        inch = GameUtils.DPI;
        inchf = (float)inch;
        lastTime = System.nanoTime();
        int i = 0;
        for (Player player : players) {
            float scoreX = getWidth()/2f;
            float scoreY = i == 0 ? getHeight()-inchf/2f : inchf/2f;
            float scoreR = i*180;
            float size = inchf;
            ScoreCounter sc = new ScoreCounter(player, scoreX, scoreY, scoreR, size);
            hud.add(sc);
            i++;
        }
        initialized = true;
    }

    private void makeOneBubble(Player p) {
        double r = GameUtils.DPI;
        double rx = GameUtils.randomDouble(r, getWidth()-r);
        double ry = GameUtils.randomDouble(r, getHeight()-r);
        double duration = 0.75;
        bubbles.add(new TouchBubble(p, rx, ry, r, duration));
    }

    private void makeBubbles() {
        Player[] dudes = flip ? players : playersReverse;
        for (Player p : dudes) {
            makeOneBubble(p);
        }
        flip = !flip;
    }

    private void gameUpdate(double delta) {
        if(Math.abs(players[0].score - players[1].score) >= WIN_SCORE_DIFFERENCE)
        {
            // Intent
            // startActivity
            // return
        }

        spawnTimer += delta;
        if (spawnTimer >= BUBBLE_INTERVAL) {
            makeBubbles();
            spawnTimer -= BUBBLE_INTERVAL;
        }
        bubbles.update(delta);
        for (Player p : players) {
            while (p.bonusBubbles > 0) {
                p.bonusBubbles--;
                this.makeOneBubble(p);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!initialized) {
            init();
        }
        long time = System.nanoTime();
        double delta = (time - lastTime) / ONE_BILLION_LOL;
        delta = Math.min(delta, MAX_DELTA);
        lastTime = time;
        gameUpdate(delta);
        gameDraw(canvas);
        invalidate();
        super.onDraw(canvas);
    }

    protected void gameDraw(Canvas canvas) {

        bubbles.render(canvas, paint);
        hud.render(canvas, paint);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        bubbles.touchEvent(event);

        return false;
    }

}
