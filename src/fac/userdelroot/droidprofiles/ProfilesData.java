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
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

/**
 * 
 *  
 * Parcelable data 
 * Loads the profile and assocaited notifications with this specific profile
 *
 */
public final class ProfilesData implements Parcelable {

	public static final Parcelable.Creator<ProfilesData> CREATOR = new Parcelable.Creator<ProfilesData>() {
		public ProfilesData createFromParcel(Parcel p) {
			return new ProfilesData(p);
		}

		public ProfilesData[] newArray(int size) {
			return new ProfilesData[size];
		}
	};
	
	// public vars
	public int profile_id;
	public String name;
	public boolean active;
	public boolean silent;
	public boolean vibrate;
	public boolean ringer;
	public String ringtone;
	public float ringvolume;
	public boolean override;
	public boolean custom_notify; // if true we only use the notifications

	public boolean led_active;
	public int led_color;
	public int led_pat;
	public boolean notify_active;
	public String notify_icon;
	public boolean ringer_active;
	public float ringer_vol;
	public String notify_ringtone;
	public boolean vibrate_active;
	public int vibrate_pat;
	public boolean tb_active;
	public int tb_color;
	public int tb_pat;
	public int notify_type;


	public ProfilesData(Cursor c) {
		
		profile_id = c.getInt(Columns.PROFILE_ID_INDEX);
 		name = c.getString(Columns.NAME_INDEX);
		active = c.getInt(Columns.ACTIVE_INDEX) == 1;
		silent = c.getInt(Columns.SILENT_INDEX) == 1;
		vibrate = c.getInt(Columns.VIBRATE_INDEX) == 1;
		ringer = c.getInt(Columns.RINGER_INDEX) == 1;
		ringtone = c.getString(Columns.RINGTONE_INDEX);
		ringvolume = c.getFloat(Columns.RINGVOL_INDEX);
		override = c.getInt(Columns.OVERRIDE_INDEX) == 1;
		custom_notify = c.getInt(Columns.CUSTOM_NOTIFY_INDEX) == 1;
		led_active = c.getInt(Columns.LED_ACTIVE_INDEX) == 1;
		led_color = c.getInt(Columns.LED_COLOR_INDEX);
		led_pat = c.getInt(Columns.LED_PAT_INDEX);
		notify_active = c.getInt(Columns.NOTIFY_ACTIVE_INDEX) == 1;
		notify_icon = c.getString(Columns.NOTIFY_ICON_INDEX);
		ringer_active = c.getInt(Columns.RINGER_ACTIVE_INDEX) == 1;
		ringer_vol = c.getFloat(Columns.RINGER_VOL_INDEX);
		notify_ringtone = c.getString(Columns.RINGTONE_INDEX);
		vibrate_active = c.getInt(Columns.VIBRATE_ACTIVE_INDEX) == 1;
		vibrate_pat = c.getInt(Columns.VIBRATE_PAT_INDEX);
		tb_active = c.getInt(Columns.TB_ACTIVE_INDEX) == 1;
		tb_color = c.getInt(Columns.TB_COLOR_INDEX);
		tb_pat = c.getInt(Columns.TB_PAT_INDEX);	
		notify_type = c.getInt(Columns.NOTIFY_TYPE_INDEX);

	}
	
	public ProfilesData(Parcel p) {
	
	
		profile_id = p.readInt();
		name = p.readString();
		active = p.readInt() == 1;
		silent = p.readInt() == 1;
		vibrate = p.readInt() == 1;
		ringer = p.readInt() == 1;
		ringtone = p.readString();
		ringvolume = p.readFloat();
		override = p.readInt() == 1;
		custom_notify = p.readInt() == 1;
		//uriRingTone = (Uri) p.readParcelable(null);
		led_active = p.readInt() == 1;
		led_color = p.readInt();
		led_pat = p.readInt();
		notify_active = p.readInt() == 1;
		notify_icon = p.readString();
		ringer_active = p.readInt() == 1;
		ringer_vol = p.readFloat();
		notify_ringtone = p.readString();
		vibrate_active = p.readInt() == 1;
		vibrate_pat = p.readInt();
		tb_active = p.readInt() == 1;
		tb_color = p.readInt();
		tb_pat = p.readInt();
		notify_type = p.readInt();
	
	}

	@Override
	public void writeToParcel(Parcel p, int flags) {
		p.writeInt(profile_id);
		p.writeString(name);
		p.writeInt(active ? 1 : 0);
		p.writeInt(silent ? 1 : 0);
		p.writeInt(vibrate ? 1 : 0);
		p.writeInt(ringer ? 1 : 0);
		p.writeString(ringtone);
		p.writeFloat(ringvolume);
		p.writeInt(override ? 1 : 0);
		p.writeInt(custom_notify ? 1 : 0);
	//	p.writeParcelable(uriRingTone, flags);
		p.writeInt(led_active ? 1 : 0);
		p.writeInt(led_color);
		p.writeInt(led_pat);
		p.writeInt(notify_active ? 1 : 0);
		p.writeString(notify_icon);
		p.writeInt(ringer_active ? 1 : 0);
		p.writeFloat(ringer_vol);
		p.writeString(notify_ringtone);
		p.writeInt(vibrate_active ? 1 : 0);
		p.writeInt(vibrate_pat);
		p.writeInt(tb_active ? 1 : 0);
		p.writeInt(tb_color);
		p.writeInt(tb_pat);
		p.writeInt(notify_type);
	}

