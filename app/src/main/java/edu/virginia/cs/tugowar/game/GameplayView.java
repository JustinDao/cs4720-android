package edu.virginia.cs.tugowar.game;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import aljudy.cs4720_android.WinActivity;

/**
 * Created by Justin on 11/23/2014.
 */
public class GameplayView extends View {

    static final int WIN_SCORE_DIFFERENCE = 16;
    static final double ONE_BILLION_LOL = 1000000000.0;
    static final int MAX_PLAYER_BUBBLES = 5;
    static final double MAX_DELTA = 1.0/30;
    static final double BUBBLE_INTERVAL = 0.5;

    double inch;
    float inchf;

    private boolean initialized = false;
    private boolean gameOver = false;
    private String winner = "";
    private int winColor = 0xFFFFFFFF;
    private int oldScoreDifference = 32;

    final Paint paint = new Paint();
    final EntitySet bubbles = new EntitySet();
    final EntitySet hud = new EntitySet();

    private Text winnerText;

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
        if (!gameOver)
        {

            int scoreDifference = players[0].score - players[1].score;

            if (oldScoreDifference != scoreDifference)
            {
                oldScoreDifference = scoreDifference;
                new LightJSON().execute();
            }

            if (scoreDifference > WIN_SCORE_DIFFERENCE)
            {
                // Blue Wins
                Log.d("winner", "Blue Wins!");
                gameOver = true;
                winner = "Blue Wins!";
                winColor = players[0].color;
                bubbles.clear();
                return;
            }
            else if (scoreDifference < -WIN_SCORE_DIFFERENCE)
            {
                // Green Wins
                Log.d("winner", "Green Wins!");
                gameOver = true;
                winner = "Green Wins!";
                winColor = players[1].color;
                bubbles.clear();
                return;
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
        else
        {
            Activity host = (Activity) this.getContext();
            Intent intent = new Intent(host, WinActivity.class);
            intent.putExtra("EXTRA_WINNER", winner);
            intent.putExtra("EXTRA_COLOR", winColor);
            host.startActivity(intent);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!initialized) {
            init();
        }

        if(gameOver) {
            // TODO: Draw winner here instead of bubbles
            long time = System.nanoTime();
            double delta = (time - lastTime) / ONE_BILLION_LOL;
            delta = Math.min(delta, MAX_DELTA);
            lastTime = time;
            gameUpdate(delta);
            gameDraw(canvas);
        }
        else {
            long time = System.nanoTime();
            double delta = (time - lastTime) / ONE_BILLION_LOL;
            delta = Math.min(delta, MAX_DELTA);
            lastTime = time;
            gameUpdate(delta);
            gameDraw(canvas);
        }

        invalidate();
        super.onDraw(canvas);
    }

    protected void gameDraw(Canvas canvas) {
        if(!gameOver)
        {
            bubbles.render(canvas, paint);
            hud.render(canvas, paint);
        }

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!gameOver)
        {
            bubbles.touchEvent(event);
        }

        return false;
    }

    // Light Code

    class LightJSON extends AsyncTask<String, Void, Integer> {
        protected Integer doInBackground(String... params) {
            try {
                int scoreDifference = players[0].score - players[1].score;
                applyColors(scoreDifference);
            } catch (Exception e) {
                Log.e("error", "Fail", e);
            }

            return 0;
        }
    }

    String getIP() {
        // TODO: Put IP in settings
        return "192.168.0.25";
    }

    String getLightJSON(int scoreDifference) {

        String json = "";

        if (scoreDifference == 0)
        {
            json = "{'lights': [{'lightId':1,'red':0,'green':0,'blue':0,'intensity': 0}], 'propagate': true}";
        }
        else if (scoreDifference > 0)
        {
            // Blue is Winning
            json = "{'lights': [";

            for(int i = 31; i > 15; i--)
            {
                json += "{'lightId':" + i + ",'red':0,'green':0,'blue':0,'intensity': 0.5},";
            }

            for (int i = 15; i > 15-scoreDifference; i--) {
                if (i < 0)
                {
                    break;
                }

                json += "{'lightId':" + i + ",'red':0,'green':0,'blue':255,'intensity': 0.5}";

                if ( (i - 1) > 15-scoreDifference)
                {
                    json += ",";
                }
            }

            for(int i = 15-scoreDifference; i >= 0; i--)
            {
                json += ",{'lightId':" + i + ",'red':0,'green':0,'blue':0,'intensity': 0.5}";
            }

            json += "], 'propagate': false}";
        }
        else if (scoreDifference < 0)
        {
            scoreDifference = scoreDifference * -1;
            // Green is Winning
            json = "{'lights': [";

            for(int i = 0; i < 16; i++)
            {
                json += "{'lightId':" + i + ",'red':0,'green':0,'blue':0,'intensity': 0.5},";
            }

            for (int i = 16; i < 16+scoreDifference; i++) {
                if (i > 31)
                {
                    break;
                }

                json += "{'lightId':" + i + ",'red':0,'green':255,'blue':0,'intensity': 0.5}";

                if ( (i + 1) < 16+scoreDifference)
                {
                    json += ",";
                }
            }

            for(int i = 16+scoreDifference; i < 32; i++)
            {
                json += ",{'lightId':" + i + ",'red':0,'green':0,'blue':0,'intensity': 0.5}";
            }

            json += "], 'propagate': false}";
        }

        return json.replace("'", "\"");
    }

    void applyColors(int scoreDifference) {
        String msg = "Successfully updated lights!";
        try {
            String json = getLightJSON(scoreDifference);
            Log.d("json", json);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://" + getIP() + "/rpi");
            StringEntity wtf = new StringEntity(json);
            wtf.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "text/json"));
            post.setEntity(wtf);
            post.setHeader("Content-Type", "text/json");
            client.execute(post);

        } catch (Exception e) {
            msg = "Could not update lights :-(";
            Log.e("error", msg, e);

        }
    }

}
