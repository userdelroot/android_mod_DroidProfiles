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

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import fac.userdelroot.droidprofiles.pref.RingtonePref;
import fac.userdelroot.droidprofiles.pref.VolumePref;

public class EmailPreferences extends PreferenceActivity {

    
    private static final String TAG = "EmailPreferences ";
    private RingtonePref mRingtone;
    private VolumePref mRingVolume;

    CheckBoxPreference mNotification, mLed, mRinger, mVibrate;
    ListPreference mNotifyIcon, mLedPattern, mLedColor, mVibratePattern;
    Notify pNotify;
    
    
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.notification_pref);
		
		Bundle b = getIntent().getExtras();
		pNotify = b.getParcelable("fac.userdelroot.droidprofiles.Notify");

		
		mNotification = (CheckBoxPreference) this.findPreference("notification_bar");
		mLed = (CheckBoxPreference) this.findPreference("led_enabled");
		mRinger = (CheckBoxPreference) this.findPreference("ringer_enabled");
		mVibrate = (CheckBoxPreference) this.findPreference("vibrate_enabled");
		mRingtone = (RingtonePref) this.findPreference("ringtone");
		mRingVolume = (VolumePref) this.findPreference("ring_volume");
		mNotifyIcon = (ListPreference) this.findPreference("notification_bar_icon");
		mLedPattern = (ListPreference) this.findPreference("led_pattern");
		mLedColor = (ListPreference) this.findPreference("led_color");
		mVibratePattern = (ListPreference) this.findPreference("vibrate_pattern");
		
		if (mNotification == null || mLed == null || mRinger == null || mVibrate == null || mRingtone == null || mRingVolume == null 
		         || mNotifyIcon == null || mLedPattern == null || mLedColor == null || mVibratePattern == null) {
		    if (Log.LOGV) 
		        Log.e(TAG + "One of our preferences has a null pointer and none of  them should be null\n bailing...");
		    finish();
		}
		    
		
		loadDefaultValues();
		
		
	}



    /**
     * Loads the default values if we have them if not then blank it all out
     */
    private void loadDefaultValues() {
        try {
            mNotification.setChecked((pNotify.notify_active == true) ? true : false);
            mLed.setChecked((pNotify.led_active == true) ? true : false);
            mRinger.setChecked((pNotify.ringer_active == true) ? true : false);
            mVibrate.setChecked((pNotify.vibrate_active == true) ? true : false);
            mRingtone.setRingtone(Uri.parse(pNotify.ringtone));
            mRingVolume.setVolume(pNotify.ringer_vol);
            // XXX need to set this from the parcel but what should be saved to the parcel? a string or an index value? 
            mNotifyIcon.setValueIndex(1);
            mLedPattern.setValueIndex(3);
            mLedColor.setValueIndex(1);
            mVibratePattern.setValueIndex(3);
        }
        catch (NullPointerException e) {
            if (Log.LOGV)
                Log.e(TAG + "Null pointer exception" + e.getLocalizedMessage().toString());
        }
    }

}
