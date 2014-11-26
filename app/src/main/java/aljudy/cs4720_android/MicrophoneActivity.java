package aljudy.cs4720_android;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import java.io.IOException;


public class MicrophoneActivity extends Activity {

    private MediaRecorder mRecorder = null;
    private ProgressBar micProgress = null;
    private Thread t;

    public void start() throws IOException {
        Log.d("debug", "calling start");
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            mRecorder.prepare();
            mRecorder.start();
        }
    }

    public void stop() {
        Log.d("debug", "calling stop");
        if (mRecorder != null) {
            MediaRecorder temp = mRecorder;
            mRecorder = null;
            temp.stop();
//            temp.release();
        }
    }

    public int getAmplitude() {
        if (mRecorder != null)
            return  mRecorder.getMaxAmplitude();
        else
            return 0;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microphone);

        micProgress = (ProgressBar) findViewById(R.id.micProgress);
        micProgress.setMax(20000);

        try {
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        t = new Thread() {
            @Override
            public void run() {
                while (mRecorder != null) {
                    try {
                        int amp = getAmplitude();
                        if (amp > 20000) amp = 20000;
                        final int famp = amp;
//                        Log.d("amp", String.valueOf(amp));
                        micProgress.post(new Runnable() {
                           @Override
                            public void run() {
                               micProgress.setProgress(famp);
                           }
                        });
                        Thread.sleep(50);
                    } catch (Exception e) {
                        Log.e("errorz", e.toString());
                    }
                }

            }

        };

        t.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_microphone, menu);
        return true;
    }

    @Override
    public void onPause(){
        super.onPause();
        this.stop();

    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            this.start();
            t = new Thread() {
                @Override
                public void run() {
                    while (mRecorder != null) {
                        try {
                            int amp = getAmplitude();
                            if (amp > 20000) amp = 20000;
                            final int famp = amp;
//                        Log.d("amp", String.valueOf(amp));
                            micProgress.post(new Runnable() {
                                @Override
                                public void run() {
                                    micProgress.setProgress(famp);
                                }
                            });
                            Thread.sleep(50);
                        } catch (Exception e) {
                            Log.e("errorz", e.toString());
                        }
                    }

                }

            };

            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
