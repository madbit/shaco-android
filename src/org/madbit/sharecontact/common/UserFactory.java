package org.madbit.sharecontact.common;

import org.madbit.sharecontact.addressbook.domain.IUser;
import org.madbit.sharecontact.addressbook.domain.User;

import android.content.Context;
import android.telephony.TelephonyManager;

public class UserFactory {
	
	private static IUser user;
	private static Context appContext;
	
	public static IUser getUser(Context context) {
		if(appContext == null)
			appContext = context;
		
		if(user == null)			
			user = initializeUser();
		
		return user;
	}
	
	private UserFactory(Context context) { }
	
	public static IUser initializeUser() {
		IUser user = new User();
		
		// get MSISDN
		TelephonyManager tMgr =(TelephonyManager)appContext.getSystemService(Context.TELEPHONY_SERVICE);
		String msisdn = tMgr.getLine1Number();
		user.setMsisdn(msisdn);
		
		return user;
	}
}
