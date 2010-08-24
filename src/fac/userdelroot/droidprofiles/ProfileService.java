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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 *  
 * 
 */
public class ProfileService extends Service {

	private static final String TAG = "ProfileService ";
	
	private TelephonyManager mTelMngr;
	private Context mContext;
	
	
	private ProfilesData mProfile;
	private ManagerNotification mMN;
	private String mPhoneNumber;
	
	@Override
	public void onCreate() {
		if (Log.LOGV)
			Log.v(TAG + "onCreate() Service");

		mContext = getApplicationContext();
		mTelMngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		mTelMngr.listen(mPhoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	
	private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			mPhoneNumber = incomingNumber;
			
			switch(state) {
			case TelephonyManager.CALL_STATE_RINGING:
				Thread t = new Thread() {
					@Override 
					public void run() {
						ProfilesHelper.initProcessing(mContext, mPhoneNumber, Notify.Columns.NOTIFY_TYPE_PHONE);
						if (ProfilesHelper.getShouldNotify() == false) {
							if (Log.LOGV)
								Log.i(TAG + "---Unable to send Notifications---");
							return;
						}
						
						// we can notify get the profile parcelable data
						mProfile = ProfilesHelper.getProfileData();
						if (mProfile == null) { 
							if (Log.LOGV)
								Log.i(TAG + "ProfilesData contains no data");
							return;
						}
						
						mMN = new ManagerNotification(mContext, mProfile);	
					}
				};
				t.start();
				
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if (mMN != null)
					mMN.doStop();
				
				mMN = null;
				Log.v(TAG + "---Call idle---");
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				if (mMN != null)
					mMN.doStop();
				
				mMN = null;
				
				Log.v(TAG + "---Call off hook---");
				break;
			}
		}
	};

	@Override
	public void onDestroy() {
		
		if (Log.LOGV)
			Log.v(TAG + "onDestroy() -- Service");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (Log.LOGV)
			Log.v(TAG + " onStartCommand " + flags + " startId " + startId + " intent " + intent.toString());

		return START_STICKY;

	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

}
