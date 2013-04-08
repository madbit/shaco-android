package org.madbit.sharecontact.activity;

import java.util.List;

import org.madbit.sharecontact.R;
import org.madbit.sharecontact.adapter.ContactsListAdapter;
import org.madbit.sharecontact.addressbook.AddressBookDAO;
import org.madbit.sharecontact.addressbook.domain.Contact;
import org.madbit.sharecontact.common.UserFactory;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends ListActivity {
	
	boolean emptyList = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		populateContactList();		
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateContactList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if(!emptyList) {
			Contact contact = (Contact) getListAdapter().getItem(position);		
			Intent shareContactActivity = new Intent(this, ShareContactActivity.class);
			shareContactActivity.putExtra("sharedContact", contact);			
			this.startActivity(shareContactActivity);
		}
	}

	private void populateContactList() {
		UserFactory.initialize(getApplicationContext());		

		// get contacts list from Address Book
		AddressBookDAO cm = AddressBookDAO.getInstance();
		List<Contact> contacts = cm.readContacts(getContentResolver());
		
		// populate list
		ContactsListAdapter contactsListAdapter = new ContactsListAdapter(MainActivity.this, R.layout.activity_main, contacts);
		setListAdapter(contactsListAdapter);
		contactsListAdapter.notifyDataSetChanged();
	}

}
