package aljudy.cs4720_android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;


public class RPiActivity extends Activity {

    int[] sliderIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpi);

        this.sliderIds = new int[] {R.id.redBar, R.id.greenBar, R.id.blueBar, R.id.brightnessBar};
        Button applyButton = (Button)this.findViewById(R.id.button);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread() {
                    @Override
                    public void run() {
//                        Looper.prepare();
                        applyColors();
                    }
                };
                t.start();
            }
        });
    }

    int getSliderValue(int barIdx) {
        return ((SeekBar)this.findViewById(this.sliderIds[barIdx])).getProgress();
    }

    String getIP() {
        return ((EditText)this.findViewById(R.id.editText)).getText().toString();
    }

    String getLightJSON() {
        int rVal = getSliderValue(0);
        int gVal = getSliderValue(1);
        int bVal = getSliderValue(2);
        float bright = getSliderValue(3)/100f;
        String json = "{'lights': [{'lightId':1,'red':" + rVal + ",'green':" + gVal + ",'blue':" +
                bVal + ",'intensity':" + bright + "}], 'propagate': true}";
        return json.replace("'", "\"");
    }

    void applyColors() {
        String msg = "Successfully updated lights!";
        try {
            Log.d("json", getLightJSON());

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://" + getIP() + "/rpi");
            String json = getLightJSON();
            StringEntity wtf = new StringEntity(json);
            wtf.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "text/json"));
            post.setEntity(wtf);
            post.setHeader("Content-Type", "text/json");
            client.execute(post);

        } catch (Exception e) {
            // pokemon programming
            // GOTTA CATCH EM ALL
            Log.e("pokemon", "i wanna be the very best", e);
            msg = "Could not update lights :-(";
        } finally {
//            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }


        //http://www.xyzws.com/Javafaq/how-to-use-httpurlconnection-post-data-to-web-server/139
//        URL url;
//        HttpURLConnection connection = null;
//        try {
//            //Create connection
//            String json = getLightJSON();
//
//            url = new URL("http://192.168.0.25/rpi");
//            connection = (HttpURLConnection)url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type",
//                    "text/json");
//
//            connection.setRequestProperty("Content-Length", "" +
//                    Integer.toString(json.length()));
//            connection.setRequestProperty("Content-Language", "en-US");
//
//            connection.setUseCaches (false);
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//
//            //Send request
//            DataOutputStream wr = new DataOutputStream (
//                    connection.getOutputStream ());
//            wr.writeChars(json);
//            wr.flush ();
//            wr.close ();
//
////            //Get Response
////            InputStream is = connection.getInputStream();
////            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
////            String line;
////            StringBuffer response = new StringBuffer();
////            while((line = rd.readLine()) != null) {
////                response.append(line);
////                response.append('\r');
////            }
////            rd.close();
////            return response.toString();
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            return;
//
//        } finally {
//
//            if(connection != null) {
//                connection.disconnect();
//            }
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
