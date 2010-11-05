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
import java.util.List;

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
    private Profile mProfile;
    private Notify pPhone;
    private Notify pSms;
    private Notify pMms;
    private Notify pEmail;
    
    private static final int CONTACTS_RESULT = 9;
    private static final int EMAIL_RESULT = 10;
    private static final int SMS_RESULT = 11;
    private static final int MMS_RESULT = 12;
    private static final int PHONE_RESULT = 13;
    
    
    private List<Integer> mContacts;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.add_profile);

        mContacts = null;
        
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
            Log.e(TAG + this.getClass().getSimpleName().toString()
                    + " something is borked!!! bail bail bail!!!!!!!");

            // bail bail bail
            finish();
            return;
        }
        // same as above
        if (mEmailNotify == null || mSmsNotify == null || mMmsNotify == null
                || mPhoneNotify == null) {
            Log.e(TAG + this.getClass().getSimpleName().toString()
                    + " something is borked!!! bail bail bail!!!!!!!");
            finish();
            return;
        }

        // set click listeners
        setListeners();

        // not needed
        // mListOverrides.setOnPreferenceClickListener(this);

        // initialize our profileID if there is one
        getProfileData();

        // set initial values here from database
        loadDefaultValues();

        // we want all of them
        getNotifyParcels();
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
     * get profile data.  if a valid profile id was sent in the intent
     * we will grab the profile. 
     * If not this is a new profile
     */
    private void getProfileData() {

        Intent intent = getIntent();
        // get id
        long id = intent.getLongExtra(Profiles.PROFILE_ID, -1);
        
        if (id > 0)
            mProfile = Profiles.getProfileByID(getContentResolver(), (int) id);
        else 
            mProfile = new Profile();
    }

    private void loadDefaultValues() {

        // Check if we have a valid profile or not
        if (mProfile != null) {

            // We are editing a profile

            // set the values from the database
            mProfName.setText(mProfile.name);
            mProfActive.setChecked(mProfile.active);
            mProfSilent.setChecked(mProfile.silent);
            mProfVibrate.setChecked(mProfile.vibrate);
            mProfRing.setChecked(mProfile.ringer);
            mProfRingVolume.setVolume((int) mProfile.ringvolume);

            if (mProfile.ringtone != null) {
                mProfRingtone.setRingtone(Uri.parse(mProfile.ringtone));
                mProfRingVolume.setRingtone(Uri.parse(mProfile.ringtone));
            }
            
            mProfOverride.setChecked(mProfile.override);
            mProfCustom.setChecked(mProfile.custom_notify);

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
        }
    }
    
    /**
     * getNotification thread runner to load our parcels
     * TODO: i really hate "Loading" screens so need to do checks to make sure we grabbed this info.
     * Maybe do this some place else?
     * @param notifyType
     */
    private void getNotifyParcels() {

        // this needs to be a valid ID 
        // If it is not we return because this is a new profile
        if (mProfile.id <= 0)
            return;

        // thread to get the data for the parcel. this can be done in the background and it keeps the interface snappy.
        // TODO: should probably add some checks to make sure this completed?
            Thread t = new Thread() {
                @Override
                public void run() {
                    pPhone = Profiles.getNotifyByProfileId(getContentResolver(), mProfile.id,
                            Notify.Columns.NOTIFY_TYPE_PHONE);
                    pSms = Profiles.getNotifyByProfileId(getContentResolver(), (int) mProfile.id,
                            Notify.Columns.NOTIFY_TYPE_SMS);
                    pMms = Profiles.getNotifyByProfileId(getContentResolver(), (int) mProfile.id,
                            Notify.Columns.NOTIFY_TYPE_MMS);
                    pEmail = Profiles.getNotifyByProfileId(getContentResolver(), (int) mProfile.id,
                            Notify.Columns.NOTIFY_TYPE_EMAIL);
                }
            };
            t.start();

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

            mProfile.name = newValue.toString();
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
        if (mProfRingtone.getRingtone() != null) {
            mProfile.ringtone =  mProfRingtone.getRingtone();
        }
        else {
            mProfile.ringtone = "";
        }
        // grab the volume
        mProfile.ringvolume = mProfRingVolume.getVolume();

        // check if this profile active.  If so, set others to not be.
        if (mProfile.active)
            Profiles.disableActiveProfiles(getContentResolver());
        
        // if this profile is valid we are just editing
        if (mProfile.id > 0) {
            Profiles.saveProfile(getContentResolver(), mProfile);
        } else {
            // we are creating new profile get profileID
            mProfile.id = Profiles.saveProfile(getContentResolver(), mProfile);
        }

        // add the contacts
        addContacts();

        addNotifies();
    }
    
    /**
     * adds Contacts to the profile
     */
    private void addContacts() {

        if (mContacts == null)
            return;

        // contacts
        // TODO:
        // _ID profile_id real_contact_id
        // so delete old data and add new data
        // these methods run quite fast so I assume that should not be an issue
        // on any android enabled device.
        // until I find a better solution this works
        // NOTE: this may change to per a notification basis in future releases

        // first delete all profile contacts match real_id
        // we only allow 1 contact per a profile

        // if there are no contacts, delete contacts if present.
        // TODO: add something here to determine if they were present or not
        if (mContacts.isEmpty()) {
            Profiles.deleteContactsbyProfileId(getContentResolver(), mProfile.id);
            return;
        }

        // first delete all contacts with real_id
        // we do not allow multiple contacts
        // fixme: this is kind of lame, we need a query profile_id AND real_id =
        // that would eliminate this
        // and would also fix it in the ProfilesHelper.class

        // delete all contacts associated with this profile
        Profiles.deleteContactsbyProfileId(getContentResolver(), mProfile.id);

        // delete all contacts associated with other profiles
        for (int real_id : mContacts) {
            Profiles.delContactsByRealId(getContentResolver(), real_id);

        }

        // add all contacts to this profile
        for (int id : mContacts) {
            Profiles.saveContacts(getContentResolver(), mProfile.id, id);
        }
    }

    /**
     * AddNotify types
     */
    private void addNotifies() {

        // these could be null so we check in Profiles.insertNotifies() 
        Profiles.saveNotifies(getContentResolver(), pEmail, Notify.Columns.NOTIFY_TYPE_EMAIL, mProfile.id);
        Profiles.saveNotifies(getContentResolver(), pSms, Notify.Columns.NOTIFY_TYPE_SMS, mProfile.id);
        Profiles.saveNotifies(getContentResolver(), pMms, Notify.Columns.NOTIFY_TYPE_MMS, mProfile.id);
        Profiles.saveNotifies(getContentResolver(), pPhone, Notify.Columns.NOTIFY_TYPE_PHONE, mProfile.id);
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

            mProfile.active = mProfActive.isChecked();
            mProfActive.setSummary(mProfActive.isChecked() ? "yes" : "no");

            return true;
        }

        if (pref == mProfSilent) {
            mProfile.silent = mProfSilent.isChecked();

            mProfSilent.setSummary(mProfSilent.isChecked() ? "yes" : "no");
            return true;
        }

        if (pref == mProfVibrate) {
            mProfile.vibrate = mProfVibrate.isChecked();
            mProfVibrate.setSummary(mProfVibrate.isChecked() ? "yes" : "no");
            return true;
        }

        if (pref == mProfRing) {
            mProfile.ringer = mProfRing.isChecked();
            mProfRing.setSummary(mProfRing.isChecked() ? "yes" : "no");
            return true;
        }
        if (pref == mProfOverride) {
            mProfile.override = mProfOverride.isChecked();
            mProfOverride.setSummary(mProfOverride.isChecked() ? "yes" : "no");
            return true;
        }

        if (pref == mProfRingVolume) {
            mProfRingVolume.setVolume(mProfile.ringvolume);
            return true;
        }

        if (pref == mProfCustom) {
            mProfile.custom_notify = mProfCustom.isChecked();
            mProfCustom.setSummary(mProfCustom.isChecked() ? "yes" : "no");
            return true;
        }

        if (pref == mContactScreen) {
            Intent intent = new Intent(this, ContactsActivity.class);
            intent.putExtra("fac.userdelroot.droidprofiles.ContactsActivity", mProfile.id);
            startActivityForResult(intent,CONTACTS_RESULT);
            return true;
        }

        if (pref == mEmailNotify) {

            // if this is null, this is either a new profile or profile has no
            // notifications
            if (pEmail == null)
                pEmail = new Notify();

            Intent intent = new Intent(this, EmailPreferences.class);
            intent.putExtra("fac.userdelroot.droidprofiles.Notify", pEmail);
            startActivityForResult(intent,EMAIL_RESULT);
            return true;
        }
        if (pref == mSmsNotify) {

            // if this is null, this is either a new profile or profile has no
            // notifications
            if (pSms == null)
                pSms = new Notify();

            Intent intent = new Intent(this, SMSPreferences.class);
            intent.putExtra("fac.userdelroot.droidprofiles.Notify", pSms);
            startActivityForResult(intent,SMS_RESULT);

            return true;
        }
        if (pref == mMmsNotify) {

            // if this is null, this is either a new profile or profile has no
            // notifications
            if (pMms == null)
                pMms = new Notify();

            Intent intent = new Intent(this, MMSPreferences.class);
            intent.putExtra("fac.userdelroot.droidprofiles.Notify", pMms);
            startActivityForResult(intent, MMS_RESULT);

            return true;
        }
        if (pref == mPhoneNotify) {

            // if this is null, this is either a new profile or profile has no
            // notifications
            if (pPhone == null)
                pPhone = new Notify();

            Intent intent = new Intent(this, PhonePreferences.class);
            intent.putExtra("fac.userdelroot.droidprofiles.Notify", pPhone);
            startActivityForResult(intent, PHONE_RESULT);
            return true;
        }

        return false;
    }


    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACTS_RESULT:
                    mContacts = data.getIntegerArrayListExtra("fac.userdelroot.droidprofiles.ContactsActivity");
                    Log.i(TAG + "onActivityResult contacts " + mContacts.toString());
                    break;
                case EMAIL_RESULT:
                    pEmail = data.getParcelableExtra("fac.userdelroot.droidprofiles.Notify");
                    break;
                case SMS_RESULT:
                    pSms = data.getParcelableExtra("fac.userdelroot.droidprofiles.Notify");
                    break;
                case MMS_RESULT:
                    pMms = data.getParcelableExtra("fac.userdelroot.droidprofiles.Notify");
                    break;
                case PHONE_RESULT:
                    pPhone = data.getParcelableExtra("fac.userdelroot.droidprofiles.Notify");
                    break;

            }
        }
        
     //   if (Log.LOGV)
     //       Log.v(TAG + " onActivityResult " + data.getParcelableExtra("fac.userdelroot.droidprofiles.Notify").toString());
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
