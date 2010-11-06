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
import android.provider.BaseColumns;

/**
 *  
 * 
 */
public class Contacts {

	public static class Columns implements BaseColumns {

		// Uri for this table
		public static final Uri CONTENT_URI = Uri.parse("content://fac.userdelroot.droidprofiles/dpcontacts");

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