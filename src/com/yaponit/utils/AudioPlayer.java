package com.yaponit.utils;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;

public class AudioPlayer extends Player{
	
	

	public boolean playRecordFile(File fileToPlay) {
		if (fileToPlay == null) {
			return false;
		}
		if (mPlayer == null) {
			mPlayer = new MediaPlayer();
		}
		try {
			
			mPlayer.setDataSource(fileToPlay.getAbsolutePath());
			mPlayer.prepare();
			mPlayer.start();
			return true;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}	
	
	public void stopPlayRecord() {
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.reset();
		}
	}
}
