package com.peiban.service.receiver;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.peiban.app.ChatMessageNotifiy;
import com.peiban.app.FinalFactory;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.UserInfoApi;
import com.peiban.service.SessionListUtil;
import com.peiban.service.XmppManager;
import com.peiban.vo.CustomerVo;
import com.peiban.vo.MessageInfo;
import com.peiban.vo.MessageType;
import com.peiban.vo.SessionList;
import com.peiban.vo.UserInfoVo;

/**
 * 
 * 功能： 接收到发送的消息.通过广播发送出去 <br />
 * 日期：2013-5-6<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class NotifyChatMessage implements NotifyMessage {
	private static final String TAG = "NotifyChatMessage";

	/**
	 * 聊天服务发来聊天信息, 广播包<br/>
	 * 附加参数: {@link NotifyChatMessage#EXTRAS_NOTIFY_CHAT_MESSAGE}
	 */
	public static final String ACTION_NOTIFY_CHAT_MESSAGE = "com.sns.notify.ACTION_NOTIFY_CHAT_MESSAGE";
	/**
	 * 某消息列表有更新，注意查收
	 * 附加参数: {@link NotifyChatMessage#EXTRAS_NOTIFY_SESSION_MESSAGE}
	 */
	public static final String ACTION_NOTIFY_SESSION_MESSAGE = "com.sns.notify.ACTION_NOTIFY_SESSION_MESSAGE";
	
	/**
	 * 附加信息<br/> {@link MessageInfo}
	 */
	public static final String EXTRAS_NOTIFY_CHAT_MESSAGE = "extras_message";
	/**
	 * 附加信息<br/> {@link SessionList}
	 */
	public static final String EXTRAS_NOTIFY_SESSION_MESSAGE = "extras_session";
	
	

	private ChatMessageNotifiy chatMessageNotifiy;
	public XmppManager xmppManager;
	public UserInfoVo userInfoVo;
	

	public NotifyChatMessage(XmppManager xmppManager) {
		super();
		this.xmppManager = xmppManager;
		this.userInfoVo = xmppManager.getSnsService().getUserInfoVo();
		chatMessageNotifiy = new ChatMessageNotifiy(xmppManager.getSnsService());
	}

	@Override
	public void notifyMessage(String msg) {
		Log.d(TAG, "notifyMessage()");
		try {
			JSONObject json = JSON.parseObject(msg);
			MessageInfo info = JSON.toJavaObject(json, MessageInfo.class);
			if(MessageType.MAP == info.getType()){
				info.setContent(json.getString("content"));
			}
			if (info != null) {
				saveMessageInfo(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveMessageInfo(MessageInfo info) {
		FinalDb db = FinalFactory.createFinalDb(xmppManager.getSnsService(),
				this.userInfoVo);
		// 1. 获取本地会话列表.
		SessionList sessionList = SessionListUtil.toDbMessage(
				xmppManager.getSnsService(), info, userInfoVo);
		
		String lastContent = "";
		switch (info.getType()) {
		case MessageType.PICTURE :
			lastContent = "图片";
			break;
		case MessageType.VOICE :
			lastContent = "声音";
			info.setSendState(4);
			break;
		case MessageType.TEXT :
			lastContent = info.getContent();
			break;
		case MessageType.MAP :
			lastContent = "位置信息";
			break;

		default:
			break;
		}
		
		saveInfo(sessionList, info, lastContent, db);
	}
	
	private void saveInfo(SessionList sessionList, MessageInfo info, String lastContent, FinalDb db){
		Log.d(TAG, "saveInfo()");
		info.setPullTime(System.currentTimeMillis() + "");  // 得到消息的时间.
		String fuid = info.getFromId();  // 得到对方的id.
		if (sessionList == null) {
			sessionList = new SessionList();
			sessionList.setCreateTime(System.currentTimeMillis());
			sessionList
					.setFuid(fuid);
			sessionList.setNotReadNum(sessionList.getNotReadNum() + 1);
			sessionList.setUpdateTime(System.currentTimeMillis());
			sessionList.setLastContent(lastContent);
			db.saveBindId(sessionList);
		} else {
			sessionList.setNotReadNum(sessionList.getNotReadNum() + 1);
			sessionList.setUpdateTime(System.currentTimeMillis());
			sessionList.setLastContent(lastContent);
			db.update(sessionList);
		}

		info.setSessionId(sessionList.getId());
		db.saveBindId(info);  // 保存聊天信息。
		
		
		if(!checkLocalFuidInfo(db, fuid)){
			if(downFuidInfo(fuid, db)){
				Log.d(TAG, "downSuccess()");
			}
		}
		sendBroad(sessionList, info);
	}
	
	// 检查本来是否有Fuid的用户信息
	private boolean checkLocalFuidInfo(FinalDb db, String fuid){
		CustomerVo customerVo = db.findById(fuid, CustomerVo.class);
		if(customerVo == null){
			return false;
		}else{
			return true;
		}
	}
	
	// 下载用户信息.
	private boolean downFuidInfo(String fuid, FinalDb db){
		Log.d(TAG, "downFuidInfo()");
		String result = new UserInfoApi().getInfo(userInfoVo.getUid(), fuid);
		
		String data = ErrorCode.getData(result);
		if(data != null){
			try {
				CustomerVo customerVo = JSON.toJavaObject(JSON.parseObject(data), CustomerVo.class);
				if(customerVo != null){
					try {
						// 标记为陌生人.
						customerVo.setFriend("0");
						db.save(customerVo);
						return true;
					} catch (Exception e) {
						Log.d(TAG, "downFuidInfo()", e);
						return false;
					}
					
				}else{
					return false;
				}
			} catch (Exception e) {
				Log.d(TAG, "downFuidInfo()", e);
				return false;
			}
		}else{
			return false;
		}
	}
	
	private void sendBroad(SessionList sessionList, MessageInfo info) {
		Log.d(TAG, "sendBroad()");
		Intent intent = new Intent(ACTION_NOTIFY_CHAT_MESSAGE);
		intent.putExtra(EXTRAS_NOTIFY_CHAT_MESSAGE, info);
		intent.putExtra(EXTRAS_NOTIFY_SESSION_MESSAGE, sessionList);
		chatMessageNotifiy.notifiy(info);
		if (xmppManager != null && xmppManager.getSnsService() != null) {
			xmppManager.getSnsService().sendBroadcast(intent);
		}
	}
}
