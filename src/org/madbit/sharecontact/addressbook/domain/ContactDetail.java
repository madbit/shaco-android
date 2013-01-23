package org.madbit.sharecontact.addressbook.domain;

import java.io.Serializable;

import org.madbit.sharecontact.addressbook.common.ContactDetailType;

public class ContactDetail implements Serializable {
	private static final long serialVersionUID = -1421272876398985028L;
	
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
