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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author userdelroot
 */
public class ListProfiles extends Activity implements OnItemClickListener {

    private static final String TAG = "ProfilesActivity ";
	private static final int MENU_ACTIVE = Menu.FIRST;
	private static final int MENU_DEACTIVE = Menu.FIRST + 1;
	private ListView mList;
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

		setContentView(R.layout.list_profiles);

		mList = (ListView) findViewById(R.id.profile_list);
		TextView profileAdd = (TextView) findViewById(R.id.profile_add);
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
		// Set the profile id to an invalid id
		mProfileId = -1;

	}

	/**
	 * TODO: move this into {@link Profiles} then just return the cursor
	 */
	private final void fillProfileList() {

		Uri uri = Profile.Columns.CONTENT_URI;
		String projection[] = { Profile.Columns.NAME, Profile.Columns._ID };
		String sortOrder = Profile.Columns.NAME + " COLLATE LOCALIZED ASC";
		Cursor cursor = managedQuery(uri, projection, null, null, sortOrder);
		int to[] = { R.id.profile_list_id, R.id.profile_list_name };
		SimpleCursorAdapter mSimpleAdapter = new SimpleCursorAdapter(this, R.layout.profiles_list, cursor,
				projection, to);
		mList.setAdapter(mSimpleAdapter);
		
		
		if (Log.LOGV)
		    Log.i(TAG + "fillProfileList() row count " + cursor.getCount() );
	}

	@Override
	protected void onPause() {
		super.onPause();
	      // set the lsitener(s)
        mList.setOnItemClickListener(null);
        mList.setOnCreateContextMenuListener(null);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// do i need to do this ?
		mList = null;
		
	}
	

    @Override
    protected void onResume() {
        super.onResume();
        
        // set the lsitener(s)
        mList.setOnItemClickListener(this);
        mList.setOnCreateContextMenuListener(this);
        fillProfileList();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ACTIVE, 0, "Enabled").setIcon(
				android.R.drawable.ic_menu_more);
		menu.add(0, MENU_DEACTIVE, 0, "Disabled").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	
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
        final long id = info.id;
        
        switch (item.getItemId()) {
            case R.id.set_profile_active:
                Profiles.setProfileActive(getContentResolver(), id, true);
                fillProfileList();

                return true;
            case R.id.edit_profile:
                    mProfileId = id;
                    AddorEditProfile();
                return true;
                
            case R.id.delete_profile:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.delete_confirm_dialog))
                        .setMessage(getString(R.string.delete_confirm))
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int w) {

                                       deleteProfile(id,info.position);
                                       
                                    }
                                }).setNegativeButton(android.R.string.cancel, null).show();
                return true;
                
           default:
              break;
        
        }
        
        return super.onContextItemSelected(item);
    }
	
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        // Inflate the menu from xml.
        
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    
    
    
    
    /**
     * Delete the profile TODO: maybe add a toast with the profile name that was
     * deleted?
     * 
     * @param id
     */
    private void deleteProfile(long id, int pos) {

        
        Profiles.deleteProfile(getContentResolver(), id);

        fillProfileList();
        Toast.makeText(this, R.string.profile_deleted, Toast.LENGTH_SHORT).show();
    }
    
    private void serviceOn(boolean b) {
		
		if (b) { 
         //   startService(new Intent(this, ProfileService.class));
            return;
		}
       // stopService(new Intent(this, ProfileService.class));
	}
	
}
