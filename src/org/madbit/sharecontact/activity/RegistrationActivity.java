package org.madbit.sharecontact.activity;

import java.util.concurrent.ExecutionException;

import org.madbit.sharecontact.R;
import org.madbit.sharecontact.asynctask.RegisterationTask;
import org.madbit.sharecontact.common.StaticValues;
import org.madbit.sharecontact.utils.AppPreferenceManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Activity {

	private static final String TAG = "org.madbit.sharecontact.activity.RegistrationActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);		
	}	

	public void confirm(View v) {

		EditText phoneNumber = (EditText) findViewById(R.id.wp_phone_number);
		EditText countryCode = (EditText) findViewById(R.id.wp_country_code);

		if(countryCode != null && phoneNumber != null) {			
			
			String msisdn = countryCode.getText().toString() + phoneNumber.getText().toString();
			
			RegisterationTask registerNewUserTask = new RegisterationTask();			
			registerNewUserTask.execute(msisdn);
			
			// wait for a response
			try {
				String result = registerNewUserTask.get();

				Log.d(TAG, "Response from server is " + result);
				
				if(result.equals("0")) {
					// change registration state value
					AppPreferenceManager.putInt(StaticValues.PREFS_REGISTRATION_STATE, StaticValues.REG_STATE_TOBECONFIRMED, this);
										
					// open confirm activity
					Intent confirmActivity = new Intent(this, RegistrationConfirmCodeActivity.class);
					// pass value to next activity
					confirmActivity.putExtra("MSISDN", msisdn);
					this.startActivity(confirmActivity);
				} else {
					Toast.makeText(getApplicationContext(), "An error occurs. Try again!", Toast.LENGTH_LONG).show();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
		}
	}

}
