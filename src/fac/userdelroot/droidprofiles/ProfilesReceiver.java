/**
 * 
 * Copyright (C) 2010  userdelroot r00t316@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */


package fac.userdelroot.droidprofiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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