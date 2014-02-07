package org.madbit.sharecontact.activity;

import java.util.List;

import org.madbit.sharecontact.R;
import org.madbit.sharecontact.adapter.ContactsListAdapter;
import org.madbit.sharecontact.adapter.MsisdnListAdapter;
import org.madbit.sharecontact.addressbook.AddressBookDAO;
import org.madbit.sharecontact.addressbook.domain.Contact;
import org.madbit.sharecontact.addressbook.domain.SimpleContact;
import org.madbit.sharecontact.common.StaticValues;
import org.madbit.sharecontact.common.UserFactory;
import org.madbit.sharecontact.utils.AppPreferenceManager;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MsisdnActivity extends ListActivity {
	
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_reset:
	        	// reset registration state value 
				AppPreferenceManager.putInt(StaticValues.PREFS_REGISTRATION_STATE, StaticValues.REG_STATE_REGISTER, this);
				Toast.makeText(getApplicationContext(), "Reset", Toast.LENGTH_SHORT).show();
				
				// return to registration
				Intent registrationActivity = new Intent(this, RegistrationActivity.class);			
				this.startActivity(registrationActivity);
				
	            return true;	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
//		if(!emptyList) {
//			Contact contact = (Contact) getListAdapter().getItem(position);		
//			Intent shareContactActivity = new Intent(this, ShareContactActivity.class);
//			shareContactActivity.putExtra("sharedContact", contact);			
//			this.startActivity(shareContactActivity);
//		}
	}

	private void populateContactList() {
		UserFactory.initialize(getApplicationContext());		
		
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				// get contacts list from Address Book
				AddressBookDAO cm = AddressBookDAO.getInstance();
				List<SimpleContact> contacts = cm.readSimpleContacts(getContentResolver());
				// populate list
				MsisdnListAdapter msisdnListAdapter = new MsisdnListAdapter(MsisdnActivity.this, R.layout.activity_msisdn_list, contacts);
				setListAdapter(msisdnListAdapter);
				msisdnListAdapter.notifyDataSetChanged();
			}
		});
	}
}
