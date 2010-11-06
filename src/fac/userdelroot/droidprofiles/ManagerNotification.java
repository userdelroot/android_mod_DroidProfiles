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


import android.content.Context;

/**
 * TODO: this class is broken / not used
 * Need to figure out how to get Context from here 
 * Helper class 
 * 
 */
public class ManagerNotification {

	ManagerNotification(Context c, ProfilesData profile) {
		
	    initPrepare();
		doNotification();
	}
	
	/**
	 * Prepare the notifications and set what flags we need
	 */
	final private void initPrepare() {
	
	/*	if (Log.LOGV)
			Log.v(TAG + "initPrepare()");
		
		if (mProfile.ringer || mProfile.ringer_active) {
			mAM = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
			mRM = new RingtoneManager(mContext);
			mOrigDefaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(mContext, RingtoneManager.TYPE_RINGTONE);
		}
		
		if (mProfile.vibrate || mProfile.vibrate_active) { 
			mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
			setVibratePattern();
		}*/
	}

	
	/**
	 * Set vibrate settings and pattern
	 */
	//private void setVibratePattern() {
	//	
	//}

	final private void doNotification() {
		// this is kind of a hack but it works ;)
	}
	
	final private void stop() {
	}
	
	final public void doStop() {
		 stop();
	}
}
