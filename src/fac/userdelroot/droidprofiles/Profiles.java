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

import java.util.HashMap;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 *  
 * 
 */
public class Profiles {

	public static final String PROFILE_ID = "_id";
	public static final String TAG = "Profiles";

	/**
	 * Gets all profiles
	 * 
	 * @param cr
	 *            ContentResolver
	 * @param omitId
	 *            Long omitId id to omit from result (optional)
	 * @return Cursor
	 */
	public static Cursor getProfiles(ContentResolver cr, long omitId) {

		String selection = null;
		if (omitId > 0) {
			selection = "_id !=" + omitId;
		}
		Cursor cursor = cr.query(Profile.Columns.CONTENT_URI,
				Profile.Columns.PROFILE_QUERY, selection, null, null);
		return cursor;
	}

	public static Profile getProfileByID(ContentResolver cr, int id) {

		Cursor cursor = cr.query(ContentUris.withAppendedId(
				Profile.Columns.CONTENT_URI, id),
				Profile.Columns.PROFILE_QUERY, null, null, null);
		Profile profile = null;
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				profile = new Profile(cursor);
			}
			cursor.close();
		}

		return profile;
	}

	/**
	 * Get the notification type by the profileId and type
	 * 
	 * @param cr
	 * @param profileId
	 * @param type
	 * @return Notify
	 */
	public static Notify getNotifyByProfileId(ContentResolver cr, int profileId, int type) {
		
		Uri uri = Notify.Columns.CONTENT_URI;
		Cursor cursor = null;
		Notify notify = null;
		
		switch(type) {
		case Notify.Columns.NOTIFY_TYPE_PHONE:
			cursor = cr.query(Uri.withAppendedPath(uri, "phone/" + profileId), Notify.Columns.NOTIFY_COLUMNS, null, null, null);
			
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					notify = new Notify(cursor);
				}
				cursor.close();
			}
			return notify;
		case Notify.Columns.NOTIFY_TYPE_SMS:
cursor = cr.query(Uri.withAppendedPath(uri, "phone/" + profileId), Notify.Columns.NOTIFY_COLUMNS, null, null, null);
			
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					notify = new Notify(cursor);
				}
				cursor.close();
			}
			return notify;
		case Notify.Columns.NOTIFY_TYPE_MMS:
cursor = cr.query(Uri.withAppendedPath(uri, "phone/" + profileId), Notify.Columns.NOTIFY_COLUMNS, null, null, null);
			
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					notify = new Notify(cursor);
				}
				cursor.close();
			}
			return notify;			
		case Notify.Columns.NOTIFY_TYPE_EMAIL:
cursor = cr.query(Uri.withAppendedPath(uri, "phone/" + profileId), Notify.Columns.NOTIFY_COLUMNS, null, null, null);
			
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					notify = new Notify(cursor);
				}
				cursor.close();
			}
			return notify;
			
		default: 
			Log.e(TAG + " Unknown notify type " + type);
			return null;
		}					
	}

	// TODO: fixme -
	// this actually is supposed to be Profile.*
	// AddProfile changes the values in Profile and update will grab those new
	// values

	@SuppressWarnings("unchecked")
	public static int saveProfile(ContentResolver resolver,
			HashMap<String, Comparable> profile) {
		ContentValues values = new ContentValues(profile.size());
		int id = Integer.parseInt(profile.get(Profile.Columns._ID).toString());

		if (Log.LOGV)
			Log.i("Profiles.java->saveProfile->profile " + profile.toString());

		values.put(Profile.Columns.NAME, profile.get(Profile.Columns.NAME)
				.toString());
		values.put(Profile.Columns.ACTIVE, profile.get(Profile.Columns.ACTIVE)
				.toString());
		values.put(Profile.Columns.SILENT, profile.get(Profile.Columns.SILENT)
				.toString());
		values.put(Profile.Columns.VIBRATE, profile
				.get(Profile.Columns.VIBRATE).toString());
		values.put(Profile.Columns.RINGER, profile.get(Profile.Columns.RINGER)
				.toString());
		values.put(Profile.Columns.RINGTONE, profile.get(
				Profile.Columns.RINGTONE).toString());
		values.put(Profile.Columns.RING_VOL, profile.get(
				Profile.Columns.RING_VOL).toString());
		values.put(Profile.Columns.OVERRIDES, profile.get(
				Profile.Columns.OVERRIDES).toString());
		values.put(Profile.Columns.CUSTOM_NOTIFY, profile.get(
				Profile.Columns.CUSTOM_NOTIFY).toString());
		profile.clear();

		if (id > 0) {

			return resolver.update(ContentUris.withAppendedId(
					Profile.Columns.CONTENT_URI, id), values, null, null);
		}

		Uri newUri = resolver.insert(Profile.Columns.CONTENT_URI, values);

		return Integer.valueOf(newUri.getPathSegments().get(1));
	}

	/**
	 * Contacts
	 */
	public static void deleteContactsbyProfileId(ContentResolver cr,
			long profileId) {
		cr.delete(Uri.withAppendedPath(Contacts.Columns.CONTENT_URI,
				"profiles/" + profileId), null, null);
	}

	public static void delContactsByRealId(ContentResolver cr, long real_id) {
		int numRows = 0;

		numRows = cr.delete(Uri.withAppendedPath(Contacts.Columns.CONTENT_URI,
				"realid/" + real_id), null, null);

		if (Log.LOGV)
			Log.i(TAG + " delContactsByReadId number of rows affected "
					+ numRows + " for id " + real_id);
	}

	public static void insertContacts(ContentResolver cr, long profileId,
			long contact_id) {
		ContentValues values = new ContentValues();
		values.put(Contacts.Columns.PROFILE_ID, profileId);
		values.put(Contacts.Columns.REAL_ID, contact_id);
		values.put(Contacts.Columns.EMAIL, "none");
		values.put(Contacts.Columns.NAME, "none");
		values.put(Contacts.Columns.PHONE, "none");
		cr.insert(Contacts.Columns.CONTENT_URI, values);
	}

	public static Cursor getContacts(ContentResolver cr, long profileId) {
		// invalid profile id
		if (profileId > 0) {
			// Uri uri = Overrides.Columns.CONTENT_URI;
			Cursor cursor = cr.query(Uri.withAppendedPath(
					Contacts.Columns.CONTENT_URI, "profiles/" + profileId),
					Contacts.Columns.CONTACTS_COLUMNS, null, null, null);

			return cursor;

		}
		return null;

	}
}
