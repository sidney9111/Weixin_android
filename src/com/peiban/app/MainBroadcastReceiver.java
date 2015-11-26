package com.peiban.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;

import com.peiban.app.cache.UserInfoCache;
import com.peiban.app.ui.IndexTabActivity;
import com.peiban.service.receiver.NotifyChatMessage;
import com.peiban.service.receiver.NotifySystemMessage;
import com.peiban.vo.SessionList;
import com.peiban.vo.UserInfoVo;

public class MainBroadcastReceiver extends BroadcastReceiver{
	private String mUid;
	private Vibrator vibrator;
	private Handler mHandler;
	private UserInfoVo userInfoVo;
	
	public MainBroadcastReceiver(Context context, Handler handler){
		userInfoVo = new UserInfoCache(context).getCacheUserInfo();
		mUid = userInfoVo.getUid();
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		mHandler = handler;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(NotifyChatMessage.ACTION_NOTIFY_CHAT_MESSAGE.equals(action)){
			
//			SessionList sessionList = (SessionList) intent.getSerializableExtra(NotifyChatMessage.EXTRAS_NOTIFY_SESSION_MESSAGE);
//			if(sessionList != null){
//				SessionObserver.notifyObserver(sessionList);
//			}
			
			context.sendBroadcast(new Intent(IndexTabActivity.ACTION_REFRESH_SESSION));
//			try {
//				IndexTabActivity.getInstance().sessionPromptUpdate();
//			} catch (Exception e) {
//			}
		}else if(NotifySystemMessage.ACTION_NOTIFY_SYSTEM_MESSAGE.equals(intent.getAction())){

			context.sendBroadcast(new Intent(IndexTabActivity.ACTION_REFRESH_NOTIFIY));
			//			try {
//				IndexTabActivity.getInstance().notifyPromptUpdate();
//			} catch (Exception e) {
//			}
		}
	}
}
