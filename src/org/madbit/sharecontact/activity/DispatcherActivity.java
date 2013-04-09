package org.madbit.sharecontact.activity;

import org.madbit.sharecontact.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class DispatcherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dispatcher);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		int registrationState = prefs.getInt("registrationState", 0);

		switch (registrationState) {
		// registration
		case 0:
			Intent welcomeActivity = new Intent(this, WelcomeActivity.class);			
			this.startActivity(welcomeActivity);
			break;
		// registration code confirmation
		case 1:
			Intent confirmActivity = new Intent(this, ConfirmRegCodeActivity.class);			
			this.startActivity(confirmActivity);
			break;
		// main activity
		case 2:
			Intent mainActivity = new Intent(this, MainActivity.class);			
			this.startActivity(mainActivity);
			break;
		default:
			break;
		}
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.dispatcher, menu);
//		return true;
//	}

}
