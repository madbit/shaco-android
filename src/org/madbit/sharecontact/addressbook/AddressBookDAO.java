package org.madbit.sharecontact.addressbook;

import java.util.ArrayList;
import java.util.List;

import org.madbit.sharecontact.addressbook.common.ContactDetailType;
import org.madbit.sharecontact.addressbook.domain.Contact;
import org.madbit.sharecontact.addressbook.domain.ContactDetail;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;

public class AddressBookDAO {

	private static AddressBookDAO instance;

	private AddressBookDAO() {

	}

	public static AddressBookDAO getInstance() {
		if(instance == null)
			instance = new AddressBookDAO();
		return instance;
	}

	public List<Contact> readContacts(ContentResolver contentResolver) {
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		
		Cursor cursorContact = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		// get all the contacts
		while(cursorContact.moveToNext()) {					
			String contactId = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts._ID));
			String displayName = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			
			Contact contact = new Contact();
			contact.setContactId(contactId);
			contact.setDisplayName(displayName);
			
			// get all phone numbers for contactId
			Cursor cursorPhones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + contactId, null, null);
			List<ContactDetail> phoneNumbers = new ArrayList<ContactDetail>();
			while (cursorPhones.moveToNext()) {
				String number = cursorPhones.getString(cursorPhones.getColumnIndex(Phone.NUMBER));
				int type = cursorPhones.getInt(cursorPhones.getColumnIndex(Phone.TYPE));
				
				ContactDetail contactDetail = new ContactDetail();
				contactDetail.setValue(number);				
				
				switch (type) {
					case Phone.TYPE_HOME:
						contactDetail.setContactDetailType(ContactDetailType.HOME);
						break;
					case Phone.TYPE_MOBILE:
						contactDetail.setContactDetailType(ContactDetailType.MOBILE);
						break;
					case Phone.TYPE_WORK:
						contactDetail.setContactDetailType(ContactDetailType.WORK);
						break;
					default:
						contactDetail.setContactDetailType(ContactDetailType.HOME);
						break;
				}
				
				phoneNumbers.add(contactDetail);
								
				System.out.println(contactId + " " + displayName + " " + number + " " + type);			
			}
			contact.setPhoneNumbers(phoneNumbers);
			contacts.add(contact);
			
			cursorPhones.close();			
		}
		cursorContact.close();

		return contacts;
	}
	
	public List<Contact> readContacts(ContentResolver contentResolver, String cidToExclude) {
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		
		Cursor cursorContact = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		// get all the contacts
		while(cursorContact.moveToNext()) {					
			String contactId = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts._ID));
			if(contactId.equals(cidToExclude))
				continue;
			
			String displayName = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			
			Contact contact = new Contact();
			contact.setContactId(contactId);
			contact.setDisplayName(displayName);
			
			// get all phone numbers for contactId
			Cursor cursorPhones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + contactId, null, null);
			List<ContactDetail> phoneNumbers = new ArrayList<ContactDetail>();
			while (cursorPhones.moveToNext()) {
				String number = cursorPhones.getString(cursorPhones.getColumnIndex(Phone.NUMBER));
				int type = cursorPhones.getInt(cursorPhones.getColumnIndex(Phone.TYPE));
				
				ContactDetail contactDetail = new ContactDetail();
				contactDetail.setValue(number);				
				
				switch (type) {
					case Phone.TYPE_HOME:
						contactDetail.setContactDetailType(ContactDetailType.HOME);
						break;
					case Phone.TYPE_MOBILE:
						contactDetail.setContactDetailType(ContactDetailType.MOBILE);
						break;
					case Phone.TYPE_WORK:
						contactDetail.setContactDetailType(ContactDetailType.WORK);
						break;
					default:
						contactDetail.setContactDetailType(ContactDetailType.HOME);
						break;
				}
				
				phoneNumbers.add(contactDetail);
								
				System.out.println(contactId + " " + displayName + " " + number + " " + type);			
			}
			contact.setPhoneNumbers(phoneNumbers);
			contacts.add(contact);
			
			cursorPhones.close();			
		}
		cursorContact.close();

		return contacts;
	}

	public void readContactById(ContentResolver cr, String contactId) {
		
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, "CONTACT_ID = '" + contactId + "'", null, null);
		if (cursor.moveToFirst()) {
			//
			//  Get all phone numbers.
			//
			Cursor phones = cr.query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + contactId, null, null);
			while (phones.moveToNext()) {
				String number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
				int type = phones.getInt(phones.getColumnIndex(Phone.TYPE));
				
				switch (type) {
				case Phone.TYPE_HOME:
					// do something with the Home number here...
					break;
				case Phone.TYPE_MOBILE:
					// do something with the Mobile number here...
					break;
				case Phone.TYPE_WORK:
					// do something with the Work number here...
					break;
				}
			}
			phones.close();
		}
		cursor.close();
	}

//	private void readContactsOld(ContentResolver contentResolver) {
//		String[] projection = new String[] {
//				Data.MIMETYPE,
//				ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
//				ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME
//		};
//
//		String selection =  Data.MIMETYPE+" = '"+ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE+"'";
//		Cursor cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, projection, selection, null, null);
//
//		ArrayList<Contact> contacts = new ArrayList<Contact>();
//		while (cursor.moveToNext()) {
//			Contact c = new Contact();
//			String familyName = cursor.getString(1) != null ? cursor.getString(1) : "";
//			String givenName = cursor.getString(2) != null ? cursor.getString(2) : "";
////			c.setFamilyName(familyName);
////			c.setGivenName(givenName);
//			contacts.add(c);			
//		} 
//		cursor.close();
//	}

}
