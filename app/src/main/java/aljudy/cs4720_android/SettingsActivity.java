package aljudy.cs4720_android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String ip = preferences.getString("RPi_IP_Address", "0.0.0.0");

        TextView ipText = (TextView) findViewById(R.id.ipText);
        ipText.setText(ip);
        ((EditText) findViewById(R.id.ipEdit)).setText(ip);

        Button applyButton = (Button) findViewById(R.id.applyIP);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyIP();
            }
        });

        Button rpiButton = (Button) findViewById(R.id.rpiButton);
        rpiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRPi();
            }
        });
    }

    public void applyIP() {
        String ip = ((EditText) findViewById(R.id.ipEdit)).getText().toString();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("RPi_IP_Address", ip); // value to store
        editor.commit();
        Log.d("commit", preferences.getString("RPi_IP_Address", "fail"));
        ((TextView) findViewById(R.id.ipText)).setText(ip);
    }

    public void startRPi() {
        Intent i = new Intent(getApplicationContext(), RPiActivity.class);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
