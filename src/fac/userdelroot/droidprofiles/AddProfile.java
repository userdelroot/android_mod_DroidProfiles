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

import java.util.ArrayList;
import java.util.HashMap;
import fac.userdelroot.droidprofiles.pref.RingtonePref;
import fac.userdelroot.droidprofiles.pref.VolumePref;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Add a profile class
 */
public class AddProfile extends PreferenceActivity implements OnPreferenceChangeListener,
        OnPreferenceClickListener {

    private static final String TAG = "AddProfile ";

    // Menu
    private static final int MENU_SAVE = Menu.FIRST;

    private static final int MENU_CANCEL = Menu.FIRST + 1;

    private static final int NOTIFY_PHONE = Notify.Columns.NOTIFY_TYPE_PHONE;

    private static final int NOTIFY_SMS = Notify.Columns.NOTIFY_TYPE_SMS;

    private static final int NOTIFY_MMS = Notify.Columns.NOTIFY_TYPE_MMS;

    private static final int NOTIFY_EMAIL = Notify.Columns.NOTIFY_TYPE_EMAIL;

    // variables
    private static final String KEY_PROFILE_NAME = "profile_name";

    private static final String KEY_PROFILE_ACTIVE = "profile_active";

    private static final String KEY_PROFILE_SILENT = "profile_silent";

    private static final String KEY_PROFILE_VIBRATE = "profile_vibrate";

    private static final String KEY_PROFILE_RING = "profile_ringer";

    private static final String KEY_RINGTONE = "profile_ringtone";

    private static final String KEY_PROFILE_OVERRIDE = "profile_override";

    private static final String KEY_PROFILE_CUSTOM = "profile_custom";

    // Preferernces
    private EditTextPreference mProfName;

    private CheckBoxPreference mProfActive, mProfSilent, mProfVibrate, mProfRing, mProfOverride,
            mProfCustom;

    private PreferenceScreen mEmailNotify, mSmsNotify, mMmsNotify, mPhoneNotify, mContactScreen;

    private RingtonePref mProfRingtone;

    private VolumePref mProfRingVolume;

    // private vars
    private long mProfileId;

    private Notify pPhone;

    private Notify pSms;

    private Notify pMms;

    private Notify pEmail;

    @SuppressWarnings("unchecked")
    private static final HashMap<String, Comparable> mProfile = new HashMap<String, Comparable>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.add_profile);

        mProfName = (EditTextPreference) this.findPreference(KEY_PROFILE_NAME);
        mProfActive = (CheckBoxPreference) this.findPreference(KEY_PROFILE_ACTIVE);
        mProfSilent = (CheckBoxPreference) this.findPreference(KEY_PROFILE_SILENT);
        mProfVibrate = (CheckBoxPreference) this.findPreference(KEY_PROFILE_VIBRATE);
        mProfRing = (CheckBoxPreference) this.findPreference(KEY_PROFILE_RING);
        mProfOverride = (CheckBoxPreference) this.findPreference(KEY_PROFILE_OVERRIDE);
        mProfCustom = (CheckBoxPreference) this.findPreference(KEY_PROFILE_CUSTOM);

        mContactScreen = (PreferenceScreen) this.findPreference("profile_contacts");
        mProfRingtone = (RingtonePref) this.findPreference(KEY_RINGTONE);
        mProfRingVolume = (VolumePref) this.findPreference("profile_ring_volume");

        // // we do not want silent to be shown.
        // mRingtone.setShowSilent(false);

        mEmailNotify = (PreferenceScreen) this.findPreference("email_notify");
        mSmsNotify = (PreferenceScreen) this.findPreference("sms_notify");
        mMmsNotify = (PreferenceScreen) this.findPreference("mms_notify");
        mPhoneNotify = (PreferenceScreen) this.findPreference("phone_notify");

        /**
         * This should never be null if they are something is horribly wrong
         */
        if (mProfName == null || mProfActive == null || mProfSilent == null || mProfVibrate == null
                || mProfRing == null || mProfOverride == null || mProfCustom == null
                || mContactScreen == null || mProfRingtone == null || mProfRingVolume == null) {
            Log.e(this.getClass().getSimpleName().toString()
                    + " something is borked!!! bail bail bail!!!!!!!");

            // bail bail bail
            finish();
            return;
        }
        // same as above
        if (mEmailNotify == null || mSmsNotify == null || mMmsNotify == null
                || mPhoneNotify == null) {
            Log.e(this.getClass().getSimpleName().toString()
                    + " something is borked!!! bail bail bail!!!!!!!");
            finish();
            return;
        }

        // set click listeners
        setListeners();

        // not needed
        // mListOverrides.setOnPreferenceClickListener(this);

        // initialize our profileID if there is one
        getIntentID();

        // clear helpercontacts
        HelperContacts.clearAll();

        // set initial values here from database
        loadDefaultValues();

        // we want all of them
        getNotification(null);
    }

    private void getNotification(Integer notifyType) {

        // this needs to be a valid ID or we are creating a new profile
        if (mProfileId <= 0)
            return;

        // if null we want all of them
        if (notifyType == null) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    pPhone = Profiles.getNotifyByProfileId(getContentResolver(), (int) mProfileId,
                            Notify.Columns.NOTIFY_TYPE_PHONE);
                    pSms = Profiles.getNotifyByProfileId(getContentResolver(), (int) mProfileId,
                            Notify.Columns.NOTIFY_TYPE_SMS);
                    pMms = Profiles.getNotifyByProfileId(getContentResolver(), (int) mProfileId,
                            Notify.Columns.NOTIFY_TYPE_MMS);
                    pEmail = Profiles.getNotifyByProfileId(getContentResolver(), (int) mProfileId,
                            Notify.Columns.NOTIFY_TYPE_EMAIL);
                }
            };
            t.start();
        }

    }

    /**
     * set the click listeners
     */
    private final void setListeners() {

        mProfName.setOnPreferenceChangeListener(this);
        mProfActive.setOnPreferenceClickListener(this);
        mProfSilent.setOnPreferenceClickListener(this);
        mProfVibrate.setOnPreferenceClickListener(this);
        mProfRing.setOnPreferenceClickListener(this);
        mProfOverride.setOnPreferenceClickListener(this);
        mProfCustom.setOnPreferenceClickListener(this);
        mContactScreen.setOnPreferenceClickListener(this);
        mProfRingVolume.setOnPreferenceClickListener(this);
        mEmailNotify.setOnPreferenceClickListener(this);
        mSmsNotify.setOnPreferenceClickListener(this);
        mMmsNotify.setOnPreferenceClickListener(this);
        mPhoneNotify.setOnPreferenceClickListener(this);
    }

    /**
     * get the intent extras (profile._id) This will return a valid ID if a
     * profile is being edited if not it will return -1 which is invalid and
     * build the default values manually
     */
    private void getIntentID() {

        Intent intent = getIntent();
        // get id
        mProfileId = intent.getLongExtra(Profiles.PROFILE_ID, -1);
    }

    private void loadDefaultValues() {

        // If the Id is valid we have a valid profile to edit
        // if it is invalid we have a new profile to create
        if (mProfileId > 0) { // is id valid?

            Profile profile = Profiles.getProfileByID(getContentResolver(), (int) mProfileId);
            // this should never be null here because there is a valid
            // profile._ID
            // but check it anyways in case something gets borked!
            if (profile != null) {

                // We are editing a profile

                // set the values from the database
                mProfName.setText(profile.name);
                mProfActive.setChecked(profile.active);
                mProfSilent.setChecked(profile.silent);
                mProfVibrate.setChecked(profile.vibrate);
                mProfRing.setChecked(profile.ringer);
                mProfRingtone.setRingtone(Uri.parse(profile.ringtone));
                mProfRingVolume.setVolume((int) profile.ringvolume);
                mProfRingVolume.setRingtone(Uri.parse(profile.ringtone));
                mProfOverride.setChecked(profile.override);
                mProfCustom.setChecked(profile.custom_notify);

                // set default name because this is an edit
                mProfile.put(Profile.Columns.NAME, profile.name);

            }
        } else {

            // We are creating a new profile
            mProfName.setText(null);
            mProfActive.setChecked(false);
            mProfSilent.setChecked(false);
            mProfVibrate.setChecked(false);
            mProfRing.setChecked(false);
            mProfRingtone.setRingtone(null);
            mProfRingVolume.setVolume(50);
            mProfRingVolume.setRingtone(null);
            mProfOverride.setChecked(false);
            mProfCustom.setChecked(false);

            // Load override profile list

        }

        // set the intial values for a LinkedHaskMap (mProfile)
        mProfile.put(Profile.Columns._ID, mProfileId); // set for edits.

        mProfile.put(Profile.Columns.ACTIVE, mProfActive.isChecked() ? 1 : 0);
        mProfile.put(Profile.Columns.SILENT, mProfSilent.isChecked() ? 1 : 0);
        mProfile.put(Profile.Columns.VIBRATE, mProfVibrate.isChecked() ? 1 : 0);
        mProfile.put(Profile.Columns.RINGER, mProfRing.isChecked() ? 1 : 0);
        mProfile.put(Profile.Columns.RINGTONE, mProfRingtone.getRingtone());
        mProfile.put(Profile.Columns.RING_VOL, mProfRingVolume.getVolume());
        mProfile.put(Profile.Columns.OVERRIDES, mProfOverride.isChecked() ? 1 : 0);
        mProfile.put(Profile.Columns.CUSTOM_NOTIFY, mProfCustom.isChecked() ? 1 : 0);
    }

    /*
     * Do something if the preference has changed update summary set newValue
     * into the LinkedHashMap (mProfile) key is the db table name value is well
     * the value use the actual Profile.Columns.TBL_NAME to set the key. so
     * there are no issues
     */
    @Override
    public boolean onPreferenceChange(Preference pref, Object newValue) {

        if (pref == mProfName) {

            // store it
            mProfile.put(Profile.Columns.NAME, newValue.toString());

            // Change Summary to reflect our changes
            // TODO: Add this to the string resource file to format the text
            mProfName.setSummary(newValue.toString());
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_SAVE, 0, R.string.menu_save).setIcon(android.R.drawable.ic_menu_save);
        menu.add(0, MENU_CANCEL, 0, R.string.menu_cancel).setIcon(
                android.R.drawable.ic_menu_close_clear_cancel);
        // mAddingProfile ? R.string.profile_menu_cancel :
        // R.string.vpn_menu_revert).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // we assume cancel here
                showCancelDialog();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SAVE:
                // TODO: validation needs to be done, make sure profile_name !=
                // null
                if (validate()) {
                    saveProfile();
                    // TODO: set this to show Saving or Updating depending on
                    // what
                    // we are doing
                    Toast.makeText(this, "Saving " + mProfName.getText(), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, " Please enter a Profile Name ", Toast.LENGTH_LONG).show();
                }

                return true;

            case MENU_CANCEL:
                showCancelDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * write the profile to the database. TODO: thread this entire thing
     */
    private void saveProfile() {
        // need to grab the ringtone -)
        if (mProfRingtone.getRingtone() != null)
            mProfile.put(Profile.Columns.RINGTONE, mProfRingtone.getRingtone());
        else
            mProfile.put(Profile.Columns.RINGTONE, "");
        // grab the volume
        mProfile.put(Profile.Columns.RING_VOL, mProfRingVolume.getVolume());

        if (Log.LOGV)
            Log.v("AddProfile->saveProfile " + mProfile.toString());
        // if this profile is valid we are just editing
        if (mProfileId > 0) {
            Profiles.saveProfile(getContentResolver(), mProfile);
        } else {
            // we are creating new profile get profileID
            mProfileId = Profiles.saveProfile(getContentResolver(), mProfile);
        }

        // add the contacts
        addContacts();

    }

    /**
     * adds Contacts to the profile
     */
    private void addContacts() {

        // check if the contacts list has changed if not bail out
        if (!HelperContacts.hashChanged())
            return;

        // contacts
        // TODO:
        // -This is going to work the same as the overrides as it only stores
        // _ID profile_id real_contact_id
        // so delete old data and add new data
        // these methods run quite fast so I assume that should not be an issue
        // on any android enabled device.
        // until I find a better solution this works
        // NOTE: this may change to per a notification basis in future releases

        // first delete all profile contacts match real_id
        // we only allow 1 contact per a profile

        ArrayList<Long> list = HelperContacts.getList();

        if (list == null)
            return;

        // this contact does not want any contacts associated so delete em
        if (list.isEmpty()) {
            Profiles.deleteContactsbyProfileId(getContentResolver(), mProfileId);
        }

        // first delete all contacts with real_id
        // we do not allow multiple contacts
        // fixme: this is kind of lame, we need a query profile_id AND real_id =
        // that would eliminate this
        // and would also fix it in the ProfilesHelper.class

        if (list.isEmpty() == false) {

            // delete all contacts associated with this profile
            Profiles.deleteContactsbyProfileId(getContentResolver(), mProfileId);

            // delete all contacts associated with other profiles
            for (long real_id : list) {
                Profiles.delContactsByRealId(getContentResolver(), real_id);

            }

            // add all contacts to this profile
            for (long id : list) {
                Profiles.insertContacts(getContentResolver(), mProfileId, id);
            }

        }

    }

    // TODO: Add strings to the string resource
    private void showCancelDialog() {
        new AlertDialog.Builder(this).setTitle("Dialog title")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure you want to cancel?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("Cancel", null).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mProfName.setOnPreferenceChangeListener(null);
        mProfActive.setOnPreferenceClickListener(null);
        mProfSilent.setOnPreferenceClickListener(null);
        mProfVibrate.setOnPreferenceClickListener(null);
        mProfRing.setOnPreferenceClickListener(null);
        mProfOverride.setOnPreferenceClickListener(null);
        mProfCustom.setOnPreferenceClickListener(null);
        mProfRingVolume.setOnPreferenceClickListener(null);
        mContactScreen.setOnPreferenceClickListener(null);
        mEmailNotify.setOnPreferenceClickListener(null);
        mSmsNotify.setOnPreferenceClickListener(null);
        mMmsNotify.setOnPreferenceClickListener(null);
        mPhoneNotify.setOnPreferenceClickListener(null);
    }

    /*
     * I guess this is how it is suppose to work but seems silly
     * OnPreferenceChange() did not work with the checked items, it would
     * provide the previous state and not the current state which was screwing
     * up everything Maybe I misread the docs but if a state is changed that
     * means just that it changed so if i click a check box to checked, the
     * stated changed to isChecked == true. But that is not the case Soooooooo,
     * the below provides the current state for isChecked.
     */
    @Override
    public boolean onPreferenceClick(Preference pref) {

        if (pref == mProfActive) {

            mProfile.put(Profile.Columns.ACTIVE, mProfActive.isChecked() ? 1 : 0);
            mProfActive.setSummary(mProfActive.isChecked() ? "yes" : "no");

            return true;
        }

        if (pref == mProfSilent) {
            mProfile.put(Profile.Columns.SILENT, mProfSilent.isChecked() ? 1 : 0);

            mProfSilent.setSummary(mProfSilent.isChecked() ? "yes" : "no");
            return true;
        }

        if (pref == mProfVibrate) {
            mProfile.put(Profile.Columns.VIBRATE, mProfVibrate.isChecked() ? 1 : 0);
            mProfVibrate.setSummary(mProfVibrate.isChecked() ? "yes" : "no");
            return true;
        }

        if (pref == mProfRing) {
            mProfile.put(Profile.Columns.RINGER, mProfRing.isChecked() ? 1 : 0);
            mProfRing.setSummary(mProfRing.isChecked() ? "yes" : "no");
            return true;
        }
        if (pref == mProfOverride) {
            mProfile.put(Profile.Columns.OVERRIDES, mProfOverride.isChecked() ? 1 : 0);
            mProfOverride.setSummary(mProfOverride.isChecked() ? "yes" : "no");
            return true;
        }

        if (pref == mProfRingVolume) {
            mProfRingVolume.setVolume(Integer.valueOf(mProfile.get(Profile.Columns.RING_VOL)
                    .toString()));
        }

        if (pref == mProfCustom) {
            mProfile.put(Profile.Columns.CUSTOM_NOTIFY, mProfCustom.isChecked() ? 1 : 0);
            mProfCustom.setSummary(mProfCustom.isChecked() ? "yes" : "no");
            return true;
        }

        if (pref == mContactScreen) {
            Intent intent = new Intent(this, ContactsActivity.class);
            intent.putExtra(Notify.Columns._ID, mProfileId);
            startActivity(intent);
            return true;
        }

        if (pref == mEmailNotify) {

            if (Log.LOGV)
                Log.v("email notify click start intent");

            // if this is null, this is either a new profile or profile has no
            // notifications
            if (pEmail == null)
                pEmail = new Notify();

            Intent intent = new Intent(this, EmailPreferences.class);
            intent.putExtra("fac.userdelroot.droidprofiles.Notify", pEmail);
            startActivity(intent);
            return true;
        }
        if (pref == mSmsNotify) {
            if (Log.LOGV)
                Log.v("sms notify click start intent");

            // if this is null, this is either a new profile or profile has no
            // notifications
            if (pSms == null)
                pSms = new Notify();

            Intent intent = new Intent(this, SMSPreferences.class);
            intent.putExtra("fac.userdelroot.droidprofiles.Notify", pSms);
            startActivity(intent);

            return true;
        }
        if (pref == mMmsNotify) {
            if (Log.LOGV)
                Log.v("mms notify click start intent");

            // if this is null, this is either a new profile or profile has no
            // notifications
            if (pMms == null)
                pMms = new Notify();

            Intent intent = new Intent(this, MMSPreferences.class);
            intent.putExtra("fac.userdelroot.droidprofiles.Notify", pMms);
            startActivity(intent);

            return true;
        }
        if (pref == mPhoneNotify) {
            if (Log.LOGV)
                Log.v("phone notify click start intent");

            // if this is null, this is either a new profile or profile has no
            // notifications
            if (pPhone == null)
                pPhone = new Notify();

            Intent intent = new Intent(this, PhonePreferences.class);
            intent.putExtra("fac.userdelroot.droidprofiles.Notify", pPhone);
            startActivity(intent);
            return true;
        }

        return true;
    }

    /**
     * Validatation
     * 
     * @return
     */
    private boolean validate() {
        // Context c = mProfName.getContext();
        String name = mProfName.getText();
        return TextUtils.isEmpty(name) ? false : true;

    }
}
