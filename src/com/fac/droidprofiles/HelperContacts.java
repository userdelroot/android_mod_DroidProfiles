/**
 * 
 */
package com.fac.droidprofiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Helper class to store and retrieve contact information
 * ContactsPosition[contacts._ID][position_in_list_view]
 * ContactList[contact._ID][contact_name]
 *
 */
public class HelperContacts {

	private static final String TAG = "HelperContacts ";

	// Do not allow initialize 
	public HelperContacts() {
		
	}
	
	private static final ArrayList<Long> list = new ArrayList<Long>();
	@SuppressWarnings("unchecked")
	private static final HashMap map = new HashMap();
	private static boolean bHasChanged = false;
	
	static final ArrayList<Long> getList() {
		return list;
	}
		
	@SuppressWarnings("unchecked")
	static final HashMap getMap() {
		return map;
	}
	
	static final void clearAll() {
		map.clear();
	}
	
	@SuppressWarnings("unchecked")
	static final void addItemToMap(long id) {
		bHasChanged = true;
		map.put(id, "none");
	}
	static final void delItemFromMap(long id) {
		bHasChanged = true;
		map.remove(id);
	}
	
	static final void setList() {
		if (Log.LOGV) 
			Log.i(TAG + " setList map " + map.toString());
		
		list.clear();
		long key = -1;
		if (map.isEmpty()) { 
			return;
		}
		
		if (map.containsKey(key)) {
			if (Log.LOGV)
				Log.v(TAG + "no items were checked");
			
			return;
		}
		
		
		Collection<?> keys = map.keySet();
		for (Object id : keys) {
			list.add((Long) id);
		}
	}
	
	/**
	 * Has the contacts list changed
	 * @return
	 */
	static boolean hashChanged() {
		return bHasChanged;
	}
		
}