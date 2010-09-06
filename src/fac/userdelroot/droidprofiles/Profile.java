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
 */
public final class Profile implements Parcelable {

	public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
		public Profile createFromParcel(Parcel p) {
			return new Profile(p);
		}

		public Profile[] newArray(int size) {
			return new Profile[size];
		}
	};

	// Define the columns for the profiles table
	public static class Columns implements BaseColumns {

		// Uri for this this table
		public static final Uri CONTENT_URI = Uri
				.parse("content://fac.userdelroot.droidprofiles/profiles");

		// Colums _ID is index 0
		public static final String NAME = "name"; // index 1
		public static final String ACTIVE = "active"; // index 2
		public static final String SILENT = "silent"; // index 3
		public static final String VIBRATE = "vibrate"; // index 4
		public static final String RINGER = "ringer"; // index 5
		public static final String RINGTONE = "ringtone"; // index 6
		public static final String RING_VOL = "ringvol"; // index 7
		public static final String OVERRIDES = "overrides"; // index 8
		public static final String CUSTOM_NOTIFY = "custom_notify"; // index 9

		// default sort order
		public static final String SORT_ORDER = " ASC";

		// is profile active
		public static final String[] PROFILE_QUERY = { _ID, NAME, ACTIVE,
				SILENT, VIBRATE, RINGER, RINGTONE, RING_VOL, OVERRIDES, CUSTOM_NOTIFY };

		public static final int ID_INDEX = 0;
		public static final int NAME_INDEX = 1;
		public static final int ACTIVE_INDEX = 2;
		public static final int SILENT_INDEX = 3;
		public static final int VIBRATE_INDEX = 4;
		public static final int RINGER_INDEX = 5;
		public static final int RINGTONE_INDEX = 6;
		public static final int RINGVOL_INDEX = 7;
		public static final int OVERRIDE_INDEX = 8;
		public static final int CUSTOM_NOTIFY_INDEX = 9;

	}

	// enabled / disabled
	public static final boolean ENABLED = true;
	public static final boolean DISABLED = false;

	// public vars
	public int id;
	public String name;
	public boolean active;
	public boolean silent;
	public boolean vibrate;
	public boolean ringer;
	public String ringtone;
	public float ringvolume;
	public boolean override;
	public boolean custom_notify; // if this is true we check the sms, mms etc
	// tables
	public Uri uriRingTone;

	public Profile(Cursor c) {

		id = c.getInt(Columns.ID_INDEX);
		name = c.getString(Columns.NAME_INDEX);
		active = c.getInt(Columns.ACTIVE_INDEX) == 1;
		silent = c.getInt(Columns.SILENT_INDEX) == 1;
		vibrate = c.getInt(Columns.VIBRATE_INDEX) == 1;
		ringer = c.getInt(Columns.RINGER_INDEX) == 1;
		ringtone = c.getString(Columns.RINGTONE_INDEX);
		ringvolume = c.getFloat(Columns.RINGVOL_INDEX);
		override = c.getInt(Columns.OVERRIDE_INDEX) == 1;
		custom_notify = c.getInt(Columns.CUSTOM_NOTIFY_INDEX) == 1;

		if (!custom_notify && silent != DISABLED) {

			if (ringtone != null && ringtone.length() != 0) {
				uriRingTone = Uri.parse(ringtone);
			}
		}

		if (uriRingTone == null) {
			// TODO: if our ringtone is null default to default ringtone ?
		}
	}

	/*
	 * Constructor
	 * 
	 * @param Parcel
	 */
	public Profile(Parcel p) {
		id = p.readInt();
		name = p.readString();
		active = p.readInt() == 1;
		silent = p.readInt() == 1;
		vibrate = p.readInt() == 1;
		ringer = p.readInt() == 1;
		ringtone = p.readString();
		ringvolume = p.readFloat();
		override = p.readInt() == 1;
		custom_notify = p.readInt() == 1;
		uriRingTone = (Uri) p.readParcelable(null);

	}

	@Override
	public void writeToParcel(Parcel p, int flags) {

		p.writeInt(id);
		p.writeString(name);
		p.writeInt(active ? 1 : 0);
		p.writeInt(silent ? 1 : 0);
		p.writeInt(vibrate ? 1 : 0);
		p.writeInt(ringer ? 1 : 0);
		p.writeString(ringtone);
		p.writeFloat(ringvolume);
		p.writeInt(override ? 1 : 0);
		p.writeInt(custom_notify ? 1 : 0);
		p.writeParcelable(uriRingTone, flags);

	}

	@Override
	public int describeContents() {

		return 0;
	}

}