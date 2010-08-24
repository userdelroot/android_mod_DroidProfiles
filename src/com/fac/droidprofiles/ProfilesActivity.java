/**
 * 
 */
package com.fac.droidprofiles;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author root
 * 
 */
public class ProfilesActivity extends Activity implements OnItemClickListener {

	private static final int MENU_ACTIVE = Menu.FIRST;
	private static final int MENU_DEACTIVE = Menu.FIRST + 1;
	private ListView mList;
	private SimpleCursorAdapter adapter;
	private long mProfileId;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		mProfileId = id;
		AddorEditProfile();
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.profiles_activity);

		mList = (ListView) findViewById(R.id.profile_list);
		LinearLayout profileAdd = (LinearLayout) findViewById(R.id.profile_add);
		profileAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AddorEditProfile();
			}

		});

		// we set this negative
		// in case we are starting a new profile in AddProfile
		// if we are editing a profile this will be set to the correct
		// _id
		mProfileId = -1;
	}

	protected void AddorEditProfile() {
		Intent intent = new Intent(this, AddProfile.class);
		intent.putExtra(Profiles.PROFILE_ID, mProfileId);
		startActivity(intent);
		// Set the profile id to no row
		mProfileId = -1;

	}

	/**
	 * TODO: move this into {@link Profiles} then just return the cursor
	 */
	private final void PopulateProfileList() {

		Uri uri = Profile.Columns.CONTENT_URI;
		Cursor cursor;
		String projection[] = { Profile.Columns.NAME, Profile.Columns._ID };
		String sortOrder = Profile.Columns.NAME + " COLLATE LOCALIZED ASC";
		cursor = (Cursor) managedQuery(uri, projection, null, null, sortOrder);

		int to[] = { R.id.profile_list_id, R.id.profile_list_name };
		adapter = new SimpleCursorAdapter(this, R.layout.profiles_list, cursor,
				projection, to);

		this.mList.setAdapter(adapter);

		// set the lsitener
		mList.setOnItemClickListener(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		adapter = null;
	}

	@Override
	protected void onStart() {
		super.onStart();

		PopulateProfileList();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// do i need to do this ?
		adapter = null;
		mList = null;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ACTIVE, 0, "Enabled").setIcon(
				android.R.drawable.ic_menu_more);
		menu.add(0, MENU_DEACTIVE, 0, "Disabled").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case MENU_ACTIVE:
			serviceOn(true);
			return true;
		case MENU_DEACTIVE:
			serviceOn(false);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void serviceOn(boolean b) {
		
		if (b) { 
            startService(new Intent(this, ProfileService.class));
            return;
		}
        stopService(new Intent(this, ProfileService.class));
	}

	
	
}
