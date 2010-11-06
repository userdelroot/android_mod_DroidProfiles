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


package fac.userdelroot.droidprofiles.pref;
//import fac.userdelroot.droidprofiles.R;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;


/**
 * 
 * Provides a multiple select checkbox that extends {@link ListPreference}
 */
public class ListPrefMulti extends ListPreference {

	protected static final String TAG = "ListPrefMulti";
	private CharSequence[] mEntries;
	private CharSequence[] mEntryValues;
	private boolean[] mCheckedValues;
//	private ArrayList mCheckedList = null;

	public ListPrefMulti(Context context, AttributeSet attrs) {
		super(context, attrs);
		//mCheckedList = new ArrayList();
		// mEntries = getEntries();
		// mEntryValues = getEntryValues();
		// mSelEntryValue = new LinkedHashMap();
	}

	public ListPrefMulti(Context context) {
		super(context, null);
		//mCheckedList = new ArrayList();
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		// Should the DialagPreference super be called?
		// super.onPrepareDialogBuilder(builder);

		if (mEntries == null || mEntryValues == null) {
			throw new IllegalStateException(
					"ListPrefMulti requires an entries array and an entryValues array.");
		}

		// mClickedDialogEntryIndex = getValueIndex();
		builder.setMultiChoiceItems(mEntries, mCheckedValues,
				new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {

						// TODO:
						// This may be an issue later but for now this is how it
						// is working...

						// mCheckValues is updated depending on if we are
						// checked or not
						// we just retried mCheckValues and hopefully the order
						// doesn't change :(
						// probably should do this a different way to check and
						// make sure we are matched with
						// the correct thing we want checked.
						// but for now this works ;)

						// if (isChecked) {
						// mCheckedList.add(which);
						// } else {
						// if (mCheckedList.contains(which)) {
						// mCheckedList.remove(which);
						// }
						// }
						//if (Log.LOGV)
						//	Log.v(TAG + "Check box clicked" + isChecked
							//		+ " which " + mEntryValues[which]);

					}
				}).setPositiveButton(fac.userdelroot.droidprofiles.R.string.menu_save,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// see above
						// buildCheckedList();

						dialog.dismiss();

					}
				}).setNegativeButton(fac.userdelroot.droidprofiles.R.string.menu_cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//mCheckedList = null;
						dialog.cancel();
					}
				});

	}

	public void buildCheckedList() {
		/*
		 * int i = mCheckedValues.length; ArrayList tmp = new ArrayList();
		 * 
		 * for (boolean val : mCheckedValues) { Log.v("what we got " + val); }
		 */

	}

	/**
	 * Get the CheckedItems list
	 * 
	 * @return boolean[]
	 */
	public boolean[] getCheckedItems() {
		return this.mCheckedValues;
	}

	/**
	 * 
	 * @deprecated this is not available at this time, yes it says deprecated
	 *             but i did not want it used we don't use it! Use this instead
	 *             {@link #getCheckedItems()}
	 */
//	@Deprecated
//	public ArrayList getCheckedList() {
//		return this.mCheckedList;
//	}

	public void setCheckedValues(boolean[] checkedValues) {
		this.mCheckedValues = checkedValues;
	}

	@Override
	public CharSequence[] getEntries() {
		return super.getEntries();
	}

	@Override
	public CharSequence[] getEntryValues() {
		return super.getEntryValues();
	}

	@Override
	public void setEntries(CharSequence[] entries) {
		this.mEntries = entries;
		super.setEntries(entries);
	}

	@Override
	public void setEntryValues(CharSequence[] entryValues) {
		this.mEntryValues = entryValues;
		super.setEntryValues(entryValues);
	}

}