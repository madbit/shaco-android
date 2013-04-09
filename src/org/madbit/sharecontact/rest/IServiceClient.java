package org.madbit.sharecontact.rest;

import org.madbit.sharecontact.exception.ServiceException;

public interface IServiceClient {

	public String shareContact(String sender, String receiver, String contactToShare) throws ServiceException;	
	public String registerNewUser(String msisdn) throws ServiceException;
}
