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

import java.util.Random;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 *  
 * 
 */
public class ProfilesProvider extends ContentProvider {

	
	private static final String TAG = "ProfilesProvider ";
	private SQLiteOpenHelper mOpenHelper;
	private static final int PROFILES = 1;
	private static final int PROFILES_ID = 2;
	private static final int PROFILE_PHONE = 3;
	private static final int PROFILE_SMS = 4;
	private static final int PROFILE_MMS = 5;
	private static final int PROFILE_EMAIL = 6;
	private static final int CONTACTS = 7;
	private static final int CONTACTS_PROFILE_ID = 8;
	private static final int CONTACTS_REAL_ID = 9;
	private static final int NOTIFY_PHONE = 10;
	private static final int NOTIFY_SMS = 11;
	private static final int NOTIFY_MMS = 12;
	private static final int NOTIFY_EMAIL = 13;
	private static final int NOTIFICATION = 14;
	private static final int NOTIFICATION_ID = 15;	
	
	private static final String AUTHORITY = "fac.userdelroot.droidprofiles";

	private static final UriMatcher sUriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);


	static {
		sUriMatcher.addURI(AUTHORITY, "profiles", PROFILES);
		sUriMatcher.addURI(AUTHORITY, "profiles/#", PROFILES_ID);
		sUriMatcher.addURI(AUTHORITY, "profiles/phone/#", PROFILE_PHONE);
		sUriMatcher.addURI(AUTHORITY, "profiles/sms/#", PROFILE_SMS);
		sUriMatcher.addURI(AUTHORITY, "profiles/mms/#", PROFILE_MMS);
		sUriMatcher.addURI(AUTHORITY, "profiles/email/#", PROFILE_EMAIL);
		sUriMatcher.addURI(AUTHORITY, "dpcontacts", CONTACTS);
		sUriMatcher.addURI(AUTHORITY, "dpcontacts/profiles/#", CONTACTS_PROFILE_ID); 
		sUriMatcher.addURI(AUTHORITY, "dpcontacts/realid/#", CONTACTS_REAL_ID);
		sUriMatcher.addURI(AUTHORITY, "notifications/phone/#", NOTIFY_PHONE);
		sUriMatcher.addURI(AUTHORITY, "notifications/sms/#", NOTIFY_SMS);
		sUriMatcher.addURI(AUTHORITY, "notifications/mms/#", NOTIFY_MMS);
		sUriMatcher.addURI(AUTHORITY, "notifications/email/#", NOTIFY_EMAIL);
		sUriMatcher.addURI(AUTHORITY, "notifications", NOTIFICATION);
		sUriMatcher.addURI(AUTHORITY, "notifications/#", NOTIFICATION_ID);
		
	};

	/*
	 * Table Defs
	 */
	private static final String PROFILES_TBL = "profiles";
	private static final String CONTACTS_TBL = "contacts";
	private static final String NOTIFICATION_TBL = "notifications";
	
	
	/**
	 * Joins
	 */
	private static final String JOIN_PHONE =  CONTACTS_TBL + " JOIN " + PROFILES_TBL 
						+ " ON (contacts.profile_id = profiles._id AND profiles.overrides=1)" 
						+ " LEFT OUTER JOIN " + NOTIFICATION_TBL 
						+ " ON (profiles._id = notifications.profile_id AND notifications.notify_type = 1)" ;
	
	
	private static final String JOIN_SMS =  CONTACTS_TBL +" JOIN " + PROFILES_TBL 
	+ " ON (contacts.profile_id = profiles._id AND profiles.overrides=1)" 
	+ " LEFT OUTER JOIN " + NOTIFICATION_TBL + " ON (profiles._id = notifications.profile_id AND notifications.notify_type = 2)" ;
	
	private static final String JOIN_MMS =  CONTACTS_TBL +" JOIN " + PROFILES_TBL 
	+ " ON (contacts.profile_id = profiles._id AND profiles.overrides=1)" 
	+ " LEFT OUTER JOIN " + NOTIFICATION_TBL + " ON (profiles._id = notifications.profile_id AND notifications.notify_type = 3)" ;
	private static final String JOIN_EMAIL =  CONTACTS_TBL +" JOIN " + PROFILES_TBL 
	+ " ON (contacts.profile_id = profiles._id AND profiles.overrides=1)" 
	+ " LEFT OUTER JOIN " + NOTIFICATION_TBL + " ON (profiles._id = notifications.profile_id AND notifications.notify_type = 4)";
	
	

	private static class DatabaseHelper extends SQLiteOpenHelper {

		private static final String DB_NAME = "profiles.db";
		private static final int DB_VER = 1;

		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VER);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// profiles table
			db.execSQL("CREATE TABLE profiles (" + "_id INTEGER PRIMARY KEY, "
					+ "name TEXT, " + "active INTEGER, " + "silent INTEGER, "
					+ "vibrate INTEGER, " + "ringer INTEGER, "
					+ "ringtone TEXT, " + "ringvol INTEGER, "
					+ "overrides INTEGER, " + "custom_notify INTEGER);");

			/*
			 * contacts table We store the default phone number for this contact
			 * and email address for quick references for the contact will bind
			 * to the primary phone number / email address
			 */
			db.execSQL("CREATE TABLE contacts (" + "_id INTEGER PRIMARY KEY, "
					+ "profile_id INTEGER, " + "name TEXT, " + "phone TEXT, "
					+ "email TEXT, " + "real_id, INTEGER);");

			// Build the below table columns here for future changes
			String sql = "_id INTEGER PRIMARY KEY, " + "profile_id INTEGER, "
					+ "led_active INTEGER, " + "led_color TEXT, "
					+ "led_pattern TEXT, " + "notifybar_active INTEGER, "
					+ "notifybar_icon TEXT, " + "ringer_active INTEGER, "
					+ "ringer_volume INTEGER, " + "ringtone TEXT, "
					+ "vibrate_active INTEGER, " + "vibrate_pattern TEXT, "
					+ "trackball_active INTEGER, "
					+ "trackball_color TEXT, "
					+ "trackball_pattern TEXT, " + "notify_type INTEGER);";

			// notification table 1 email 2 sms 3 mms 4 phone
			db.execSQL("CREATE TABLE notifications (" + sql);

			/*
			 * Fill the db with some default profiles to use Silent, vibrate
			 * only, loud
			 */
			db
					.execSQL("INSERT INTO profiles "
							+ "(name,active,silent,vibrate,ringer,ringtone,ringvol,overrides,custom_notify) "
							+ "VALUES " + "('Silent',0,1,0,1,'',0,0,0);");
			db
					.execSQL("INSERT INTO profiles "
							+ "(name,active,silent,vibrate,ringer,ringtone,ringvol,overrides,custom_notify) "
							+ "VALUES " + "('Normal',0,0,1,1,'',50,0,0);");
			db
					.execSQL("INSERT INTO profiles "
							+ "(name,active,silent,vibrate,ringer,ringtone,ringvol,overrides,custom_notify) "
							+ "VALUES " + "('Loud',1,0,1,1,'',100,0,0);");
			db
					.execSQL("INSERT INTO profiles "
							+ "(name,active,silent,vibrate,ringer,ringtone,ringvol,overrides,custom_notify) "
							+ "VALUES " + "('Vibrate',0,1,1,0,'',0,0,0);");

			db
					.execSQL("INSERT INTO notifications "
							+ "(profile_id,led_active,led_color,led_pattern,notifybar_active,"
							+ "notifybar_icon,ringer_active,ringer_volume,ringtone,vibrate_active,vibrate_pattern,notify_type) "
							+ "VALUES " + "(3,1,'0','0',1,'',0,0,1,0,'0',4);");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			Log.w(TAG + "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			try {
				db.execSQL("DROP TABLE IF EXISTS profiles");
				db.execSQL("DROP TABLE IF EXISTS contacts");
				db.execSQL("DROP TABLE IF EXISTS notifications");
				onCreate(db);

			} catch (SQLException e) {
				Log
						.e(TAG + "getting exception "
								+ e.getLocalizedMessage().toString());
			}
		}

	}

	/**
	 * Constructor don't allow class to be initialized
	 */
	public ProfilesProvider() {

	}

	
	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		if (mOpenHelper == null) {
			return false;
		}
		return true;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = 0;
		long rowId = 0;

		String seg = null;

		switch (sUriMatcher.match(uri)) {
		case PROFILES:
			count = db.delete(PROFILES_TBL, selection, selectionArgs);
			break;
		case PROFILES_ID:
			seg = uri.getPathSegments().get(1);
			rowId = Long.parseLong(seg);
			if (TextUtils.isEmpty(selection)) {
				selection = "_id= " + seg;
			} else {
				selection = "_id= " + seg + " AND (" + selection + ")";
			}
			count = db.delete(PROFILES_TBL, selection, selectionArgs);
			break;
		case CONTACTS:
			break;
		case CONTACTS_PROFILE_ID:
			seg = uri.getPathSegments().get(2);
			rowId = Long.parseLong(seg);
			if (TextUtils.isEmpty(selection)) {
				selection = "profile_id= " + seg;
			} else {
				selection = "profile_id= " + seg + " AND (" + selection + ")"; 
			}
			count = db.delete(CONTACTS_TBL, selection, selectionArgs);
			break;
			
		case CONTACTS_REAL_ID:
			seg = uri.getPathSegments().get(2);
			
			rowId = Long.parseLong(seg);
			if (TextUtils.isEmpty(selection)) {
				selection = "real_id= " + seg;
			} else {
				selection = "real_id= " + seg + " AND (" + selection + ")"; 
			}
			count = db.delete(CONTACTS_TBL, selection, selectionArgs);
			break;
			
		case NOTIFY_PHONE:
			seg = uri.getPathSegments().get(2);
			rowId = Long.parseLong(seg);
			if (TextUtils.isEmpty(selection)) {
				selection = "profile_id= " + seg + " AND notify_type = 1";
			}
			else {
				selection = "profile_id= " + seg + " AND (" + selection + ")";
			}
			count = db.delete(NOTIFICATION_TBL, selection, selectionArgs);
			break;
		case NOTIFY_SMS:
			seg = uri.getPathSegments().get(2);
			rowId = Long.parseLong(seg);
			if (TextUtils.isEmpty(selection)) {
				selection = "profile_id= " + seg + " AND notify_type = 2";
			}
			else {
				selection = "profile_id= " + seg + " AND (" + selection + ")";
			}
			count = db.delete(NOTIFICATION_TBL, selection, selectionArgs);
			break;
		case NOTIFY_MMS:
			seg = uri.getPathSegments().get(2);
			rowId = Long.parseLong(seg);
			if (TextUtils.isEmpty(selection)) {
				selection = "profile_id= " + seg + " AND notify_type = 3";
			}
			else {
				selection = "profile_id= " + seg + " AND (" + selection + ")";
			}
			count = db.delete(NOTIFICATION_TBL, selection, selectionArgs);
			break;
		case NOTIFY_EMAIL:
			seg = uri.getPathSegments().get(2);
			rowId = Long.parseLong(seg);
			if (TextUtils.isEmpty(selection)) {
				selection = "profile_id= " + seg + " AND notify_type = 4";
			}
			else {
				selection = "profile_id= " + seg + " AND (" + selection + ")";
			}
			count = db.delete(NOTIFICATION_TBL, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("cannot delete unknown Uri " + uri);
		}
		if (Log.LOGV) 
			Log.i(TAG + "delete id " + rowId + " rows affected " + count);

		db.close();
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		int match = sUriMatcher.match(uri);
		switch (match) {
		case PROFILES:
			return "vnd.android.cursor.dir/profiles";

		case PROFILES_ID:
			return "vnd.android.cursor.item/profiles";

		case PROFILE_PHONE:
			return "vnd.android.cursor.item/profiles/phone";
			
		case PROFILE_SMS:
			return "vnd.android.cursor.item/profiles/sms";
			
		case PROFILE_MMS:
			return "vnd.android.cursor.item/profiles/mms";

		case PROFILE_EMAIL:
			return "vnd.android.cursor.item/profiles/email";
			
		case CONTACTS:
			return "vnd.android.cursor.dir/dpcontacts";

		case CONTACTS_PROFILE_ID:
			return "vnd.android.cursor.dir/dpcontacts";
			
		case CONTACTS_REAL_ID:
			return "vnd.android.cursor.dir/dbcontacts/realid";
		
		case NOTIFY_PHONE:
			return "vnd.android.cursor.item/notification/phone";
		
		case NOTIFY_SMS:
			return "vnd.android.cursor.item/notification/sms";
		
		case NOTIFY_MMS:
			return "vnd.android.cursor.item/notification/mms";
		
		case NOTIFY_EMAIL:
			return "vnd.android.cursor.item/notification/email";
	
			
		case NOTIFICATION:
			return "vnd.android.cursor.dir/notification";
			
		case NOTIFICATION_ID:
			return "vnd.android.cursor.item/notification";
		
		
		default:
			throw new IllegalArgumentException("unknown Uri " + uri);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#insert(android.net.Uri,
	 * android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {

		ContentValues newValues = null;
		ContentValues outValues;
		Uri newUri = null;
		long rowId = 0;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if (db == null) {
			Log.v(TAG + " Fatal db object == null eeeek!");
			return null;
		}

		if (values != null) {
			outValues = new ContentValues(values);
		} else {
			outValues = new ContentValues();
		}

		switch (sUriMatcher.match(uri)) {
		case PROFILES: {
			newValues = preInsertProfiles(outValues);

			rowId = db.insert(PROFILES_TBL, null, newValues);
			if (rowId < 0)
				throw new SQLException("failed to insert profiles row " + uri);
			// update the insert row id
			// this is used for profile_id to link tables
			newUri = ContentUris.withAppendedId(Profile.Columns.CONTENT_URI,
					rowId);
			getContext().getContentResolver().notifyChange(newUri, null);
		}
			break;
		case CONTACTS: {
		    rowId = db.insert(CONTACTS_TBL, null, outValues);
			if (rowId < 0)
				throw new SQLException("failed to insert contacts row " + uri);
			newUri = ContentUris.withAppendedId(Contacts.Columns.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(newUri, null);
		}
		
		case NOTIFICATION: {
			rowId = db.insert(NOTIFICATION_TBL, null, outValues);
			if (rowId < 0)
				throw new SQLException("failed to insert notification row " + uri);
			newUri = ContentUris.withAppendedId(Notify.Columns.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(newUri, null);
		}
		
			break;
		default:
			throw new IllegalArgumentException("Cannot insert null: " + uri);
		}
		
		if (Log.LOGV)
			Log.i(TAG + " new _id " + rowId + " new uri " + newUri );

		db.close();
		return newUri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#query(android.net.Uri,
	 * java.lang.String[], java.lang.String, java.lang.String[],
	 * java.lang.String)
	 * 
	 * Build up out query
	 */
	@Override
	public Cursor query(Uri uri, String[] projectionIn, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder q = new SQLiteQueryBuilder();

		int match = sUriMatcher.match(uri);
		
		switch (match) {
		case PROFILES:
			q.setTables(PROFILES_TBL);
			break;
		case PROFILES_ID:
			q.setTables(PROFILES_TBL);
			q.appendWhere("_id=");
			q.appendWhere(uri.getPathSegments().get(1));
			break;
		case PROFILE_PHONE:
			q.setTables(JOIN_PHONE);
			q.appendWhere("contacts.real_id=" + uri.getPathSegments().get(2));
			break;
		case PROFILE_SMS:
			q.setTables(JOIN_SMS);
			q.appendWhere("contacts.real_id=" + uri.getPathSegments().get(2));
			break;
		case PROFILE_MMS:
			q.setTables(JOIN_MMS);
			q.appendWhere("contacts.real_id=" + uri.getPathSegments().get(2));
			break;
		case PROFILE_EMAIL:
			q.setTables(JOIN_EMAIL);
			q.appendWhere("contacts.real_id=" + uri.getPathSegments().get(2));
			break;
		case CONTACTS:
			q.setTables(CONTACTS_TBL);
			break;
		case CONTACTS_PROFILE_ID:
			q.setTables(CONTACTS_TBL);
			q.appendWhere("profile_id=");
			q.appendWhere(uri.getPathSegments().get(2));
			break;
			
		case NOTIFY_PHONE:
			q.setTables(NOTIFICATION_TBL);
			q.appendWhere("profile_id=");
			q.appendWhere(uri.getPathSegments().get(2));
			q.appendWhere(" AND notify_type = 1");
			break;
		case NOTIFY_SMS:
			q.setTables(NOTIFICATION_TBL);
			q.appendWhere("profile_id=");
			q.appendWhere(uri.getPathSegments().get(2));
			q.appendWhere(" AND notify_type = 2");
			break;
			
		case NOTIFY_MMS:
			q.setTables(NOTIFICATION_TBL);
			q.appendWhere("profile_id=");
			q.appendWhere(uri.getPathSegments().get(2));
			q.appendWhere(" AND notify_type = 3");
			break;
			
		case NOTIFY_EMAIL:
			q.setTables(NOTIFICATION_TBL);
			q.appendWhere("profile_id=");
			q.appendWhere(uri.getPathSegments().get(2));
			q.appendWhere(" AND notify_type = 4");
			break;
			
		default:
			throw new IllegalArgumentException("unknown Uri " + uri);
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor ret = q.query(db, projectionIn, selection, selectionArgs, null,
				null, sortOrder);

		if (ret == null) {
			Log.v(TAG + " Query result == null");
			return null;
		} else {
			ret.setNotificationUri(getContext().getContentResolver(), uri);
		}
		//db.close();
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#update(android.net.Uri,
	 * android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count;
		long rowId = 0;
		int match = sUriMatcher.match(uri);
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		switch (match) {
		case PROFILES_ID: {
			String segment = uri.getPathSegments().get(1);
			rowId = Long.parseLong(segment);
			count = db.update(PROFILES_TBL, values, "_id= " + rowId, null);	
		}
		break;
		case NOTIFICATION_ID: {
			String segment = uri.getPathSegments().get(1);
			rowId = Long.parseLong(segment);
			count = db.update(NOTIFICATION_TBL, values, "_id= "+ rowId , null);
		}
		break;
		default: {
			throw new UnsupportedOperationException("Cannot update URL: " + uri);
		}
		}

		getContext().getContentResolver().notifyChange(uri, null);
		db.close();
		return count;

	}

	// Pre insert build up the values if null with default values
	// no value == 0 or '' depends on dataType
	private ContentValues preInsertProfiles(ContentValues values) {

		// Do not allow null profile names
		// it should never get to here because of the checking done in
		// ProfilesActivity.class
		// but just in case set Blank-Random_number
		if (!values.containsKey(Profile.Columns.NAME)) {
			Random rand = new Random();
			values.put(Profile.Columns.NAME, "\"Blank-\""
					+ String.valueOf(rand.nextInt()));
		}
		if (!values.containsKey(Profile.Columns.ACTIVE)) {
			values.put(Profile.Columns.ACTIVE, 0);
		}
		if (!values.containsKey(Profile.Columns.SILENT)) {
			values.put(Profile.Columns.SILENT, 0);
		}
		if (!values.containsKey(Profile.Columns.VIBRATE)) {
			values.put(Profile.Columns.VIBRATE, 0);

			if (!values.containsKey(Profile.Columns.RINGER)) {
				values.put(Profile.Columns.RINGER, 0);
			}
		}
		if (!values.containsKey(Profile.Columns.RINGTONE)) {
			values.put(Profile.Columns.RINGTONE, "");
		}
		if (!values.containsKey(Profile.Columns.RING_VOL)) {
			values.put(Profile.Columns.RING_VOL, 0.100f);
		}
		if (!values.containsKey(Profile.Columns.OVERRIDES)) {
			values.put(Profile.Columns.OVERRIDES, 0);
		}
		if (!values.containsKey(Profile.Columns.CUSTOM_NOTIFY)) {
			values.put(Profile.Columns.CUSTOM_NOTIFY, 0);
		}

		return values;
	}
}