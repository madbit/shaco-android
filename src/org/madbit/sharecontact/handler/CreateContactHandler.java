package org.madbit.sharecontact.handler;

import java.io.IOException;
import java.util.ArrayList;

import org.madbit.sharecontact.addressbook.AddressBookDAO;

import net.sourceforge.cardme.engine.VCardEngine;
import net.sourceforge.cardme.vcard.VCard;
import net.sourceforge.cardme.vcard.exceptions.VCardParseException;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.provider.ContactsContract;
import android.util.Log;

public class CreateContactHandler {	

	private static final String TAG = "org.madbit.sharecontact.handler.CreateContactHandler";

	public void createContact(ContentResolver contentResolver, String vCardString) {

		VCardEngine vCardEngine = new VCardEngine();

		try {
			VCard vCard = vCardEngine.parse(vCardString);
			Log.d(TAG, vCard.getFN().getFormattedName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (VCardParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String DisplayName = "XYZ";
		String MobileNumber = "123456";
		String HomeNumber = "1111";
		String WorkNumber = "2222";
		String emailID = "email@nomail.com";
		String company = "bad";
		String jobTitle = "abcd";

		ArrayList <ContentProviderOperation> ops = new ArrayList <ContentProviderOperation> ();

		ops.add(ContentProviderOperation.newInsert(
				ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
				.build());

		//------------------------------------------------------ Names
		if (DisplayName != null) {
			ops.add(ContentProviderOperation.newInsert(
					ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
							.withValue(
									ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
									DisplayName).build());
		}

		//------------------------------------------------------ Mobile Number                     
		if (MobileNumber != null) {
			ops.add(ContentProviderOperation.
					newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
							.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
							.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
									ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
									.build());
		}

		//------------------------------------------------------ Home Numbers
		if (HomeNumber != null) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
							.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
							.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
									ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
									.build());
		}

		//------------------------------------------------------ Work Numbers
		if (WorkNumber != null) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
							.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
							.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
									ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
									.build());
		}

		//------------------------------------------------------ Email
		if (emailID != null) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
							.withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
							.withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
							.build());
		}

		//------------------------------------------------------ Organization
		if (!company.equals("") && !jobTitle.equals("")) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
							.withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
							.withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
							.withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
							.withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
							.build());
		}

		AddressBookDAO dao = AddressBookDAO.getInstance();
		dao.createContact(contentResolver, ops);
	}

}
