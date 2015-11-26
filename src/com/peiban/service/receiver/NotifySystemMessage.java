package com.peiban.service.receiver;

import net.tsz.afinal.FinalDb;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.peiban.app.FinalFactory;
import com.peiban.app.SystemNotifiy;
import com.peiban.service.XmppManager;
import com.peiban.vo.CustomerVo;
import com.peiban.vo.NotifiyType;
import com.peiban.vo.NotifiyVo;

public class NotifySystemMessage implements NotifyMessage{
	private static final String TAG = NotifySystemMessage.class.getCanonicalName();
	/**
	 * 聊天服务发送系统消息广播包<br/>
	 * 附加参数: {@link NotifySystemMessage#EXTRAS_NOTIFY_SYSTEM_MESSAGE}
	 * 附加参数: {@link NotifySystemMessage#EXTRAS_NOTIFY_SYSTEM_TAG}
	 */
	public static final String ACTION_NOTIFY_SYSTEM_MESSAGE = "com.sns.notify.ACTION_NOTIFY_SYSTEM_MESSAGE";
	
	/**
	 * 附加标识<br/> {@link NotifiyType}
	 */
	public static final String EXTRAS_NOTIFY_SYSTEM_TAG = "extra_tag";
	
	/**
	 * 附加信息<br/> {@link NotifiyVo}
	 */
	public static final String EXTRAS_NOTIFY_SYSTEM_MESSAGE = "extras_message";
	
	/** VIP 状态发生变化
	 * 附加参数: {@link NotifySystemMessage#EXTRAS_VIP}
	 *  */
	public static final String ACTION_VIP_STATE = "com.sns.notify.ACTION_VIP_STATE";
	
	/**
	 * {@link CustomerVo}
	 * */
	public static final String EXTRAS_VIP = "extra_vip";
	
	private XmppManager xmppManager;
	private FinalDb finalDb;
	private SystemNotifiy systemNotifiy;
	
	
	public NotifySystemMessage(XmppManager xmppManager) {
		super();
		this.xmppManager = xmppManager;
		this.systemNotifiy = new SystemNotifiy(xmppManager.getSnsService());
		try {
			finalDb = FinalFactory.createFinalDb(xmppManager.getSnsService(), xmppManager.getSnsService().getUserInfoVo());
		} catch (Exception e) {
			Log.e(TAG, "NotifySystemMessage()", e);
		}
	}


	@Override
	public void notifyMessage(String msg) {
		Log.d(TAG, "notitySystemMessage()："+msg);
		try {
			JSONObject object = JSON.parseObject(msg);
			NotifiyVo notifiyVo = JSON.toJavaObject(object, NotifiyVo.class);
			notifiyVo.setSent(object.getString("sent"));
			System.out.println(msg);
			// 如果是认证头像信息，更新用户信息
			if(NotifiyType.HEAD_AUTH_SUCCESS == notifiyVo.getType()){
				if(TextUtils.isEmpty(notifiyVo.getContent())){
					notifiyVo.setContent(NotifiyType.HEAD_AUTH_SUCCESS_TAG);
				}
				try {
					CustomerVo customerVo = finalDb.findById(xmppManager.getSnsService().getUserInfoVo().getUid(), CustomerVo.class);
					customerVo.setHeadattest("1");
					finalDb.update(customerVo);
					Log.d(TAG, "notitySystemMessage()： 更新成功");
				} catch (Exception e) {
					Log.d(TAG, "notitySystemMessage()： 更新失败", e);
				}
			}else if(NotifiyType.CHANGE_VIP_STATE == notifiyVo.getType()){
				try {
					Log.d(TAG, "notitySystemMessage()： " + notifiyVo.getSent());
					CustomerVo customerVo = JSONObject.toJavaObject(JSONObject.parseObject(notifiyVo.getSent()), CustomerVo.class);
					finalDb.update(customerVo);
					
					Intent intent = new Intent(ACTION_VIP_STATE);
					intent.putExtra(EXTRAS_VIP, customerVo);
					xmppManager.getSnsService().sendBroadcast(intent);
					Log.d(TAG, "notitySystemMessage()： 更新成功");
				} catch (Exception e) {
					Log.d(TAG, "notitySystemMessage()： 更新失败", e);
				}
				
				return;
			}
			FinalDb finalDb = FinalFactory.createFinalDb(xmppManager.getSnsService(), xmppManager.getSnsService().getUserInfoVo());
			finalDb.saveBindId(notifiyVo);
			
			
			Intent intent = new Intent(ACTION_NOTIFY_SYSTEM_MESSAGE);
			intent.putExtra(EXTRAS_NOTIFY_SYSTEM_MESSAGE, notifiyVo);
			xmppManager.getSnsService().sendBroadcast(intent);
			this.systemNotifiy.notifiy(notifiyVo);
		} catch (Exception e) {
			Log.e(TAG, "notityMessage()", e);
		}
	}
	
	/**
	 * 过滤系统消息.
	 * <br />
	 * 1.好友添加功能;<br/>
	 * 2.好友删除功能;<br/>
	 * @param notifiyVo
	 * 作者:fighter <br />
	 * 创建时间:2013-6-8<br />
	 * 修改时间:<br />
	 */
	private void filterNotifiy(NotifiyVo notifiyVo){
		int type = notifiyVo.getType();
		switch (type) {
		case NotifiyType.ADDFRIENDED:
			try {
				CustomerVo customerVo = JSON.toJavaObject(JSON.parseObject(notifiyVo.getSent()), CustomerVo.class);
				if(customerVo == null){
					return;
				}
				
				sendBroad(NotifiyType.ADDFRIENDED, customerVo);
			} catch (Exception e) {
				Log.e(TAG, "filterNotifiy():ADDFRIENDED", e);
			}
			break;
		case NotifiyType.DEL_FRIEND:
			try {
				CustomerVo customerVo = JSON.toJavaObject(JSON.parseObject(notifiyVo.getSent()), CustomerVo.class);
				if(customerVo == null){
					return;
				}
				finalDb.delete(customerVo);
				
				sendBroad(NotifiyType.ADDFRIENDED, customerVo);
			} catch (Exception e) {
				Log.e(TAG, "filterNotifiy():DEL_FRIEND", e);
			}
			break;

		default:
			break;
		}
	}
	
	private void sendBroad(int tag, CustomerVo customerVo){
		
		
		Intent intent = new Intent();
		intent.setAction(NotifySystemMessage.ACTION_NOTIFY_SYSTEM_MESSAGE);
		intent.putExtra(EXTRAS_NOTIFY_SYSTEM_TAG, tag);
		intent.putExtra(EXTRAS_NOTIFY_SYSTEM_MESSAGE, customerVo);
		try {
			xmppManager.getSnsService().sendBroadcast(intent);
		} catch (Exception e) {
			Log.e(TAG, "sendBroad()", e);
		}
	}
	
	
	class FriendManager{
		
		public void addFriend(CustomerVo customerVo){
			
		}
		
		public void delFriend(CustomerVo customerVo){
			
		}
	}

}
