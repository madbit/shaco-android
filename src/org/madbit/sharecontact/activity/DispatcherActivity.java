package org.madbit.sharecontact.activity;

import org.madbit.sharecontact.R;
import org.madbit.sharecontact.common.StaticValues;

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
		// 0 is the default value
		int registrationState = prefs.getInt(StaticValues.PREFS_REGISTRATION_STATE, StaticValues.REG_STATE_REGISTER);

		switch (registrationState) {
		// registration
		case StaticValues.REG_STATE_REGISTER:
			Intent registrationActivity = new Intent(this, RegistrationActivity.class);			
			this.startActivity(registrationActivity);
			break;
		// registration code confirmation
		case StaticValues.REG_STATE_TOBECONFIRMED:
			Intent confirmActivity = new Intent(this, RegistrationConfirmCodeActivity.class);			
			this.startActivity(confirmActivity);
			break;
		// main activity
		case StaticValues.REG_STATE_CONFIRMED:
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
