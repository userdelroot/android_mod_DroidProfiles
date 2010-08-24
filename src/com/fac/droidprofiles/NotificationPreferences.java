package com.fac.droidprofiles;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class NotificationPreferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.notification_pref);
	}

}
