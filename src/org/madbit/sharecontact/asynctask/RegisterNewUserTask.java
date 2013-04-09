package org.madbit.sharecontact.asynctask;

import org.madbit.sharecontact.rest.ServiceClient;
import org.madbit.sharecontact.rest.ServiceClientFactory;

import android.os.AsyncTask;

public class RegisterNewUserTask extends AsyncTask<String, Void, String>{

	@Override
	protected String doInBackground(String... params) {		
		String msisdn = params[0];
		
		ServiceClient serviceClient = ServiceClientFactory.createServiceClient();
		return serviceClient.registerNewUser(msisdn);
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
}
