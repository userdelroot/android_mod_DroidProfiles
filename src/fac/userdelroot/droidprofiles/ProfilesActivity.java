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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 *  
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
		mList.setOnCreateContextMenuListener(this);
		
/*		mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               // delete this profile and everything associated with it
                Log.v("ID LONG PRESS " + id + " POSITION " + position);
                
                return true;
            }
        });
        */
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

	
	
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        final int id = (int) info.id;
        switch (item.getItemId()) {
            case R.id.set_profile_active:
                break;
                
            case R.id.edit_profile:
                break;
                
            case R.id.delete_profile:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.delete_confirm_dialog))
                        .setMessage(getString(R.string.delete_confirm))
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int w) {

                                        Log.i("Delete Profile");
                                    }
                                }).setNegativeButton(android.R.string.cancel, null).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }
	
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        // Inflate the menu from xml.
        getMenuInflater().inflate(R.menu.context_menu, menu);

    }

    private void serviceOn(boolean b) {
		
		if (b) { 
            startService(new Intent(this, ProfileService.class));
            return;
		}
        stopService(new Intent(this, ProfileService.class));
	}
	
}
