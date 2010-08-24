package com.fac.droidprofiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class ProfilesReceiver extends BroadcastReceiver {

	private final String TAG = "ProfilesReceiver ";
	private final String BOOT_STATE = "android.intent.action.BOOT_COMPLETED";
		
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals(BOOT_STATE)) {
			
			Intent mService = new Intent();
			mService.setAction("com.fac.droidprofiles.ProfileService");
			context.startService(mService);
			
			if (Log.LOGV)
				Log.i(TAG + "DroidProfiles Service Boot Completed");
			
		}
		
		
	}
}