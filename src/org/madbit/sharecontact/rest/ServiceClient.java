package org.madbit.sharecontact.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.madbit.sharecontact.exception.ServiceException;

public class ServiceClient implements IServiceClient {
	
	private static final String SERVICE_ENPOINT = "http://10.0.2.2:8080/sc/rest";
	private HttpClient httpClient;
	
	public ServiceClient() {
		this.httpClient = new DefaultHttpClient();
	}
	
	public String shareContact(String sender, String receiver, String contactToShare) {
		HttpPost post = new HttpPost(SERVICE_ENPOINT + "/shareContact");
		
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("sender", sender));
		    nameValuePairs.add(new BasicNameValuePair("receiver", receiver));
		    nameValuePairs.add(new BasicNameValuePair("contactToShare", contactToShare));
			
		    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
						
			HttpResponse response = httpClient.execute(post);

			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder builder = new StringBuilder();
			String aux = "";		
			while ((aux = br.readLine()) != null)
			    builder.append(aux);
			
			br.close();
			return builder.toString();
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException();
		} catch (ClientProtocolException e) {
			throw new ServiceException();
		} catch (IOException e) {
			throw new ServiceException();
		}
	}

	@Override
	public String registerNewUser(String msisdn) {
		HttpPost post = new HttpPost(SERVICE_ENPOINT + "/registerNewUser");
		
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("msisdn", msisdn));
			
		    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
						
			HttpResponse response = httpClient.execute(post);

			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder builder = new StringBuilder();
			String aux = "";		
			while ((aux = br.readLine()) != null)
			    builder.append(aux);
			
			br.close();
			return builder.toString();
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException();
		} catch (ClientProtocolException e) {
			throw new ServiceException();
		} catch (IOException e) {
			throw new ServiceException();
		}
	}

	@Override
	public String confirmRegistrationCode(String regCode, String msisdn) throws ServiceException {
		HttpPost post = new HttpPost(SERVICE_ENPOINT + "/confirmRegistrationCode");
		
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("regCode", regCode));
		    nameValuePairs.add(new BasicNameValuePair("msisdn", msisdn));
		    
		    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
						
			HttpResponse response = httpClient.execute(post);

			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder builder = new StringBuilder();
			String aux = "";		
			while ((aux = br.readLine()) != null)
			    builder.append(aux);
			
			br.close();
			return builder.toString();
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException();
		} catch (ClientProtocolException e) {
			throw new ServiceException();
		} catch (IOException e) {
			throw new ServiceException();
		}
	}

}
