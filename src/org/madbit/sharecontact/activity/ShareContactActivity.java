package org.madbit.sharecontact.activity;

import java.util.List;

import org.madbit.sharecontact.R;
import org.madbit.sharecontact.adapter.ContactsListAdapter;
import org.madbit.sharecontact.addressbook.AddressBookDAO;
import org.madbit.sharecontact.addressbook.domain.Contact;
import org.madbit.sharecontact.addressbook.vcard.VCardFactory;
import org.madbit.sharecontact.exception.ServiceException;
import org.madbit.sharecontact.handler.ShareContactHandler;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class ShareContactActivity extends ListActivity {
	
	private static final String TAG = "org.madbit.sharecontact.activity.ShareContactActivity";
	
	private Contact sharedContact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get contact to share from previous Activity
		sharedContact = (Contact) this.getIntent().getSerializableExtra("sharedContact");

		// get contacts list from Address Book
		AddressBookDAO cm = AddressBookDAO.getInstance();
		List<Contact> contacts = cm.readContacts(getContentResolver(), sharedContact.getContactId());

		// populate list
		ContactsListAdapter contactsListAdapter = new ContactsListAdapter(ShareContactActivity.this, R.layout.activity_share_contact, contacts);
		setListAdapter(contactsListAdapter);
		contactsListAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Contact contactTo = (Contact) getListAdapter().getItem(position);
		
		String sharedVCard = VCardFactory.makeVCardString(getApplicationContext(), sharedContact.getDisplayName(), sharedContact.getContactId());
		
		Log.d(TAG, sharedVCard);
		
		ShareContactHandler handler = new ShareContactHandler();
		try {
			boolean isSuccess = handler.shareContact(contactTo, sharedVCard);
			
			if(isSuccess) {
				Log.d(TAG, "Share success");
				showAlertDialog(getString(R.string.dialog_share_success));
			} else {
				Log.d(TAG, "Share failed");
				showAlertDialog(getString(R.string.dialog_share_failed));
			}
		} catch (ServiceException e) {
			Log.e(TAG, "ServiceException");
			showAlertDialog(getString(R.string.dialog_share_failed));
		}
	}

	private void showAlertDialog(String message) {
		@SuppressWarnings("unused")
		AlertDialog alertDialog = new AlertDialog.Builder(this)
		.setMessage(message)
		.setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				showHome();
			}
		})
		.setNegativeButton(getString(R.string.button_close), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		} )
		.show();
	}
	
	private void showHome() {
		Intent mainActivity = new Intent(this, MainActivity.class);
		this.startActivity(mainActivity);
	}	
	
}
