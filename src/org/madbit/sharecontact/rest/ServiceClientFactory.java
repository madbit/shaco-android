package org.madbit.sharecontact.rest;

public class ServiceClientFactory {
		
	private ServiceClientFactory() { }

	public static ServiceClient createServiceClient() {		
		return new ServiceClient();
	}
}