	@Override
	public int describeContents() {
		return 0;
	}


	// Define the columns for the profiles table
	public static class Columns implements BaseColumns {

		// Uri for this this table
		public static final Uri CONTENT_URI_PHONE = Uri
				.parse("content://fac.userdelroot.droidprofiles/profiles/phone");

		public static final Uri CONTENT_URI_SMS = Uri.parse("content://com.fac.droidprofiles/profiles/sms");
		
		// Uri for this this table
		public static final Uri CONTENT_URI_MMS = Uri
				.parse("content://fac.userdelroot.droidprofiles/profiles/mms");

		public static final Uri CONTENT_URI_EMAIL = Uri.parse("content://fac.userdelroot.droidprofiles/profiles/email");	
		
		
		public static final int TYPE_PHONE = 1;
		public static final int TYPE_SMS = 2;
		public static final int TYPE_MMS = 3;
		public static final int TYPE_EMAIL = 4;
		
		public static final int PROFILE_ID_INDEX = 0;
		public static final int NAME_INDEX = 1;
		public static final int ACTIVE_INDEX = 2;
		public static final int SILENT_INDEX = 3;
		public static final int VIBRATE_INDEX = 4;
		public static final int RINGER_INDEX = 5;
		public static final int RINGTONE_INDEX = 6;
		public static final int RINGVOL_INDEX = 7;
		public static final int OVERRIDE_INDEX = 8;
		public static final int CUSTOM_NOTIFY_INDEX = 9;
		public static final int LED_ACTIVE_INDEX = 10;
		public static final int LED_COLOR_INDEX = 11;
		public static final int LED_PAT_INDEX = 12;
		public static final int NOTIFY_ACTIVE_INDEX = 13;
		public static final int NOTIFY_ICON_INDEX = 14;
		public static final int RINGER_ACTIVE_INDEX = 15;
		public static final int RINGER_VOL_INDEX = 16;
		public static final int NOTIFY_RINGTONE_INDEX = 17;
		public static final int VIBRATE_ACTIVE_INDEX = 18;
		public static final int VIBRATE_PAT_INDEX = 19;
		public static final int TB_ACTIVE_INDEX = 20;
		public static final int TB_COLOR_INDEX = 21;
		public static final int TB_PAT_INDEX = 22;
		public static final int NOTIFY_TYPE_INDEX = 23;
		
		public static final String PROFILE_ID = "profile_id"; // index 0
		public static final String REAL_ID = "real_id"; // index 1
		public static final String NAME = "name"; // index 2
		public static final String ACTIVE = "active"; // index 3
		public static final String SILENT = "silent"; // index 4
		public static final String VIBRATE = "vibrate"; // index 5
		public static final String RINGER = "ringer"; // index 6
		public static final String RINGTONE = "ringtone"; // index 7
		public static final String RING_VOL = "ringvol"; // index 8
		public static final String OVERRIDE = "overrides"; // index 9
		public static final String CUSTOM_NOTIFY = "custom_notify"; // index 10
		public static final String LED_ACTIVE = "led_active"; // index 11
		public static final String LED_COLOR = "led_color"; // index 12
		public static final String LED_PAT = "led_pattern"; // index 13
		public static final String NOTIFY_ACTIVE = "notifybar_active"; // index 14
		public static final String NOTIFY_ICON = "notifybar_icon"; // index 15
		public static final String RINGER_ACTIVE = "ringer_active"; // index 16
		public static final String RINGER_VOL = "ringer_volume"; // index 17
		public static final String NOTIFY_RINGTONE = "ringtone"; // index 18
		public static final String VIBRATE_ACTIVE = "vibrate_active"; // index 19
		public static final String VIBRATE_PAT = "vibrate_pattern"; // index 20
		public static final String TB_ACTIVE = "trackball_active"; // index 21
		public static final String TB_COLOR = "trackball_color"; // index 22
		public static final String TB_PAT = "trackball_pattern"; // index 23
		public static final String NOTIFY_TYPE = "notify_type"; // index 24
		
		
		
		public static final String[] PROJECTION = {
			 "contacts.profile_id", "profiles.name", "profiles.active",
			 "profiles.silent", "profiles.vibrate", "profiles.ringer", "profiles.ringtone", "profiles.ringvol", 
			 "profiles.overrides", "profiles.custom_notify", "notifications.led_active", "notifications.led_color", 
			 "notifications.led_pattern", "notifications.notifybar_active", 
			 "notifications.notifybar_icon", "notifications.ringer_active", 
			 "notifications.ringer_volume", "notifications.ringtone", "notifications.vibrate_active", 
			 "notifications.vibrate_pattern", "notifications.trackball_active", "notifications.trackball_color", 
			 "notifications.trackball_pattern", "notifications.notify_type" };
		
	}

}
