package org.madbit.sharecontact.activity;

import java.util.List;

import org.madbit.sharecontact.R;
import org.madbit.sharecontact.adapter.ContactsListAdapter;
import org.madbit.sharecontact.addressbook.AddressBookDAO;
import org.madbit.sharecontact.addressbook.domain.Contact;
import org.madbit.sharecontact.handler.ShareContactHandler;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ShareContactActivity extends ListActivity {
	
	private String sharedCid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sharedCid = (String) this.getIntent().getSerializableExtra("sharedCid");

		AddressBookDAO cm = AddressBookDAO.getInstance();
		List<Contact> contacts = cm.readContacts(getContentResolver(), sharedCid);

		ContactsListAdapter contactsListAdapter = new ContactsListAdapter(ShareContactActivity.this, R.layout.activity_share_contact, contacts);
		setListAdapter(contactsListAdapter);
		contactsListAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Contact contact = (Contact) getListAdapter().getItem(position);
		
		ShareContactHandler handler = new ShareContactHandler();
		handler.shareContact(contact.getContactId(), sharedCid);
	}
}
