package com.peiban.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;

import com.peiban.R;
import com.peiban.app.ui.IndexTabActivity;
import com.peiban.service.SnsService;
import com.peiban.vo.NotifiyType;
import com.peiban.vo.NotifiyVo;
import com.peiban.vo.SNSMessage;

public class SystemNotifiy extends AbstractNotifiy{
	public static final int NOTION_ID = 10023;
	public SystemNotifiy(SnsService context) {
		super(context);
	}

	@Override
	public void notifiy(SNSMessage message) {
		if(message instanceof NotifiyVo){
			NotifiyVo notifiyVo = (NotifiyVo) message;
			String msg = "";
			switch (notifiyVo.getType()) {
			case NotifiyType.APPLY_DATA:
				msg = "申请查看个人资料";
				break;
			case NotifiyType.SYSTEM_MSG:
				msg = "系统消息";
				break;
			case NotifiyType.BE_FRIEND:
				msg = "好友申请";
				break;
			case NotifiyType.COMMENTED:
				msg = "被评论";
				break;
			case NotifiyType.SEE_DATA:
				msg = "查看资料";
				break;
			case NotifiyType.DEL_FRIEND:
				msg = "被好友删除";
				break;
			case NotifiyType.ADDFRIENDED:
				msg = "添加好友成功!";
				break;
			case NotifiyType.RESFUEFRIENDED:
				msg = "好友请求被拒绝";
				break;

			default:
				break;
			}
			
			
			// Notification
			Notification notification = new Notification();
			notification.icon = R.drawable.logo; // 设置通知的图标
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
			notification.when = System.currentTimeMillis();
			
			 Intent intent = new Intent(getService(), IndexTabActivity.class);
			 intent.putExtra("tag", "notation");
//			 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			 intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			 intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//			 intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//
			 PendingIntent contentIntent = PendingIntent.getActivity(getService(), 0,
			 intent, PendingIntent.FLAG_UPDATE_CURRENT);
			//
			 notification.setLatestEventInfo(getService(), "有新通告!", msg,
			 contentIntent);
			 getNotificationManager().notify(NOTION_ID, notification);
		}
	}
}
