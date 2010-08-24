/**
 * Class for Contacts table
 */
package com.fac.droidprofiles;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

/**
 * @author root
 * 
 */
public class Contacts {

	public static class Columns implements BaseColumns {

		// Uri for this table
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.fac.droidprofiles/dpcontacts");

		// Define tables
		// _ID = index 0
		public static final String PROFILE_ID = "profile_id"; // index 1
		public static final String NAME = "name"; // index 2
		public static final String PHONE = "phone"; // index 3
		public static final String EMAIL = "email"; // index 4
		// ContactsContract database ID
		public static final String REAL_ID = "real_id"; // index 5

		static final String[] CONTACTS_COLUMNS = { _ID, PROFILE_ID, NAME,
				PHONE, EMAIL, REAL_ID };

		// must match CONTACTS_COLUMNS above
		public static final int ID_INDEX = 0;
		public static final int PROFILE_ID_INDEX = 1;
		public static final int NAME_INDEX = 2;
		public static final int PHONE_INDEX = 3;
		public static final int EMAIL_INDEX = 4;
		public static final int REAL_ID_INDEX = 5;
	}
}