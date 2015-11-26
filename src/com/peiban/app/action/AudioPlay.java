package com.peiban.app.action;

import java.io.File;
import java.io.IOException;

import net.tsz.afinal.core.FileNameGenerator;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.widget.Toast;

import com.peiban.app.control.ReaderImpl;
/**
 * 
 * 功能：使用 MediaPlayer 播放音频<br />
 * 日期：2013-4-8<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class AudioPlay {
	private static final String TAG = "MYAUDIOPLAY";
	private MediaPlayer mMediaPlayer;
	private boolean state = false;
	private String currUrl = "";
	
	private File voicePath;
	
	private MyMediaListenerImpl mListenerImpl;
	private Context context;
	
	public AudioPlay(Context context) {
		super();
		voicePath = ReaderImpl.getAudioPath(context);
		mListenerImpl = new MyMediaListenerImpl();
		this.context = context;
	}

	private void init(){
		this.mMediaPlayer = new MediaPlayer();
		this.mMediaPlayer.setOnCompletionListener(mListenerImpl);
	}
	
	/**
	 * 
	 * @param context
	 * @param id res.raw....
	 * 作者:fighter <br />
	 * 创建时间:2013-4-8<br />
	 * 修改时间:<br />
	 */
	public void play(Context context, int id){
		mMediaPlayer = MediaPlayer.create(context, id);
		mMediaPlayer.setOnCompletionListener(mListenerImpl);
		try {
			mMediaPlayer.start();   // 开始播放.
		} catch (Exception e) {
			e.printStackTrace();
			mMediaPlayer.release();
		}
		
	}
	
	/**
	 * 播放文件名为URL地址的音频,在本地 Voice目录中进行查找，如果没有,也不进行下载.
	 * @param voiceUrl
	 * 作者:fighter <br />
	 * 创建时间:2013-4-8<br />
	 * 修改时间:<br />
	 */
	public void play(String voiceUrl){
		Log.d(TAG, "play()");
		if(currUrl.equals(voiceUrl) && state){
			stop();
			return;
		} else {
			stop();
		}
		init();
		currUrl = voiceUrl;
		String fileName = null;
		if(!currUrl.startsWith("AUDIO_")){
			fileName = FileNameGenerator.generator(voiceUrl);
		}else{
			fileName = currUrl;
		}
		File file = new File(voicePath, fileName);
		if(!file.exists()){
			Toast.makeText(context, "播放文件不存在!", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			mMediaPlayer.setDataSource(file.getAbsolutePath());
			mMediaPlayer.prepare(); // 初始化.
			mMediaPlayer.start();   // 开始播放.
			mMediaPlayer.setLooping(false);  // 不循环.
			onPlayStart();
			state = true;
		} catch (IllegalStateException e) {
			state = false;
			e.printStackTrace();
			mMediaPlayer.release();
		} catch (IOException e) {
			state = false;
			e.printStackTrace();
			mMediaPlayer.release();
		}
		
	}
	
	public void stop(){
		Log.d(TAG, "stop()");
		if(state && mMediaPlayer != null)
		{
			try {
				mMediaPlayer.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
			onPlayStop();
		}
		state = false;
	}
	
	public void onDestory(){
		stop();
	}
	protected void onPlayStart() {
		
	}
	protected void onPlayStop() {
		
	}
	
	class MyMediaListenerImpl implements OnCompletionListener{

		@Override
		public void onCompletion(MediaPlayer mp) {
			// 播放完成功后要释放资源
			mp.release();
			state = false;
			onPlayStop();
		}
		
	}
	
}
