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

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Display the contacts with selectable checkboxes
 * 
 */
public class ContactsActivity extends ListActivity implements OnItemClickListener {

	private static final String TAG = "ContactsActivity ";
	private SimpleCursorAdapter adapter;
	private ListView listview;
	private ArrayList<Integer> mContacts;
//	private static final int MENU_SAVE = Menu.FIRST;
//	private static final int MENU_CANCEL = Menu.FIRST + 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContacts = new ArrayList<Integer>();
		loadContacts();

		

	}

	private void loadContacts() {
		
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME };
		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + "= 1";
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";
		Cursor cursor = managedQuery(uri, projection, selection, null,
				sortOrder);

		int to[] = { R.id.contact_name, R.id.contact_id };
		adapter = new SimpleCursorAdapter(this, R.layout.contacts_activity,
				cursor, projection, to);

		setListAdapter(adapter);

		listview = getListView();
		listview.setItemsCanFocus(false);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listview.setOnItemClickListener(this);

	       Bundle b = getIntent().getExtras();
	      int profileid = b.getInt("fac.userdelroot.droidprofiles.ContactsActivity");
		// get id

		Cursor c = null;
		if (profileid > 0) {
			// grab the contacts from the profiles database
			c = Profiles.getContacts(getContentResolver(), profileid);
		}
		
		if (c == null)
			return;
		
		// used for checking if contact is present.
        HashMap<Integer, String> map = new HashMap<Integer, String>();
		
        if (c.moveToFirst()) {
            int index = c.getColumnIndex(Contacts.Columns.REAL_ID);
            while (c.isAfterLast() == false) {
                mContacts.add(c.getInt(index));
                map.put(c.getInt(index), "none");
                c.moveToNext();
            }
        }
        c.close();

        if (mContacts.isEmpty() || map == null)
            return;

 // if the contact exists in the profile. If so check it.
        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            int i = 0;
            while (cursor.isAfterLast() == false) {

                if (map.containsKey(cursor.getInt(index))) {
                    listview.setItemChecked(i, true);
                }
                i++;
                cursor.moveToNext();

            }
        }

        map.clear();

    }

	private final void setContacts() {

        Intent retIntent = new Intent();
        retIntent.putIntegerArrayListExtra("fac.userdelroot.droidprofiles.ContactsActivity", mContacts);
        setResult(RESULT_OK, retIntent);
	    finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// we assume save
			setContacts();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		if (id <= 0) return;
		
		if (listview.isItemChecked(position)) {

			if (Log.LOGV)
				Log.w(TAG + "adding checked item @ pos " + position + "with id " + id);
			addContact(id);
			    
			
		}
		else {
			if (Log.LOGV)
				Log.w(TAG + "deleting unchecked item @ pos " + position + "with id " + id);
			
			deleteContact(id);
			
		}
		

		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		listview.setOnItemClickListener(null);
		super.onDestroy();
	}


	private void addContact(long id) {
	    
	    mContacts.add((int)id);
	}
	
	private void deleteContact(long id) {
	    Object obj = (int) id;
	    mContacts.remove(obj);
	    obj = null;
	}
}
