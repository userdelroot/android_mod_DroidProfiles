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

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.view.KeyEvent;

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

        mNotification = (CheckBoxPreference) this.findPreference("notification_bar");
        mNotifyIcon = (ListPreference) this.findPreference("notification_bar_icon");
        mLed = (CheckBoxPreference) this.findPreference("led_enabled");
        mLedPattern = (ListPreference) this.findPreference("led_pattern");
        mLedColor = (ListPreference) this.findPreference("led_color");
        mRinger = (CheckBoxPreference) this.findPreference("ringer_enabled");
        mRingtone = (RingtonePref) this.findPreference("ringtone");
        mRingVolume = (VolumePref) this.findPreference("ring_volume");
        mVibrate = (CheckBoxPreference) this.findPreference("vibrate_enabled");
        mVibratePattern = (ListPreference) this.findPreference("vibrate_pattern");

        if (mNotification == null || mLed == null || mRinger == null || mVibrate == null
                || mRingtone == null || mRingVolume == null || mNotifyIcon == null
                || mLedPattern == null || mLedColor == null || mVibratePattern == null) {
            if (Log.LOGV)
                Log.e(TAG
                        + "One of our preferences has a null pointer and none of  them should be null\n bailing...");
            finish();
            return;
        }

    }

    /**
     * Loads the default values if we have them if not then blank it all out
     */
    private void loadDefaultValues() {
        try {
            mNotification.setChecked(pNotify.notify_active);
            mLed.setChecked(pNotify.led_active);
            mRinger.setChecked(pNotify.ringer_active);
            mVibrate.setChecked(pNotify.vibrate_active);
            mRingtone.setRingtone(Uri.parse(pNotify.ringtone));
            mRingVolume.setVolume(pNotify.ringer_vol);

            if (mNotifyIcon.findIndexOfValue(pNotify.notify_icon) > -1)
                mNotifyIcon.setValueIndex(mNotifyIcon.findIndexOfValue(pNotify.notify_icon));

            if (mLedPattern.findIndexOfValue(pNotify.led_pat) > -1)
                mLedPattern.setValueIndex(mLedPattern.findIndexOfValue(pNotify.led_pat));

            if (mLedColor.findIndexOfValue(pNotify.led_color) > -1)
                mLedColor.setValueIndex(mLedColor.findIndexOfValue(pNotify.led_color));

            if (mVibratePattern.findIndexOfValue(pNotify.vibrate_pat) > -1)
                mVibratePattern.setValueIndex(mVibratePattern.findIndexOfValue(pNotify.vibrate_pat));

        } catch (NullPointerException e) {
            if (Log.LOGV)
                Log.e(TAG + "Null pointer exception " + e.getLocalizedMessage().toString());
        } catch (RuntimeException e) {

        }
    }

    
    @Override
    protected void onPause() {
        super.onPause();
        pNotify = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle b = getIntent().getExtras();
        int profileId = b.getInt("fac.userdelroot.droidprofiles.Notify", -1);
        
        pNotify = Profiles.getNotifyByProfileId(getContentResolver(), profileId, Notify.Columns.NOTIFY_TYPE_EMAIL);

        if (pNotify == null) 
            pNotify = new Notify();
        
        loadDefaultValues();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                setParcelData();
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setParcelData() {

        try {
            pNotify.notify_active = mNotification.isChecked();
            pNotify.notify_icon = mNotifyIcon.getValue();
            pNotify.led_active = mLed.isChecked();
            pNotify.led_color = mLedColor.getValue();
            pNotify.led_pat = mLedPattern.getValue();
            pNotify.ringer_active = mRinger.isChecked();
            pNotify.ringtone = mRingtone.getRingtone();
            pNotify.ringer_vol = mRingVolume.getVolume();
            pNotify.vibrate_active = mVibrate.isChecked();
            pNotify.vibrate_pat = mVibratePattern.getValue();

            Intent retIntent = new Intent();
            retIntent.putExtra("fac.userdelroot.droidprofiles.Notify", pNotify);
            setResult(RESULT_OK, retIntent);

        } catch (NullPointerException e) {
            if (Log.LOGV)
                Log.e(TAG + "setParcelData null pointer " + e.getLocalizedMessage().toString());
        }
    }
}
