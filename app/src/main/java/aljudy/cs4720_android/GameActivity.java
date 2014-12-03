package aljudy.cs4720_android;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import java.io.IOException;

import edu.virginia.cs.tugowar.game.GameUtils;
import edu.virginia.cs.tugowar.game.GameplayView;


public class GameActivity extends Activity {

    private double MIC_MAX_AMPLITUDE = 20*1000;
    private MediaRecorder mic = null;

    private void initMic() {
        if (mic != null) return;
        mic = new MediaRecorder();
        mic.setAudioSource(MediaRecorder.AudioSource.MIC);
        mic.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mic.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mic.setOutputFile("/dev/null");
        try {
            mic.prepare();
            mic.start();
        } catch (IOException e) {
            mic = null;
            Log.e("GameActivity", "IOException booting up mic!");
            System.exit(0);
        }
    }

    private void killMic() {
        if (mic != null) {
            MediaRecorder temp = mic;
            mic = null;
            temp.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        killMic();
    }

    @Override
    public void onResume() {
        super.onResume();
        initMic();
    }

    public int getMicAmp() {
        if (mic != null) {
            return mic.getMaxAmplitude(); // / (double)MIC_MAX_AMPLITUDE;
        } else {
            return 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        GameUtils.DPI = metrics.xdpi;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        initMic();
        setContentView(new GameplayView(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
