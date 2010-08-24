/**
 * Ringtone Preference Class
 */

package com.fac.droidprofiles.pref;

import android.content.Context;
import android.net.Uri;
import android.preference.RingtonePreference;
import android.util.AttributeSet;

public class RingtonePref extends RingtonePreference {
	
	private Uri mRingtone;
	

	public RingtonePref(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	@Override
	protected Uri onRestoreRingtone() {
		return mRingtone;
	}

	@Override
	protected void onSaveRingtone(Uri ringtoneUri) {
		setRingtone(ringtoneUri);
	}
	
	public void setRingtone(Uri ringtoneUri) {
		mRingtone = ringtoneUri;
	}
	
	public String getRingtone() {
		if (mRingtone != null) {
			return mRingtone.toString();
		}
		return null;
		
	}
}