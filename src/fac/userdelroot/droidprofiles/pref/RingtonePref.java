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


package fac.userdelroot.droidprofiles.pref;

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