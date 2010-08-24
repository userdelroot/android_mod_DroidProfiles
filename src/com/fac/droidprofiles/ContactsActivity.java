/**
 * 
 */
package com.fac.droidprofiles;

import java.util.HashMap;

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

/**
 * Display the contacts with selectable checkboxes
 * 
 */
public class ContactsActivity extends ListActivity implements OnItemClickListener {

	private static final String TAG = "ContactsActivity ";
	private SimpleCursorAdapter adapter;
	private ListView listview;
//	private static final int MENU_SAVE = Menu.FIRST;
//	private static final int MENU_CANCEL = Menu.FIRST + 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loadContacts();

		

	}

	@SuppressWarnings("unchecked")
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

		Intent intent = getIntent();
		// get id

		long profileid = intent.getLongExtra(Profiles.PROFILE_ID, -1);
		Cursor c = null;
		if (profileid > 0) {
			// grab the contacts from the profiles database
			c = Profiles.getContacts(getContentResolver(), profileid);
		}
		
		if (c == null)
			return;

		HashMap map = HelperContacts.getMap();
		
		if (Log.LOGV)
			Log.v(TAG + "Map= " +map.toString() );

		
		if (map.isEmpty()) {
			if (c.moveToFirst()) {
				int index = c.getColumnIndex(Contacts.Columns.REAL_ID);
				while (c.isAfterLast() == false) {
					map.put(c.getLong(index), "none");
					c.moveToNext();
				}
			}
		}
		c.close();
		long key = -1; 
		if (map.containsKey(key)) {
			if (Log.LOGV)
				Log.v(TAG + " map reset due to no items checked ");
			map.clear();
		}
		
		if (Log.LOGV)
			Log.v(TAG +"New Map " +map.toString());

		if (cursor.moveToFirst()) {
			int index = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			int i = 0;
			while (cursor.isAfterLast() == false) {
				if (map.containsKey(cursor.getLong(index))) {
					listview.setItemChecked(i, true);
				}
				i++;
				cursor.moveToNext();
				
			}
		}
		
	}

	private final void setContacts() {
				
		
		HashMap map = HelperContacts.getMap();
		if (map != null && map.isEmpty())
			HelperContacts.addItemToMap(-1);
		
		HelperContacts.setList();

		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// we assume save
			setContacts();
			//return true;
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
			
			HelperContacts.addItemToMap(id);
			
			
		}
		else {
			if (Log.LOGV)
				Log.w(TAG + "deleting unchecked item @ pos " + position + "with id " + id);
			
			
			HelperContacts.delItemFromMap(id);
			
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

	
}
