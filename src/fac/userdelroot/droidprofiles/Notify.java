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
 * Parcelable Notify from the notifications table
 */
public final class Notify implements Parcelable {

    public static final Parcelable.Creator<Notify> CREATOR = new Parcelable.Creator<Notify>() {
        public Notify createFromParcel(Parcel p) {
            return new Notify(p);
        }

        public Notify[] newArray(int size) {
            return new Notify[size];
        }
    };

    public static class Columns implements BaseColumns {

        // Uri for this table
        public static final Uri CONTENT_URI = Uri
                .parse("content://fac.userdelroot.droidprofiles/notifications");

        // Define tables
        // _ID == index 0
        public static final String PROFILE_ID = "profile_id"; // index 1

        public static final String LED_ACTIVE = "led_active"; // index 2

        public static final String LED_COLOR = "led_color"; // index 3

        public static final String LED_PAT = "led_pattern"; // index 4

        public static final String NOTIFY_ACTIVE = "notifybar_active"; // index

        // 5
        public static final String NOTIFY_ICON = "notifybar_icon"; // index 6

        public static final String RINGER_ACTIVE = "ringer_active"; // index 7

        public static final String RINGER_VOL = "ringer_volume"; // index 8

        public static final String RINGTONE = "ringtone"; // index 9

        public static final String VIBRATE_ACTIVE = "vibrate_active"; // index

        // 10
        public static final String VIBRATE_PAT = "vibrate_pattern"; // index 11

        public static final String TB_ACTIVE = "trackball_active"; // index 12

        public static final String TB_COLOR = "trackball_color"; // index 13

        public static final String TB_PAT = "trackball_pattern"; // index 14

        public static final String NOTIFY_TYPE = "notify_type"; // index 15

        // types of notifications
        public static final int NOTIFY_TYPE_PHONE = 1; // prime type

        public static final int NOTIFY_TYPE_SMS = 2;

        public static final int NOTIFY_TYPE_MMS = 3;

        public static final int NOTIFY_TYPE_EMAIL = 4;

        public static final String[] NOTIFY_COLUMNS = {
                _ID, PROFILE_ID, LED_ACTIVE, LED_COLOR, LED_PAT, NOTIFY_ACTIVE, NOTIFY_ICON,
                RINGER_ACTIVE, RINGER_VOL, RINGTONE, VIBRATE_ACTIVE, VIBRATE_PAT, TB_ACTIVE,
                TB_COLOR, TB_PAT, NOTIFY_TYPE
        };

        // Must match EMAILS_COLUMNS or shit will break !!
        public static final int ID_INDEX = 0;

        public static final int PROFILE_ID_INDEX = 1;

        public static final int LED_ACTIVE_INDEX = 2;

        public static final int LED_COLOR_INDEX = 3;

        public static final int LED_PAT_INDEX = 4;

        public static final int NOTIFY_ACTIVE_INDEX = 5;

        public static final int NOTIFY_ICON_INDEX = 6;

        public static final int RINGER_ACTIVE_INDEX = 7;

        public static final int RINGER_VOL_INDEX = 8;

        public static final int RINGTONE_INDEX = 9;

        public static final int VIBRATE_ACTIVE_INDEX = 10;

        public static final int VIBRATE_PAT_INDEX = 11;

        public static final int TB_ACTIVE_INDEX = 12;

        public static final int TB_COLOR_INDEX = 13;

        public static final int TB_PAT_INDEX = 14;

        public static final int NOTIFY_TYPE_INDEX = 15;
    }

    // For logging
    // private static final String CLASS_NAME = "Emails";

    // public vars
    public int id;

    public int profile_id;

    public boolean led_active;

    public String led_color;

    public String led_pat;

    public boolean notify_active;

    // TODO: notify_icon Needs to be modified to a Uri
    public String notify_icon;

    public boolean ringer_active;

    public int ringer_vol;

    // TODO: ringtone needs to be modified to a Uri
    public String ringtone;

    public boolean vibrate_active;

    public String vibrate_pat;

    public boolean tb_active;

    public String tb_color;

    public String tb_pat;

    public int notify_type;

    /**
     * Constructor from a Cursor
     * 
     * @param c
     */
    public Notify(Cursor c) {
        id = c.getInt(Columns.ID_INDEX);
        profile_id = c.getInt(Columns.PROFILE_ID_INDEX);
        led_active = c.getInt(Columns.LED_ACTIVE_INDEX) == 1;
        led_color = c.getString(Columns.LED_COLOR_INDEX);
        led_pat = c.getString(Columns.LED_PAT_INDEX);
        notify_active = c.getInt(Columns.NOTIFY_ACTIVE_INDEX) == 1;
        notify_icon = c.getString(Columns.NOTIFY_ICON_INDEX);
        ringer_active = c.getInt(Columns.RINGER_ACTIVE_INDEX) == 1;
        ringer_vol = c.getInt(Columns.RINGER_VOL_INDEX);
        ringtone = c.getString(Columns.RINGTONE_INDEX);
        vibrate_active = c.getInt(Columns.VIBRATE_ACTIVE_INDEX) == 1;
        vibrate_pat = c.getString(Columns.VIBRATE_PAT_INDEX);
        tb_active = c.getInt(Columns.TB_ACTIVE_INDEX) == 1;
        tb_color = c.getString(Columns.TB_COLOR_INDEX);
        tb_pat = c.getString(Columns.TB_PAT_INDEX);
        notify_type = c.getInt(Columns.NOTIFY_TYPE_INDEX);
    }

    /**
     * Constructor from a parcel
     * 
     * @param p
     */
    public Notify(Parcel p) {
        id = p.readInt();
        profile_id = p.readInt();
        led_active = p.readInt() == 1;
        led_color = p.readString();
        led_pat = p.readString();
        notify_active = p.readInt() == 1;
        notify_icon = p.readString();
        ringer_active = p.readInt() == 1;
        ringer_vol = p.readInt();
        ringtone = p.readString();
        vibrate_active = p.readInt() == 1;
        vibrate_pat = p.readString();
        tb_active = p.readInt() == 1;
        tb_color = p.readString();
        tb_pat = p.readString();
        notify_type = p.readInt();
    }

    /**
     * Constructor for this parcelable object
     */
    public Notify() {
    }

    @Override
    public void writeToParcel(Parcel p, int flags) {
        p.writeInt(id);
        p.writeInt(profile_id);
        p.writeInt(led_active ? 1 : 0);
        p.writeString(led_color);
        p.writeString(led_pat);
        p.writeInt(notify_active ? 1 : 0);
        p.writeString(notify_icon);
        p.writeInt(ringer_active ? 1 : 0);
        p.writeInt(ringer_vol);
        p.writeString(ringtone);
        p.writeInt(vibrate_active ? 1 : 0);
        p.writeString(vibrate_pat);
        p.writeInt(tb_active ? 1 : 0);
        p.writeString(tb_color);
        p.writeString(tb_pat);
        p.writeInt(notify_type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
