package com.yaponit.utils;

import java.io.File;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public abstract class Player {

	protected MediaPlayer mPlayer;
	public Player(){
		mPlayer = new MediaPlayer();
	}

	public abstract boolean playRecordFile(File fileToPlay);
	
	public abstract void stopPlayRecord(); 
	
	public void setOnCompletionListener(OnCompletionListener listener){
		mPlayer.setOnCompletionListener(listener);
	}
}
