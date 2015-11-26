package com.peiban.app;

import net.tsz.afinal.FinalDb;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.peiban.R;
import com.peiban.app.ui.IndexTabActivity;
import com.peiban.app.ui.MainActivity;
import com.peiban.service.SnsService;
import com.peiban.vo.CustomerVo;
import com.peiban.vo.MessageInfo;
import com.peiban.vo.MessageType;
import com.peiban.vo.SNSMessage;
import com.peiban.vo.UserInfoVo;

public class ChatMessageNotifiy extends AbstractNotifiy{
	private static final String LOGTAG = "msgNotifiy";
	public ChatMessageNotifiy(SnsService context) {
		super(context);
	}

	@Override
	public void notifiy(SNSMessage message) {
		Log.d(LOGTAG, "notify()...");
		MessageInfo messageInfo = null;
		if(message instanceof MessageInfo){
			messageInfo = (MessageInfo) message;
		}else{
			return;
		}
		UserInfoVo userInfo = getService().getUserInfoVo();
		String fuid = messageInfo.getFromId();
		FinalDb finalDb = FinalFactory.createFinalDb(getService(), userInfo);
		CustomerVo customerVo = finalDb.findById(fuid, CustomerVo.class);
		if(customerVo == null){
			return;
		}
		
		String msg = null;
			
			switch (messageInfo.getType()) {
			case MessageType.PICTURE:
				msg = customerVo.getName() + " <发来图片>";
				break;
			case MessageType.TEXT:
				msg = customerVo.getName() + " : " +  messageInfo.getContent();
				break;
			case MessageType.VOICE:
				msg = customerVo.getName() + " <发来语音>";
				break;
			case MessageType.MAP:
				msg = customerVo.getName() + " <发来位置信息>";
				break;

			default:
				break;
			}
			
			// Notification
			Notification notification = new Notification();
			notification.icon = R.drawable.logo; // 设置通知的图标
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notification.defaults |= Notification.DEFAULT_LIGHTS;
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
//  音频将被重复直到通知取消或通知窗口打开。
//			notification.flags |= Notification.FLAG_INSISTENT;
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
			notification.when = System.currentTimeMillis();
			
			 Intent intent = new Intent(getService(), IndexTabActivity.class);
			 intent.putExtra("tag", "chat");
//			 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			 intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			 intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//			 intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//
			 PendingIntent contentIntent = PendingIntent.getActivity(getService(), 0,
			 intent, PendingIntent.FLAG_UPDATE_CURRENT);
			//
			 notification.setLatestEventInfo(getService(), "你有新的聊天信息!", msg,
			 contentIntent);
			 getNotificationManager().notify(messageInfo.getFromId().hashCode(), notification);
	}

}
