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
     * @param cr ContentResolver
     * @param omitId Long omitId id to omit from result (optional)
     * @return Cursor
     */
    public static Cursor getProfiles(ContentResolver cr, long omitId) {

        String selection = null;
        if (omitId > 0) {
            selection = "_id !=" + omitId;
        }
        Cursor cursor = cr.query(Profile.Columns.CONTENT_URI, Profile.Columns.PROFILE_QUERY,
                selection, null, null);
        return cursor;
    }

    /**
     * Get profile by id
     * @param cr
     * @param id
     * @return
     */
    public static Profile getProfileByID(ContentResolver cr, int id) {

        Cursor cursor = cr.query(ContentUris.withAppendedId(Profile.Columns.CONTENT_URI, id),
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
     * Get contacts
     * @param cr
     * @param profileId
     * @return
     */
    public static Cursor getContacts(ContentResolver cr, long profileId) {
        // invalid profile id
        if (profileId > 0) {
            // Uri uri = Overrides.Columns.CONTENT_URI;
            Cursor cursor = cr.query(
                    Uri.withAppendedPath(Contacts.Columns.CONTENT_URI, "profiles/" + profileId),
                    Contacts.Columns.CONTACTS_COLUMNS, null, null, null);

            return cursor;

        }
        return null;

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
        switch (type) {
            case Notify.Columns.NOTIFY_TYPE_PHONE:
                cursor = cr.query(Uri.withAppendedPath(uri, "phone/" + profileId),
                        Notify.Columns.NOTIFY_COLUMNS, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        notify = new Notify(cursor);
                    }
                    cursor.close();
                }
                return notify;
            case Notify.Columns.NOTIFY_TYPE_SMS:
                cursor = cr.query(Uri.withAppendedPath(uri, "sms/" + profileId),
                        Notify.Columns.NOTIFY_COLUMNS, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        notify = new Notify(cursor);
                    }
                    cursor.close();
                }
                return notify;
            case Notify.Columns.NOTIFY_TYPE_MMS:
                cursor = cr.query(Uri.withAppendedPath(uri, "mms/" + profileId),
                        Notify.Columns.NOTIFY_COLUMNS, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        notify = new Notify(cursor);
                    }
                    cursor.close();
                }
                return notify;
            case Notify.Columns.NOTIFY_TYPE_EMAIL:
                cursor = cr.query(Uri.withAppendedPath(uri, "email/" + profileId),
                        Notify.Columns.NOTIFY_COLUMNS, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        notify = new Notify(cursor);
                    }
                    cursor.close();
                }
                return notify;

            default:
                if (Log.LOGV)
                    Log.e(TAG + " Unknown notify type " + type);
                return null;
        }
    }

    
    public static void saveNotifies(ContentResolver cr, Notify notify, int type,  long profileId) {
        
        // If we are null bailout quietly
        if (notify == null)
            return;
        
        ContentValues values = new ContentValues();
        values.put(Notify.Columns.PROFILE_ID, profileId);
        values.put(Notify.Columns.LED_ACTIVE, notify.led_active);
        values.put(Notify.Columns.LED_COLOR, notify.led_color);
        values.put(Notify.Columns.LED_PAT, notify.led_pat);
        values.put(Notify.Columns.NOTIFY_ACTIVE, notify.notify_active);
        values.put(Notify.Columns.NOTIFY_ICON, notify.notify_icon);
        values.put(Notify.Columns.NOTIFY_TYPE, type);
        values.put(Notify.Columns.RINGER_ACTIVE, notify.ringer_active);
        values.put(Notify.Columns.RINGER_VOL, notify.ringer_vol);
        values.put(Notify.Columns.RINGTONE, notify.ringtone);
        values.put(Notify.Columns.TB_ACTIVE, 0);
        values.put(Notify.Columns.TB_COLOR, "notused");
        values.put(Notify.Columns.TB_PAT, "notused");
        values.put(Notify.Columns.VIBRATE_ACTIVE, notify.vibrate_active);
        values.put(Notify.Columns.VIBRATE_PAT, notify.vibrate_pat);
        
        // check if there is a valid profile_id associated with this notify type
      
        
       // check if only need to update
       if (notify.profile_id > 0) { 
           
           // need updating
           cr.update(ContentUris.withAppendedId(Notify.Columns.CONTENT_URI, notify.id), values, null, null);
           return; 
       }
       
       // need inserting
       cr.insert(Notify.Columns.CONTENT_URI, values);
       
    }
    
    
    // TODO: fixme -
    // this actually is supposed to be Profile.* parcelable
    // AddProfile changes the values in Profile and update will grab those new
    // values.  
    public static int saveProfile(ContentResolver resolver, HashMap<String, Comparable> profile) {
        ContentValues values = new ContentValues(profile.size());
        int id = Integer.parseInt(profile.get(Profile.Columns._ID).toString());

        if (Log.LOGV)
            Log.i(TAG + "->saveProfile->profile " + profile.toString());

        values.put(Profile.Columns.NAME, profile.get(Profile.Columns.NAME).toString());
        values.put(Profile.Columns.ACTIVE, profile.get(Profile.Columns.ACTIVE).toString());
        values.put(Profile.Columns.SILENT, profile.get(Profile.Columns.SILENT).toString());
        values.put(Profile.Columns.VIBRATE, profile.get(Profile.Columns.VIBRATE).toString());
        values.put(Profile.Columns.RINGER, profile.get(Profile.Columns.RINGER).toString());
        values.put(Profile.Columns.RINGTONE, profile.get(Profile.Columns.RINGTONE).toString());
        values.put(Profile.Columns.RING_VOL, profile.get(Profile.Columns.RING_VOL).toString());
        values.put(Profile.Columns.OVERRIDES, profile.get(Profile.Columns.OVERRIDES).toString());
        values.put(Profile.Columns.CUSTOM_NOTIFY, profile.get(Profile.Columns.CUSTOM_NOTIFY)
                .toString());
        profile.clear();

        if (id > 0) {

            return resolver.update(ContentUris.withAppendedId(Profile.Columns.CONTENT_URI, id),
                    values, null, null);
        }

        Uri newUri = resolver.insert(Profile.Columns.CONTENT_URI, values);

        return Integer.valueOf(newUri.getPathSegments().get(1));
    }



    public static void saveContacts(ContentResolver cr, long profileId, long contact_id) {
        ContentValues values = new ContentValues();
        values.put(Contacts.Columns.PROFILE_ID, profileId);
        values.put(Contacts.Columns.REAL_ID, contact_id);
        values.put(Contacts.Columns.EMAIL, "none");
        values.put(Contacts.Columns.NAME, "none");
        values.put(Contacts.Columns.PHONE, "none");
        cr.insert(Contacts.Columns.CONTENT_URI, values);
    }


    /**
     * Delete profile
     * @param cr
     * @param profileId
     */
    public static void deleteProfile(ContentResolver cr, long profileId) {
        cr.delete(ContentUris.withAppendedId(Profile.Columns.CONTENT_URI, profileId), null, null);
        deleteContactsbyProfileId(cr, profileId);
        deleteNotifies(cr, profileId);
    }
    
    /**
     * Delete notifies by profile id
     * @param cr
     * @param profileId
     */
    public static void deleteNotifies(ContentResolver cr, long profileId) {
        cr.delete(ContentUris.withAppendedId(Notify.Columns.CONTENT_URI, profileId), null, null);
    }
    
    /**
     * Delete contacts by profile id
     * @param cr
     * @param profileId
     */
    public static void deleteContactsbyProfileId(ContentResolver cr, long profileId) {
        cr.delete(Uri.withAppendedPath(Contacts.Columns.CONTENT_URI, "profiles/" + profileId),
                null, null);
    }

    /**
     * Delete contacts by real contact id
     * @param cr
     * @param real_id
     */
    public static void delContactsByRealId(ContentResolver cr, long real_id) {
        int numRows = 0;

        numRows = cr
                .delete(Uri.withAppendedPath(Contacts.Columns.CONTENT_URI, "realid/" + real_id),
                        null, null);

        if (Log.LOGV)
            Log.i(TAG + "->delContactsByReadId number of rows affected " + numRows + " for id "
                    + real_id);
    }
}
