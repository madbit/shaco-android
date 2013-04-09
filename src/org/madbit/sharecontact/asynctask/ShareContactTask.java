package org.madbit.sharecontact.asynctask;

import org.madbit.sharecontact.rest.ServiceClient;
import org.madbit.sharecontact.rest.ServiceClientFactory;

import android.os.AsyncTask;

public class ShareContactTask extends AsyncTask<String, Integer, String>{
	
	@Override
	protected String doInBackground(String... params) {
		
		String sender = params[0];
		String receiver = params[1];
		String contactToShare = params[2];
		
		ServiceClient serviceClient = ServiceClientFactory.createServiceClient();
				
		return serviceClient.shareContact(sender, receiver, contactToShare);
	}

	@Override
	// called when POST has been executed
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
}
