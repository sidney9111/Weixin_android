package com.peiban.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.xmpp.push.sns.Roster;

/**
 * 
 * 功能： 用户管理,添加好友，删除好友等操作。 <br />
 * 日期：2013-5-6<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class SNSRosterManager {
	/**
	 * 删除好友请求<br />
	 * 附加参数<br/>
	 * KEY :{@link #EXTRAS_CHATID} <br/>
	 * <b>Value:</b> String  chatId
	 */
	public static final String ACTION_REQUEST_DELETE = "com.sns.roster.admin.ACTION_REQUEST_DELETE";
	/**
	 * 添加好友请求<br/>
	 * 附加参数<br/>
	 * KEY : {@link #EXTRAS_CHATID}
	 * <b>Value:</b> String  chatId
	 */
	public static final String ACTION_REQUEST_ADD = "com.sns.roster.admin.ACTION_REQUEST_ADD";
	
	/**
	 * 响应删除好友请求的结果
	 * <br/>
	 * 附加参数
	 * <br/>
	 * KEY : {@link #EXTRAS_CHATID}
	 * <b>Value:</b> String  chatId
	 * KEY : {@link #EXTRAS_RESULT}
	 * <b>Value:</b> boolean  state
	 */
	public static final String ACTION_RESULT_DELETE = "com.sns.roster.admin.ACTION_RESULT_DELETE";
	
	/**
	 * 响应添加好友请求的结果
	 * <br/>
	 * 附加参数
	 * <br/>
	 * KEY : {@link #EXTRAS_CHATID}
	 * <b>Value:</b> String  chatId
	 * KEY : {@link #EXTRAS_RESULT}
	 * <b>Value:</b> boolean  state
	 */
	public static final String ACTION_RESULT_ADD = "com.sns.roster.admin.ACTION_RESULT_ADD";
	
	/**
	 * 附加参数KEY<br/>
	 */
	public static final String EXTRAS_CHATID = "EXTRAS_CHATID";
	/**
	 * 请求执行后返回的结果KEY<br/>
	 */
	public static final String EXTRAS_RESULT = "EXTRAS_RESULT";
	
	private BroadcastReceiver broadcastReceiver;
	
	private XmppManager xmppManager;
	
	public SNSRosterManager(XmppManager xmppManager) {
		super();
		this.xmppManager = xmppManager;
		broadcastReceiver = new RosterManagerReceiver();
		init();
	}
	
	private void init(){
		registerReceiver();
	}
	
	public void delRoster(Intent intent){
		String chatId = intent.getStringExtra(EXTRAS_CHATID);
		boolean flag = false;
		if(!TextUtils.isEmpty(chatId)){
			flag = delRoster(chatId);
			
			Intent tent = new Intent();
			tent.setAction(ACTION_RESULT_DELETE);
			tent.putExtra(EXTRAS_CHATID, chatId);
			tent.putExtra(EXTRAS_RESULT, flag);
			
			sendBroadcast(tent);
		}
	}
	
	public void addRoster(Intent intent){
		String chatId = intent.getStringExtra(EXTRAS_CHATID);
		boolean flag = false;
		if(!TextUtils.isEmpty(chatId)){
			flag = addRoster(chatId);
			
			Intent tent = new Intent();
			tent.setAction(ACTION_RESULT_ADD);
			tent.putExtra(EXTRAS_CHATID, chatId);
			tent.putExtra(EXTRAS_RESULT, flag);
			
			sendBroadcast(tent);
		}
	}

	public boolean delRoster(String chatId){
		boolean flag = true;
		try {
			String jid = chatId2Jid(chatId);
			Roster roster = xmppManager.getConnection().getRoster();
			roster.removeEntry(roster.getEntry(jid));
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		
		return flag;
	}
	
	
	public boolean addRoster(String chatId){
		boolean flag = true;
		try {
			String jid = chatId2Jid(chatId);
			xmppManager.getConnection().getRoster().createEntry(jid, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		
		return flag;
	}
	
	private void registerReceiver(){
		try {
			IntentFilter filter = new IntentFilter();
			filter.addAction(ACTION_REQUEST_ADD);
			filter.addAction(ACTION_REQUEST_DELETE);
			filter.addAction(SnsService.ACTION_SERVICE_STOP);
			xmppManager.getSnsService().registerReceiver(broadcastReceiver, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void unregisterReceiver(){
		try{
			xmppManager.getSnsService().unregisterReceiver(broadcastReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendBroadcast(Intent intent){
		try {
			xmppManager.getSnsService().sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String chatId2Jid(String chatId) throws Exception{
		return chatId + "@" + xmppManager.getConnection().getServiceName();
	}
	
	
	class RosterManagerReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action != null){
				if(SnsService.ACTION_SERVICE_STOP.equals(action)){
					unregisterReceiver();
				}
				else if(ACTION_REQUEST_ADD.equals(action)){
					addRoster(intent);
				}
				else if(ACTION_REQUEST_DELETE.equals(action)){
					delRoster(intent);
				}
			}
		}
		
	}
}
