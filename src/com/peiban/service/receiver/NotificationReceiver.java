package com.peiban.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * 功能： 消息通知。 <br />
 * 日期：2013-4-23<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public final class NotificationReceiver extends BroadcastReceiver {
	 // 普通消息
    public static final String ACTION_SHOW_NOTIFICATION = "cn.fengso.liaos.SHOW_NOTIFICATION";
    // 系统消息
    public static final String ACTION_NOTIFICATION_SYSTEM = "cn.fengso.liaos.NOTIFICATION_CLICKED";

    public static final String ACTION_NOTIFICATION_CLEARED = "cn.fengso.liaos.NOTIFICATION_CLEARED";
	@Override
	public void onReceive(Context context, Intent intent) {
		// String action = intent.getAction();
		// Log.d(LOGTAG, "action=" + action);
		//
		// if (Constants.ACTION_SHOW_NOTIFICATION.equals(action)) {
		// String notificationId = intent
		// .getStringExtra(Constants.NOTIFICATION_ID);
		// String notificationApiKey = intent
		// .getStringExtra(Constants.NOTIFICATION_API_KEY);
		// String notificationTitle = intent
		// .getStringExtra(Constants.NOTIFICATION_TITLE);
		// String notificationMessage = intent
		// .getStringExtra(Constants.NOTIFICATION_MESSAGE);
		// String notificationUri = intent
		// .getStringExtra(Constants.NOTIFICATION_URI);
		// if(Constant.EARTHQUAKE.equals(notificationTitle.trim())){
		// CoreApp app = (CoreApp) context.getApplicationContext() ;
		// }
		// Log.d(LOGTAG, "notificationId=" + notificationId);
		// Log.d(LOGTAG, "notificationApiKey=" + notificationApiKey);
		// Log.d(LOGTAG, "notificationTitle=" + notificationTitle);
		// Log.d(LOGTAG, "notificationMessage=" + notificationMessage);
		// Log.d(LOGTAG, "notificationUri=" + notificationUri);
		//
		// Notifier notifier = new Notifier(context);
		// notifier.notify(notificationId, notificationApiKey,
		// notificationTitle, notificationMessage, notificationUri);
		// }
	}

}
