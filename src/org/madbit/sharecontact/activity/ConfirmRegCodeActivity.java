package org.madbit.sharecontact.activity;

import org.madbit.sharecontact.R;
import org.madbit.sharecontact.R.layout;
import org.madbit.sharecontact.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ConfirmRegCodeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_reg_code);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.confirm_reg_code, menu);
		return true;
	}

}
