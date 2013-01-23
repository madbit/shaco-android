package org.madbit.sharecontact.addressbook.domain;

import java.io.Serializable;
import java.util.List;

public class Contact implements Serializable {
	private static final long serialVersionUID = 2413165158733644155L;
	
	private String contactId;
	private String displayName;
	private List<ContactDetail> phoneNumbers;
//	private String givenName;
//	private String familyName;
	
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public List<ContactDetail> getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(List<ContactDetail> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	
	
//	public String getGivenName() {
//		return givenName;
//	}
//	public void setGivenName(String givenName) {
//		this.givenName = givenName;
//	}
//	
//	public String getFamilyName() {
//		return familyName;
//	}
//	public void setFamilyName(String familyName) {
//		this.familyName = familyName;
//	}
}
