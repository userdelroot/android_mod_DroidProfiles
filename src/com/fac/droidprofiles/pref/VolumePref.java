/**
 * Volume Preference Class
 */

package com.fac.droidprofiles.pref;

import com.fac.droidprofiles.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


/**
 * 
 * VolumePreference based off of the android SDK (internal)
 * This was sample given by the sdk but it is internal (not sure why they have this internal?)
 *
 *
 * TODO: change to increment for OnKey change seems to be smoother?
 */
public class VolumePref extends DialogPreference implements OnSeekBarChangeListener, Runnable {

	private Drawable mMyIcon;
	private SeekBar mSeekBar;
	private int iVolume;
	private AudioManager mAudioManager;
	private final int iStreamType = AudioManager.STREAM_RING;
	private Ringtone mRingtone;
	private Uri mRingtoneUri;
	private Handler mHandler = new Handler();
	private int iLastVolume;
	private int iOrigVolume;

	public VolumePref(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setDialogLayoutResource(R.layout.volumepref);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        
        // Steal the XML dialogIcon attribute's value
        mMyIcon = getDialogIcon();
        setDialogIcon(mMyIcon);
        
        // initialize
        iVolume = -1;
        iLastVolume = -1;
        iOrigVolume = -1;
        mRingtone = null;
        mRingtoneUri = null;
        
        initalizeAudio();
	}

	private void initalizeAudio() {
		Context context = getContext();
		 mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		 iOrigVolume = iVolume;
	}
	
	
	 @Override
	    protected void onDialogClosed(boolean positiveResult) {
	        super.onDialogClosed(positiveResult);

	        if (!positiveResult)
	        	iVolume = iOrigVolume;
	        else 
	        	iVolume = mSeekBar.getProgress();
	        
	        cleanup();
	    }

	    private void cleanup() {
	    	stop();
	}

		public void onActivityStop() {
	        cleanup();
	    }


	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		
		iOrigVolume = iVolume;
		mSeekBar = (SeekBar) view.findViewById(R.id.seekbar);
		mSeekBar.setMax(mAudioManager.getStreamMaxVolume(iStreamType));
		mSeekBar.setOnSeekBarChangeListener(this);
		mSeekBar.setProgress(iVolume);
		
		if (mRingtoneUri != null) {
			mRingtone = RingtoneManager.getRingtone(getContext(), mRingtoneUri);
		}
		else { // hopefully we can grab a default ringtone
			mRingtoneUri = android.provider.Settings.System.DEFAULT_RINGTONE_URI;
			mRingtone = RingtoneManager.getRingtone(getContext(), mRingtoneUri);
		}
		
		if (mRingtone != null)
			mRingtone.setStreamType(iStreamType);
			
	}
	
	protected static SeekBar getSeekBar(View dialogView) {
		return (SeekBar) dialogView.findViewById(R.id.seekbar);
	}	

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
	
		if (!fromUser)
			return;
		
		if (mRingtone != null && !mRingtone.isPlaying())
			sampleOn();

		postVolumeChange(progress);
	}

	private void postVolumeChange(int progress) {
		iLastVolume = progress;
		mHandler.removeCallbacks(null);
		mHandler.post(this);
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (mRingtone != null && !mRingtone.isPlaying()) {
			sampleOn();
		}
	}
	
	public void setVolume(int volume) {
		iVolume = volume;
	}
	
	public int getVolume() {
			return iVolume;
	}
	
	public void setRingtone(Uri ringtoneUri) {
		mRingtoneUri = ringtoneUri;
	}

	@Override
	public void run() {
		mAudioManager.setStreamVolume(iStreamType, iLastVolume, 0);
	}
	
	public void stop() {
		sampleOff();
		mSeekBar.setOnSeekBarChangeListener(null);
	}
	private void sampleOff() {
		if (mRingtone != null )
			mRingtone.stop();
		
	}

	public void sampleOn() {
		sampleOff();
		if (mRingtone != null && !mRingtone.isPlaying())
			mRingtone.play();
	}

}