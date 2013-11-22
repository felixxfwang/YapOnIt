package com.yaponit.utils;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class AudioRecorder {
	
	MediaRecorder mRecorder = null;
	File sdcardPath = null;
	private static File recordFile = null;
	public static void setRecordFile(File file){
		deleteRecordFile();
		recordFile = file;
	}
	public static File getRecordFile(){
		return recordFile;
	}
	
	public static boolean exitRecordFile(){
		return recordFile != null;
	}
	
	public static void deleteRecordFile(){
		if(recordFile != null){
			if(recordFile.exists()){
				recordFile.delete();
				recordFile = null;
			}
		}
	}
	
	public boolean startRecording() {
		if (!existSd())
			return false;
		try {
			File path = new File(sdcardPath.getPath() + File.separator
					+ "YapOnIt");
			if (!path.exists()) {
				path.mkdirs();
			}
			recordFile = File.createTempFile("audio_record", ".mp3", path);

			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			mRecorder.setOutputFile(recordFile.getAbsolutePath());

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e("media recorder", "prepare() failed");
		}
		mRecorder.start();
		return true;
	}

	public void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}
	
	private boolean existSd() {
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			sdcardPath = Environment.getExternalStorageDirectory();
			return true;
		}
		return false;
	}
}
