package org.madbit.sharecontact.handler;

import org.madbit.sharecontact.addressbook.domain.Contact;
import org.madbit.sharecontact.addressbook.domain.IUser;
import org.madbit.sharecontact.asynctask.ShareContactTask;
import org.madbit.sharecontact.common.UserFactory;
import org.madbit.sharecontact.exception.ServiceException;

public class ShareContactHandler {
	
	private static final String TAG = "org.madbit.sharecontact.handler.ShareContactHandler";
	
	public boolean shareContact(Contact receiver, String sharedVCard) throws ServiceException {
		
		IUser localUser = UserFactory.getUser();
		
		ShareContactTask httpClient = new ShareContactTask();
		httpClient.execute(localUser.getMsisdn(), receiver.getDisplayName(), sharedVCard);
		
		return true;
	}

}
