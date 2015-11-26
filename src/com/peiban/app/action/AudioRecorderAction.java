 package com.peiban.app.action;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 
 * 功能：录音<br />
 *   通过 使用SDK中AudioRecorder调用硬件进行录音<br/>
 *   通过录音可以返回音频的播率{@link #getAmplitude()}<br/>
 *   通过添加监听{@link #recorderListener} 对录音过程进行时时监听,通过接口时时反应!<br/>
 * 日期：2013-1-22<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class AudioRecorderAction extends BaseAction {
	static final String TAG = ("AudioRecorderAction");
	private static final int MIN_TIME = 1; // 录音的下线

	public static final int RECOREDER_ON = 0; // 准备
	public static final int RECOREDER_ING = 1; // 正在进行
	public static final int RECOREDER_END = 2; // 结束

	public static int recordStart = RECOREDER_ON;
	
	private float recordTime = 0.0f; // 录制的时间
	private String recordPath;

	private AudioRecorder audioRecorder;
	private RecorderListener recorderListener;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (null == recorderListener) {
				return;
			}

			switch (msg.what) {
			case RECOREDER_ON:
				recorderListener.onStart();
				break;
			case RECOREDER_ING:
				recorderListener.recording((Double) msg.obj);
				break;
			case RECOREDER_END:
				recorderListener.stop(recordPath);
				recorderListener.recordTime(recordTime);
				break;

			default:
				break;
			}
		}

	};
	public AudioRecorderAction(Context context) {
		super(context);
	}

	/**
	 * 开始录制
	 * @param recordFile  音频录制存放的位置.
	 * @throws IOException  SD卡不能使用，不能建立文件时抛出异常!
	 * 作者:fighter <br />
	 * 创建时间:2013-5-7<br />
	 * 修改时间:<br />
	 */
	public void startRecord(String recordFile) throws IOException {
		startRecord(new File(recordFile));
	}
	
	/**
	 * 开始录制
	 * @param recordFile  音频录制存放的位置.
	 * @throws IOException  SD卡不能使用，不能建立文件时抛出异常!
	 * 作者:fighter <br />
	 * 创建时间:2013-5-7<br />
	 * 修改时间:<br />
	 */
	public void startRecord(File recordFile) throws IOException{
		handler.sendEmptyMessage(RECOREDER_ON);
		this.recordPath = recordFile.getPath();
		audioRecorder = new AudioRecorder(this.recordPath);
		try {
			audioRecorder.start();
			recordStart = RECOREDER_ING;
			new RecordThread().start();
		} catch (IOException e) {
			recordStart = RECOREDER_END;
			throw e;
		}
	}

	/**
	 * 停止录音
	 * 
	 * @throws IOException
	 *             作者:fighter <br />
	 *             创建时间:2013-1-30<br />
	 *             修改时间:<br />
	 */
	public void stopRecord() throws IOException {
		if (null == audioRecorder) {
			return;
		}

		recordStart = RECOREDER_END;
		try {
			audioRecorder.stop();
			if (MIN_TIME > recordTime) {
				delRecord();
				recordPath = null;
			}
			handler.sendEmptyMessage(recordStart);
		} catch (IOException e) {
			throw e;
		}

	}

	/*
	 * 删除录音文件
	 */
	public void delRecord() {
		if (null != recordPath) {
			File file = new File(recordPath);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	public void setRecorderListener(RecorderListener recorderListener) {
		this.recorderListener = recorderListener;
	}

	public float getRecordTime() {
		return recordTime;
	}
	
	public double getAmplitude() {		
		return audioRecorder.getAmplitude();
	}

	class RecordThread extends Thread {

		@Override
		public void run() {
			recordTime = 0.0f;
			while (RECOREDER_ING == recordStart) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendMessage(handler.obtainMessage(RECOREDER_ING,
						audioRecorder.getAmplitude()));
				recordTime += 0.2f;
			}
		}
	}

	public interface RecorderListener {
		/**
		 * 
		 * @param amplitude
		 *            说话的声倍 作者:fighter <br />
		 *            创建时间:2013-1-22<br />
		 *            修改时间:<br />
		 */
		public void recording(double amplitude);

		/**
		 * 
		 * @param path
		 *            录制的声音的路径 null 表示没有录制成功,说话时间少于 1 秒 作者:fighter <br />
		 *            创建时间:2013-1-23<br />
		 *            修改时间:<br />
		 */
		public void stop(String path);
		
		public void onStart();

		/**
		 * 录制的时间
		 * 
		 * @param recordTime
		 *            作者:fighter <br />
		 *            创建时间:2013-1-30<br />
		 *            修改时间:<br />
		 */
		public void recordTime(float recordTime);
	}

	public String getRecordPath() {
		return recordPath;
	}
}
