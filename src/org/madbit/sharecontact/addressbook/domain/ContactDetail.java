package org.madbit.sharecontact.addressbook.domain;

import org.madbit.sharecontact.addressbook.common.ContactDetailType;

public class ContactDetail {
	private ContactDetailType contactDetailType;
	private String value;
	
	public ContactDetailType getContactDetailType() {
		return contactDetailType;
	}
	public void setContactDetailType(ContactDetailType contactDetailType) {
		this.contactDetailType = contactDetailType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
