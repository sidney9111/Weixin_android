package com.peiban.app.action;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;

public class AudioRecorder {
	private static int SAMPLE_RATE_IN_HZ = 8000;

	private MediaRecorder recorder;
	final String path;

	public AudioRecorder(String path) {
		this.path = path;
	}

	public void start() throws IOException {
		recorder = new MediaRecorder();
		String state = android.os.Environment.getExternalStorageState();

		if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
			throw new IOException("SD Card is not mounted,It is  " + state
					+ ".");
		}
		File directory = new File(path).getParentFile();

		if (!directory.exists() && !directory.mkdirs()) {
			throw new IOException("Path to file could not be created");
		}

		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//		recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
//		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
		recorder.setOutputFile(path);
		recorder.prepare();
		recorder.start();
	}

	public void stop() throws IOException {
		recorder.stop();
		recorder.release();
		recorder = null;
	}

	public double getAmplitude() {
		if (recorder != null) {
			try {
				return (recorder.getMaxAmplitude());
			} catch (Exception e) {
				return 0;
			}
			
		} else{
			return 0;
		}
	}
}