package org.madbit.sharecontact.activity;

import java.util.concurrent.ExecutionException;

import org.madbit.sharecontact.R;
import org.madbit.sharecontact.asynctask.RegistrationConfirmCodeTask;
import org.madbit.sharecontact.common.StaticValues;
import org.madbit.sharecontact.utils.AppPreferenceManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationConfirmCodeActivity extends Activity {

	private final String TAG = this.getClass().getPackage() + "." + this.getClass().getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_reg_code);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration_confirm_code, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_reset:
			// reset registration state value 
			AppPreferenceManager.putInt(StaticValues.PREFS_REGISTRATION_STATE, StaticValues.REG_STATE_REGISTER, this);
			Toast.makeText(getApplicationContext(), "Reset", Toast.LENGTH_SHORT).show();

			// return to registration
			Intent registrationActivity = new Intent(this, RegistrationActivity.class);			
			this.startActivity(registrationActivity);

			return true;	        
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void confirm(View v) {

		EditText regCode = (EditText) findViewById(R.id.reg_code_text);

		if(regCode != null) {			

			// retrieve msisdn value from previous activity
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				String msisdn = extras.getString("MSISDN");
				RegistrationConfirmCodeTask task = new RegistrationConfirmCodeTask();
				task.execute(regCode.getText().toString(), msisdn);

				// wait for a response
				try {
					String result = task.get();

					Log.d(TAG, "Response from server is " + result);

					if(result.equals("0")) {
						// change registration state value to PENDING
						AppPreferenceManager.putInt(StaticValues.PREFS_REGISTRATION_STATE, StaticValues.REG_STATE_CONFIRMED, this);

						// open main activity
						Intent mainActivity = new Intent(this, MainActivity.class);			
						this.startActivity(mainActivity);
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
			} else {
				Log.d(TAG, "Extras is null");
			}
		}
	}

}
