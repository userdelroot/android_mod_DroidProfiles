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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class ProfilesHelper {

	
	private static final String TAG = "ProfilesHelper ";
	
	static String name;
	static String number;
	static long real_contact_id;
	private static ProfilesData mCurProfile;
	private static ContentResolver mCR;
	private static int mNotifyType;
	private  static String incomingNumber;
	private static boolean bShouldNotify;
	
	/**
	 * set default initial values for the ContentResolver, notifyType and incoming phone number
	 * then launch new thread
	 * @param cr
	 * @param phoneNum
	 * @param notifyType
	 */
	public static void initProcessing(Context context, String phoneNum, int notifyType) {

		
		bShouldNotify = false;
		mCR = context.getContentResolver();
		mNotifyType = notifyType;
		incomingNumber = phoneNum;
		mCurProfile = null;
		
		
		// the thread seems slower then just calling this directly ? weird
		//mThread = new Thread(null, initRunnable, "DroidProfiles->initProcessing");
		
	//	if (mThread != null )
	//		mThread.start();
		
		
		initBackground();		
	}
	

	
/*	private static Runnable initRunnable = new Runnable() {
		public void run() {
			initBackground();
		}
	};
	
*/
	private static void initBackground() {

		if (!getRealContactInfo()) return;
		
		if (!getProfileInfo()) return;
		
		
		if (!getActiveProfile())
			return;

		bShouldNotify = true;
	}

	private static boolean getActiveProfile() {
		// TODO Auto-generated method stub
		
		return true;
		
	}

	/**
	 * Gets the real contact information
	 * it was grab name / contact id and set the number val
	 */
	private static boolean getRealContactInfo() {
		
		if (mCR == null) return false;
		
		Uri lookup = Uri.withAppendedPath(Phone.CONTENT_FILTER_URI, Uri.encode(incomingNumber));

		String[] projection = { Phone.NUMBER, Phone.DISPLAY_NAME,
				Phone.CONTACT_ID };
		Cursor cursor = mCR.query(lookup, projection, null, null, null);

		if (cursor == null) return false;
		
		if (!cursor.moveToFirst()) return false;
		
			int numIndex = cursor.getColumnIndex(Phone.NUMBER);
			int displayIndex = cursor.getColumnIndex(Phone.DISPLAY_NAME);
			int idIndex = cursor.getColumnIndex(Phone.CONTACT_ID);
			name = cursor.getString(displayIndex);
			number = cursor.getString(numIndex);
			real_contact_id = cursor.getLong(idIndex);
			if (Log.LOGV)
				Log.v(TAG + "Contact: " + name + " number "
						+ number + " Contact ID " + real_contact_id);
			
			cursor.close();
		
		return true;
	}

	private static boolean getProfileInfo() {

		if (mCR == null) return false;

		Cursor cursor = null;
		switch (mNotifyType) {
		case Notify.Columns.NOTIFY_TYPE_PHONE: {
			Uri uri = ProfilesData.Columns.CONTENT_URI_PHONE;
			cursor = mCR.query(
					ContentUris.withAppendedId(uri, real_contact_id),
					ProfilesData.Columns.PROJECTION, null, null, null);
		}
			break;
		case Notify.Columns.NOTIFY_TYPE_SMS: {
			Uri uri = ProfilesData.Columns.CONTENT_URI_SMS;
			cursor = mCR.query(
					ContentUris.withAppendedId(uri, real_contact_id),
					ProfilesData.Columns.PROJECTION, null, null, null);
		}
			break;
		case Notify.Columns.NOTIFY_TYPE_MMS: {
			Uri uri = ProfilesData.Columns.CONTENT_URI_MMS;
			cursor = mCR.query(
					ContentUris.withAppendedId(uri, real_contact_id),
					ProfilesData.Columns.PROJECTION, null, null, null);
		}
			break;
		case Notify.Columns.NOTIFY_TYPE_EMAIL: {
			Uri uri = ProfilesData.Columns.CONTENT_URI_EMAIL;
			cursor = mCR.query(
					ContentUris.withAppendedId(uri, real_contact_id),
					ProfilesData.Columns.PROJECTION, null, null, null);
		}
			break;
		default:
			if (Log.LOGV)
				Log.v("-----No type was provided resorting to active----");
			return false;

		}

		if (cursor == null) return false;

		
		if (cursor.moveToFirst()== false) return false;
		
			mCurProfile = new ProfilesData(cursor);
			
			if (mCurProfile == null) 
				return false;
			
			
			// if we are here we can override
			if (Log.LOGV)
				Log.v(TAG + "Received Profile match " + mCurProfile.name + " for contact " + name);
				
			cursor.close();
			return true;

	}

	public static boolean getShouldNotify() {
		return bShouldNotify;
	}
	
	public static ProfilesData getProfileData() {

		return mCurProfile;
	}
}
