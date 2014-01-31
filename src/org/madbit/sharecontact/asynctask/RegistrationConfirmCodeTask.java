package org.madbit.sharecontact.asynctask;

import org.madbit.sharecontact.rest.ServiceClient;
import org.madbit.sharecontact.rest.ServiceClientFactory;

import android.os.AsyncTask;
import android.util.Log;

public class RegistrationConfirmCodeTask extends AsyncTask<String, Void, String>{

	private static final String TAG = "org.madbit.sharecontact.asynctask.RegisterNewUserTask";
	
	@Override
	protected String doInBackground(String... params) {	

		String regCode = params[0];
		String msisdn = params[1];
		
		Log.d(TAG, "Posting regCode " + regCode + " for MSISDN " + msisdn);
		
		ServiceClient serviceClient = ServiceClientFactory.createServiceClient();
		return serviceClient.confirmRegistrationCode(regCode, msisdn);
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
}
