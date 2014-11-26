package aljudy.cs4720_android;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;

public class TwitterActivity extends Activity {
	
	SharedPreferences pref;
	   
    private static String CONSUMER_KEY = "MHiRNFPYCYlO6ZpT6CodCCgJH";
    private static String CONSUMER_SECRET = "OKtwqNbpeUy0WKyD8v8I0kvw8x0AEyzrDzlpltyqYiHdW1xhmA";
  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter);
        pref = getPreferences(0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("CONSUMER_KEY", CONSUMER_KEY);
        edit.putString("CONSUMER_SECRET", CONSUMER_SECRET);
        edit.commit();  

		Fragment login = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();	              
        ft.replace(R.id.content_frame, login);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
	}


}
