package com.fac.droidprofiles;

/** 
 * Helper class for logging and debugging
 * @see android.util.Log
 */
final class Log {
	public final static String LOGTAG = "DroidProfiles";

	static final boolean LOGV = true;

	static final void v(String logMe) {
		android.util.Log.v(LOGTAG, logMe);
	}

	static final void e(String logMe) {
		android.util.Log.e(LOGTAG, logMe);
	}

	static final void e(String logMe, Exception ex) {
		android.util.Log.e(LOGTAG, logMe, ex);
	}

	static final void w(String logMe) {
		android.util.Log.w(LOGTAG, logMe);
	}
	
	static final void i(String logMe) {
		android.util.Log.i(LOGTAG, logMe);
	}
}
