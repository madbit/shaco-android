package org.madbit.sharecontact.asynctask;

import org.madbit.sharecontact.rest.ServiceClient;
import org.madbit.sharecontact.rest.ServiceClientFactory;

import android.os.AsyncTask;
import android.util.Log;

public class RegisterationTask extends AsyncTask<String, Void, String>{

	private static final String TAG = "org.madbit.sharecontact.asynctask.RegisterNewUserTask";
	
	@Override
	protected String doInBackground(String... params) {		
		String msisdn = params[0];
		Log.d(TAG, "Posting MSISDN " + msisdn);
		
		ServiceClient serviceClient = ServiceClientFactory.createServiceClient();
		return serviceClient.registerNewUser(msisdn);
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
}
