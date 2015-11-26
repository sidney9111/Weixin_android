package com.peiban.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.peiban.service.receiver.NotifyChatMessage;
import com.peiban.service.receiver.NotifyMessage;
import com.peiban.service.receiver.NotifySystemMessage;
import com.peiban.service.receiver.PushChatMessage;
import com.peiban.service.receiver.PushMessage;
import com.peiban.vo.MessageInfo;
import com.peiban.vo.MessageType;
import com.xmpp.push.sns.Chat;
import com.xmpp.push.sns.ChatManagerListener;
import com.xmpp.push.sns.MessageListener;
import com.xmpp.push.sns.XMPPException;
import com.xmpp.push.sns.packet.Message;
/**
 * 
 * 功能：聊天监听.监听服务端信息(聊天信息，系统消息等...) <br />
 * 日期：2013-5-5<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class SNSMessageManager implements ChatManagerListener{
	private static final String SYSTEM_USER = "beautyas";
	
	private XmppManager xmppManager;
	private MessageListener chatListener;
	
//	private LruMemoryCache<String, Chat> chatCache = new LruMemoryCache<String, Chat>(6);
	
	private NotifyChatMessage chatMessage;
	private NotifySystemMessage systemMessage;
	
	private PushChatMessage pushChatMessage;
	
	public SNSMessageManager(XmppManager xmppManager) {
		super();
		this.xmppManager = xmppManager;
		chatListener = new ChatListenerImpl();
		chatMessage = new NotifyChatMessage(xmppManager);
		systemMessage = new NotifySystemMessage(xmppManager);
		pushChatMessage = new PushChatMessage(xmppManager);
	}

	@Override
	public void chatCreated(Chat chat, boolean createdLocally) {
		if(!createdLocally){
			chat.addMessageListener(chatListener);
		}
//		chatCache.put(chat.getParticipant().split("@")[0], chat);
	}
	
	/**
	 * 创建一个会话.
	 * @param chatID
	 * @return 没有连接状态时,返回空.
	 * 作者:fighter <br />
	 * 创建时间:2013-5-5<br />
	 * 修改时间:<br />
	 */
	public Chat createChat(String chatID){
		Chat chat = null;
//			chatCache.get(chatID);
//		if(chat == null){
			try {
				chat = xmppManager
				.getConnection()
				.getChatManager()
				.createChat(
						chatID
								+ "@"
								+ xmppManager.getConnection()
										.getServiceName(), chatListener);
			} catch (Exception e) {
				e.printStackTrace();
			}
//		}
		
//		if(chat != null){
//			chatCache.put(chatID, chat);
//		}
		
		return chat;
	}
	
	/**
	 * 给指定的某人发送消息
	 * 
	 * @param uid
	 * @param info
	 *            作者:fighter <br />
	 *            创建时间:2013-3-16<br />
	 *            修改时间:<br />
	 */
	public boolean sendMessage(MessageInfo info) {
		boolean flag = false;
		Chat chat = createChat(info.getToId());
		if(chat != null){
			try {
				JSONObject json = (JSONObject) JSON.toJSON(info);
				json.remove("id");
				json.remove("sendState");
				json.remove("readState");
				json.remove("sessionId");
				json.remove("pullTime");
				if(MessageType.MAP == info.getType()){
					json.put("content", JSONObject.parseObject(info.getContent()));
				}
				chat.sendMessage(json.toJSONString());
				flag = true;
			} catch (XMPPException e) {
				flag = false;
			} catch (IllegalStateException e) {
				// 没连接上服务器
				flag = false;
			}
		}
		info.setSendState(flag? 1 : 0);
		return flag;
	}
	
	/**
	 * 发送聊天信息
	 * @param pushMessage
	 * @param messageInfo
	 * 作者:fighter <br />
	 * 创建时间:2013-5-6<br />
	 * 修改时间:<br />
	 */
	public void pushMessage(PushMessage pushMessage, MessageInfo msg){
		pushMessage.pushMessage(msg);
	}
	
	/**
	 * 接收到消息,通过广播发送发送.
	 * @param notifyMessage
	 * @param content
	 * 作者:fighter <br />
	 * 创建时间:2013-5-6<br />
	 * 修改时间:<br />
	 */
	public void notityMessage(NotifyMessage notifyMessage, String content){
		notifyMessage.notifyMessage(content);
	}
	
	public NotifySystemMessage getSystemMessage() {
		return systemMessage;
	}

	public PushChatMessage getPushChatMessage() {
		return pushChatMessage;
	}

	/**
	 * 
	 * 功能：聊天对象的单对单对话监听<br />
	 * 日期：2013-5-5<br />
	 * 地点：淘高手网<br />
	 * 版本：ver 1.0<br />
	 * 
	 * @author fighter
	 * @since
	 */
	class ChatListenerImpl implements MessageListener{

		@Override
		public void processMessage(Chat chat, Message message) {
			// jid 为  chatId@domin/chat组成
			String chatId = chat.getParticipant().split("@")[0];  // 发来消息的用户
			String content = message.getBody();					// 发送来的内容.
			if(SYSTEM_USER.equals(chatId)){
				notityMessage(systemMessage, content);
			}else{
				notityMessage(chatMessage, content);
			}
		}
		
	}

}
