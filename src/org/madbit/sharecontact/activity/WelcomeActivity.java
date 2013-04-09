package org.madbit.sharecontact.activity;

import org.madbit.sharecontact.R;
import org.madbit.sharecontact.asynctask.RegisterNewUserTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class WelcomeActivity extends Activity {

	private static final String TAG = "org.madbit.sharecontact.activity.WelcomeActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);		
	}	

	public void confirm(View v) {

		EditText phoneNumber = (EditText) findViewById(R.id.wp_phone_number);
		EditText countryCode = (EditText) findViewById(R.id.wp_country_code);

		if(countryCode != null && phoneNumber != null) {
			Log.d(TAG, "Post MSISDN " + countryCode.getText().toString() + phoneNumber.getText().toString());

			RegisterNewUserTask registerNewUserTask = new RegisterNewUserTask();
			registerNewUserTask.execute(countryCode.getText().toString() + phoneNumber.getText().toString());
		}
	}

}
