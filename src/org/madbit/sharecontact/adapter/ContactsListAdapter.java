package org.madbit.sharecontact.adapter;


import java.util.List;

import org.madbit.sharecontact.R;
import org.madbit.sharecontact.addressbook.domain.Contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactsListAdapter extends ArrayAdapter<Contact> {

	private final Context context;
	private final List<Contact> contacts;
	private int resource;
		
	public ContactsListAdapter(Context context, int resource, List<Contact> contacts) {
		super(context, resource, resource, contacts);
		this.context = context;
		this.resource = resource;
		this.contacts = contacts;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
		LinearLayout contactsView;
		Contact contact = getItem(position);
		
		if(convertView==null)
        {
            contactsView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, contactsView, true);
        } else
        	contactsView = (LinearLayout) convertView;      
		
		TextView contactName =(TextView)contactsView.findViewById(R.id.contactName);
		contactName.setText(contact.getDisplayName());
        
        
        return contactsView;
    }
}
