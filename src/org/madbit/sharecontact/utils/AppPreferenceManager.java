package org.madbit.sharecontact.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class AppPreferenceManager {

	public static void putInt(String key, int value, Context context) {
		// change registration state value to PENDING
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putInt(key, value);
		editor.commit();
	}
}
